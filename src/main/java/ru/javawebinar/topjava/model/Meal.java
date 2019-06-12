package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Meal extends AbstractBaseEntity implements Comparable<Meal> {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final int userId;

    public Meal(Integer userId, LocalDateTime dateTime, String description, int calories) {
        this(null, userId, dateTime, description, calories);
    }

    public Meal(Integer id, Integer userId, LocalDateTime dateTime, String description, int calories) {
        super(id);
        Objects.requireNonNull(userId, "UserId must not be null");
        this.userId = userId;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return calories == meal.calories &&
                userId == meal.userId &&
                dateTime.equals(meal.dateTime) &&
                description.equals(meal.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, description, calories, userId);
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                "userId=" + userId +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }

    @Override
    public int compareTo(Meal o) {
        return dateTime.compareTo(o.getDateTime());
    }
}
