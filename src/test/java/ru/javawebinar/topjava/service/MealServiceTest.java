package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal actualMeal = service.get(USER_MEAL_1.getId(), USER_ID);
        assertMatch(actualMeal, USER_MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void getAnotherUserMeal() {
        service.get(ADMIN_MEAL_1.getId(), USER_ID);
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_1.getId(), USER_ID);
        assertMatch(service.getAll(USER_ID), USER_MEAL_3, USER_MEAL_2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAnotherUserMeal() {
        service.delete(ADMIN_MEAL_2.getId(), USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFoundMeal() {
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFoundUser() {
        service.delete(USER_MEAL_1.getId(), 1);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> empty = service.getBetweenDateTimes(LocalDateTime.of(2019, Month.JANUARY, 1, 10, 0), LocalDateTime.of(2019, Month.JANUARY, 1, 12, 0), USER_ID);
        assertMatch(empty, new ArrayList<>());

        List<Meal> withTwoMeals = service.getBetweenDateTimes(LocalDateTime.of(2019, Month.JUNE, 1, 9, 0), LocalDateTime.of(2019, Month.JUNE, 1, 15, 0), USER_ID);
        assertMatch(withTwoMeals, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void update() {
        Meal meal = new Meal(USER_MEAL_1);
        meal.setDescription("new description");
        meal.setCalories(5000);
        service.update(meal, USER_ID);
        assertMatch(service.get(meal.getId(), USER_ID), meal);
    }

    @Test(expected = NotFoundException.class)
    public void updateAnotherUserMeal() {
        Meal meal = new Meal();
        meal.setId(100005);
        meal.setDescription("Завтрак");
        meal.setCalories(1000);
        meal.setDateTime(LocalDateTime.of(2019, Month.APRIL, 1, 8, 3));
        service.update(meal, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFoundMeal() {
        service.update(new Meal(1, LocalDateTime.now(), "new description", 5000), USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFoundUser() {
        Meal meal = new Meal(USER_MEAL_1);
        service.update(meal, 1);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(2019, Month.JUNE, 1, 19, 30, 0), "Перекус", 200);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(USER_ID), USER_MEAL_3, newMeal, USER_MEAL_2, USER_MEAL_1);
    }

    @Test(expected = org.springframework.dao.DuplicateKeyException.class)
    public void createExisted() {
        Meal existedMeal = new Meal(USER_MEAL_1);
        existedMeal.setId(null);
        service.create(existedMeal, USER_ID);
    }
}