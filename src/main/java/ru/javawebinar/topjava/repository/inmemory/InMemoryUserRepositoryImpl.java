package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);

        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
        }

        repository.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");

        List<User> users = new ArrayList<>(repository.values());
        users.sort(User::compareTo);

        return users;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);

        return getAll()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}