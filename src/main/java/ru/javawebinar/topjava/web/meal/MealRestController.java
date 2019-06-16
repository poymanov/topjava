package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAllByUser() {
        int userId = getAuthUserId();
        log.info("getAll for user {}", userId);

        return MealsUtil.getWithExcess(service.getAllByUserId(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllWithFilters(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo){
        int userId = getAuthUserId();
        log.info("getAll for user {}", userId);

        return MealsUtil.getWithExcess(service.getAllByUserWithFilters(userId, dateFrom, dateTo, timeFrom, timeTo), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal get(int id) {
        return checkNotFoundWithId(service.get(id, getAuthUserId()), id);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);

        Meal savedMeal = service.create(meal, getAuthUserId());

        if (savedMeal == null) {
            throw new NotFoundException("Failed to saved meal");
        } else {
            return savedMeal;
        }
    }

    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(service.delete(id, getAuthUserId()), id);
    }

    public void update(Meal meal, int id) {
        log.info("update {}", id);
        checkNotFoundWithId(service.update(meal, getAuthUserId()), id);
    }

    private int getAuthUserId() {
        if (SecurityUtil.isAuth()) {
            return SecurityUtil.getAuthUserId();
        } else {
            throw new RuntimeException("Current user not authorized");
        }
    }
}