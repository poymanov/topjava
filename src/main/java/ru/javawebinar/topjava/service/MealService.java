package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface MealService {
    Meal create(Meal user, int userId);

    boolean delete(int id, int userId) throws NotFoundException;

    Meal get(int id, int userId) throws NotFoundException;

    Meal update(Meal meal, int userId);

    List<Meal> getAllByUserId(int userId);

    List<Meal> getAllByUserWithFilters(int userId, LocalDateTime dateFrom, LocalDateTime dateTo);
}