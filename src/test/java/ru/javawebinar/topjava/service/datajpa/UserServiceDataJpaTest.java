package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends AbstractUserServiceTest {
    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(ADMIN_ID);
        assertMatch(user, ADMIN);
        assertMatch(user.getMeals(), ADMIN_MEAL2, ADMIN_MEAL1);
    }

    @Test
    public void getWithMealsUserNotFound() {
        thrown.expect(NotFoundException.class);
        service.getWithMeals(1);
    }

    @Test
    public void getWithMealsEmpty() {
        User newUser = service.create(new User(null, "User2", "user2@yandex.ru", "password", Role.ROLE_USER));
        User user = service.getWithMeals(newUser.getId());
        assertMatch(user, newUser);
        assertMatch(user.getMeals());
    }
}