package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAllByUser() {
        int userId = getAuthUserId();
        log.info("getAll for user {}", userId);

        return service.getAllByUserId(userId);
    }

    public List<MealTo> getAllWithFilters(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {
        int userId = getAuthUserId();
        log.info("getAll for user {}", userId);

        if (dateFrom == null) {
            dateFrom = LocalDate.MIN;
        }

        if (dateTo == null) {
            dateTo = LocalDate.MAX;
        }

        if (timeFrom == null) {
            timeFrom = LocalTime.MIN;
        }

        if (timeTo == null) {
            timeTo = LocalTime.MAX;
        }

        return service.getAllByUserWithFilters(userId, dateFrom, dateTo, timeFrom, timeTo);
    }

    public Meal get(int id) {
        return service.get(id, getAuthUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        return service.create(meal, getAuthUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, getAuthUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {}", id);
        assureIdConsistent(meal, id);
        service.update(meal, getAuthUserId());
    }

    private int getAuthUserId() {
        if (SecurityUtil.isAuth()) {
            return SecurityUtil.getAuthUserId();
        } else {
            throw new RuntimeException("Current user not authorized");
        }
    }
}