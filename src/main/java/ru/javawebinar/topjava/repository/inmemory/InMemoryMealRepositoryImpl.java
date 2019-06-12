package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            log.info("Meal saved ({})", meal.getId());
            return meal;
        }

        log.info("Meal updated ({})", meal.getId());

        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (get(id, userId) == null) {
            log.info("Failed to delete meal {}, user {}", id, userId);
            return false;
        }

        if (repository.remove(id) != null) {
            log.info("Meal deleted ({})", id);
            return true;
        } else {
            log.info("Failed to delete meal {}", id);
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);

        if (meal == null || meal.getUserId() != userId) {
            log.info("Failed to get meal {}, user {}", id, userId);
            return null;
        }

        return meal;
    }

    @Override
    public List<Meal> getAll() {
        List<Meal> meals = new ArrayList<>(repository.values());
        meals.sort(Collections.reverseOrder());
        return meals;
    }

    @Override
    public List<Meal> getAllByUserId(int userId) {
        return repository
                .values()
                .stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
    }

    public List<Meal> getAllByUserWithFilters(int userId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return getAllByUserId(userId).stream().filter(meal -> {
            return DateTimeUtil.isBetweenDate(meal.getDateTime(), dateFrom, dateTo);
        }).collect(Collectors.toList());
    }
}

