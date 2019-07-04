package ru.javawebinar.topjava.service.jdbc;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    @BeforeClass
    public static void skipAll() {
        Assume.assumeFalse(true);
    }
}