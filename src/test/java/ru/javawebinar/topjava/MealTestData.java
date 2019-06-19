package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int MEAL_START_SEQ = START_SEQ + 2;
    public static final Meal MEAL_1 = new Meal(MEAL_START_SEQ, LocalDateTime.of(2019, Month.JUNE, 1, 9, 10), "Завтрак", 500);
    public static final Meal MEAL_2 = new Meal(MEAL_START_SEQ + 1, LocalDateTime.of(2019, Month.JUNE, 1, 14, 20), "Обед", 1000);
    public static final Meal MEAL_3 = new Meal(MEAL_START_SEQ + 2, LocalDateTime.of(2019, Month.JUNE, 1, 21, 40), "Ужин", 500);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}