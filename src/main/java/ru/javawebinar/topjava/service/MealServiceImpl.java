package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    @Override
    public boolean delete(int id, int userId) throws NotFoundException {
        return repository.delete(id, userId);
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.get(id, userId);
    }

    @Override
    public Meal update(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    @Override
    public List<Meal> getAllByUserId(int userId) {
        return repository.getAllByUserId(userId);
    }

    public List<Meal> getAllByUserWithFilters(int userId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return repository.getAllByUserWithFilters(userId, dateFrom, dateTo);
    }
}