package ru.javawebinar.topjava.repository.jdbc.postgres;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.repository.jdbc.common.AbstractJdbcMealRepository;

import java.time.LocalDateTime;

@Repository
public class JdbcMealRepositoryPostgresImpl extends AbstractJdbcMealRepository<LocalDateTime> {

    public JdbcMealRepositoryPostgresImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public LocalDateTime convertDateTime(LocalDateTime dateTime) {
        return dateTime;
    }
}
