package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal, int userId) {
        checkNew(meal);
        return checkNotFound(repository.save(meal, userId), null);
    }

    @Override
    public boolean delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public Meal update(Meal meal, int userId) {
        return checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    @Override
    public List<MealTo> getAllByUserId(int userId) {
        return MealsUtil.getWithExcess(repository.getAllByUserId(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllByUserWithFilters(int userId, LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {
        return MealsUtil.getWithExcessWithFilter(repository.getAllByUserId(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY, meal -> DateTimeUtil.isBetweenDate(meal, dateFrom, dateTo, timeFrom, timeTo));
    }
}