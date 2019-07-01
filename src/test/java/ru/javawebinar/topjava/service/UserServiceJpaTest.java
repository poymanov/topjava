package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.common.AbstractUserServiceTest;

@ActiveProfiles(Profiles.JPA)
public class UserServiceJpaTest extends AbstractUserServiceTest {

}