package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
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
    public void delete(int id, int userId) {
        if (get(id, userId) == null) {
            log.info("Failed to delete meal {}, user {}", id, userId);
            return;
        }

        repository.remove(id);
        log.info("Meal deleted ({})", id);
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
    public Collection<Meal> getAll() {
        List<Meal> meals = new ArrayList<>(repository.values());
        meals.sort(Collections.reverseOrder());
        return meals;
    }
}

