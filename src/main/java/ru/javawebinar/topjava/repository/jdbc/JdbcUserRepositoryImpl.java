package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
        }

        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());

        String sql = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            List<Role> roles = new ArrayList<>(user.getRoles());

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, user.getId());
                ps.setString(2, roles.get(i).name());
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }

        });

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE id=?", new RowMapperWithRoles(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE email=?", new RowMapperWithRoles(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        Map<Integer, User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", rs -> {
            Map<Integer, User> data = new LinkedHashMap<>();

            while (rs.next()) {
                int id = rs.getInt("id");

                User user = new User();
                user.setId(id);
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setPassword(rs.getString("password"));
                user.setRoles(new LinkedHashSet<>());
                data.putIfAbsent(id, user);
            }

            return data;
        });

        jdbcTemplate.query("SELECT * FROM user_roles", rs -> {
            do {
                User user = users.get(rs.getInt("user_id"));

                if (user == null) {
                    continue;
                }

                user.getRoles().add(Role.valueOf(rs.getString("role")));
            } while (rs.next());
        });

        return new ArrayList<>(users.values());
    }

    public class RowMapperWithRoles implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> users = new LinkedHashMap<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String name = rs.getString("name");
                int calories = rs.getInt("calories_per_day");
                boolean enabled = rs.getBoolean("enabled");
                String password = rs.getString("password");

                User user = users.computeIfAbsent(id, i -> {
                    User newUser = new User();
                    newUser.setId(id);
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setCaloriesPerDay(calories);
                    newUser.setEnabled(enabled);
                    newUser.setPassword(password);
                    newUser.setRoles(new LinkedHashSet<>());
                    return newUser;
                });

                user.getRoles().add(Role.valueOf(rs.getString("role")));
            }

            return new ArrayList<>(users.values());
        }
    }

}
