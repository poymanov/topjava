package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        getFilteredWithExceeded2(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    /**
     * via loops
     */
    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> daysCalories = new HashMap<>();
        List<UserMealWithExceed> mealsListWithExceed = new ArrayList<>();

        for (UserMeal meal : mealList) {
            LocalDate date = meal.getDateTime().toLocalDate();
            Integer calories = meal.getCalories();

            daysCalories.merge(date, calories, Integer::sum);
        }

        for (UserMeal meal : mealList) {
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                LocalDate date = meal.getDateTime().toLocalDate();
                boolean isExceeded = daysCalories.get(date) > caloriesPerDay;
                mealsListWithExceed.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExceeded));
            }
        }

        return mealsListWithExceed;
    }

    /**
     * via Java 8 Stream API
     */
    public static List<UserMealWithExceed> getFilteredWithExceeded2(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> daysCalories = mealList
                .stream()
                .collect(Collectors.toMap(meal -> meal.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));

        return mealList.stream()
                .filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> {
                    LocalDate date = meal.getDateTime().toLocalDate();
                    boolean isExceeded = daysCalories.get(date) > caloriesPerDay;

                    return new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExceeded);
                }).collect(Collectors.toList());

    }
}
