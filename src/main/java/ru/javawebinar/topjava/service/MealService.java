package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService {
    Meal create(Meal user, int userId);

    boolean delete(int id, int userId) throws NotFoundException;

    Meal get(int id, int userId) throws NotFoundException;

    Meal update(Meal meal, int userId);

    List<MealTo> getAllByUserId(int userId);

    List<MealTo> getAllByUserWithFilters(int userId, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo);
}