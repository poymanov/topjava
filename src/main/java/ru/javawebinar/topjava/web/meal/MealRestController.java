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

import java.util.List;

@Controller
public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getWithExcess(service.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllByUser() throws Exception {
        int userId = getAuthUserId();
        log.info("getAll for user {}", userId);

        return MealsUtil.getWithExcess(service.getAllByUserId(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal get(int id) throws Exception {
        int userId = getAuthUserId();
        log.info("get {} for user {}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) throws Exception {
        log.info("create {}", meal);
        checkNew(meal);

        int userId = getAuthUserId();
        meal.setUserId(userId);

        return service.create(meal);
    }

    public void delete(int id) throws Exception {
        log.info("delete {}", id);
        int userId = getAuthUserId();
        service.delete(id, userId);
    }

    public void update(Meal meal, int id) throws Exception {
        int userId = getAuthUserId();
        log.info("update {} for user {}", id, userId);
        service.update(meal, userId);
    }

    private int getAuthUserId() throws Exception {
        if (SecurityUtil.isAuth()) {
            return SecurityUtil.getAuthUserId();
        } else {
            throw new Exception("Current user not authorized");
        }
    }
}