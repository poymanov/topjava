package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.common.AbstractMealServiceTest;

@ActiveProfiles(Profiles.JPA)
public class MealServiceJpaTest extends AbstractMealServiceTest {

}