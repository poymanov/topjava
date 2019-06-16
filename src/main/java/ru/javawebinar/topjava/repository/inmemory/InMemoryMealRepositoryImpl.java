package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        List<Meal> meals = MealsUtil.MEALS;

        for (int i = 0; i < meals.size(); i++) {
            save(meals.get(i), i < 3 ? 1 : 2);
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);

            if (!repository.containsKey(userId)) {
                repository.put(userId, new ConcurrentHashMap<>());
            }

            log.info("Meal is saving ({})", meal.getId());

            if (repository.get(userId).putIfAbsent(meal.getId(), meal) == null) {
                return meal;
            }

            return null;
        }

        log.info("Meal is updating ({})", meal.getId());

        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.get(userId).get(id);
    }

    @Override
    public List<Meal> getAllByUserId(int userId) {
        return getFilteredUserMeals(userId, meal -> true);
    }

    public List<Meal> getAllByUserWithFilters(int userId, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {
        return getFilteredUserMeals(userId, meal -> DateTimeUtil.isBetweenDate(meal, dateFrom, dateTo, timeFrom, timeTo));
    }

    private List<Meal> getFilteredUserMeals(int userId, Predicate<Meal> filter) {
        return repository
                .get(userId).values()
                .stream()
                .filter(filter)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }
}

