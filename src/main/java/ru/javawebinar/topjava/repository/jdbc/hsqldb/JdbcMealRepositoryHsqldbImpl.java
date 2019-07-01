package ru.javawebinar.topjava.repository.jdbc.hsqldb;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.repository.jdbc.common.AbstractJdbcMealRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public class JdbcMealRepositoryHsqldbImpl extends AbstractJdbcMealRepository<Timestamp> {

    public JdbcMealRepositoryHsqldbImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public Timestamp convertDateTime(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }
}
