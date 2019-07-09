package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    // false if not found
    boolean delete(int id);

    // null if not found
    User get(int id);

    // null if not found
    User getByEmail(String email);

    List<User> getAll();

    boolean deleteRole(int userId, Role role);

    boolean addRole(int userId, Role role);

    default User getWithMeals(int id) {
        throw new UnsupportedOperationException();
    }
}