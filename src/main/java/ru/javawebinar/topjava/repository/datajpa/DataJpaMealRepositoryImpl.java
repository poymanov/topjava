package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepositoryImpl implements MealRepository {
    private static final Sort SORT_DATETIME = new Sort(Sort.Direction.DESC, "dateTime");

    @Autowired
    private CrudMealRepository crudRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && crudRepository.findByIdAndUserId(meal.getId(), userId).isEmpty()) {
            return null;
        }

        meal.setUser(crudUserRepository.getOne(userId));
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.findByIdAndUserId(id, userId).orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllByUserId(userId, SORT_DATETIME);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudRepository.findAllByUserIdAndDateTimeBetween(userId, startDate, endDate, SORT_DATETIME);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return crudRepository.findByIdWithUser(id, userId).orElse(null);
    }
}
