package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    Meal save(Meal meal);

    boolean delete(int id, int userId);

    Meal get(int id, int userId);

    List<Meal> getAll();

    List<Meal> getAllByUserId(int userId);

    List<Meal> getAllByUserWithFilters(int userId, LocalDateTime dateFrom, LocalDateTime dateTo);
}
