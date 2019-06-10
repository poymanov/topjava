package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {
    void update(Meal resume);

    void save(Meal resume);

    Meal get(int id);

    void delete(int id);

    List<Meal> getAll();
}
