package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.IdGenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapStorage implements Storage {
    protected Map<Integer, Meal> storage = new LinkedHashMap<>();

    @Override
    public void update(Meal meal) {
        storage.put(meal.getId(), meal);
    }

    @Override
    public void save(Meal meal) {
        if (meal.getId() == 0) {
            meal = new Meal(IdGenerator.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
        }

        storage.put(meal.getId(), meal);
    }

    @Override
    public Meal get(int id) {
        return storage.get(id);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }
}
