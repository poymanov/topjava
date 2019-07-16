package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;

class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MealsUtil.getWithExcess(MEALS, SecurityUtil.authUserCaloriesPerDay())));
    }

    @Test
    void testCreate() throws Exception {
        Meal expected = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), "Test meal", 1000);
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        Meal returned = readFromJson(action, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(mealService.getAll(USER_ID), expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void testUpdate() throws Exception {
        Meal updated = new Meal(MEAL1);
        updated.setDescription("UpdatedDescription");
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void testGetBetween() throws Exception {
        String dateFrom = DateTimeUtil.toStringDate(LocalDate.of(2015, Month.MAY, 31));
        String timeFrom = DateTimeUtil.toStringTime(LocalTime.MIN);
        String dateTo = DateTimeUtil.toStringDate(LocalDate.of(2015, Month.MAY, 31));
        String timeTo = DateTimeUtil.toStringTime(LocalTime.MAX);

        mockMvc.perform(get(REST_URL + "filter?startDate=" + dateFrom + "&startTime=" + timeFrom + "&endDate=" + dateTo + "&endTime=" + timeTo))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MealsUtil.getWithExcess(Arrays.asList(MEAL6, MEAL5, MEAL4), SecurityUtil.authUserCaloriesPerDay())));
    }

    @Test
    void testGetBetweenByDate() throws Exception {
        String dateFrom = DateTimeUtil.toStringDate(LocalDate.of(2015, Month.MAY, 31));
        String dateTo = DateTimeUtil.toStringDate(LocalDate.of(2015, Month.MAY, 31));

        mockMvc.perform(get(REST_URL + "filter?startDate=" + dateFrom + "&endDate=" + dateTo))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MealsUtil.getWithExcess(Arrays.asList(MEAL6, MEAL5, MEAL4), SecurityUtil.authUserCaloriesPerDay())));
    }

    @Test
    void testGetBetweenByTime() throws Exception {
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(13, 0);
        String timeFrom = DateTimeUtil.toStringTime(startTime);
        String timeTo = DateTimeUtil.toStringTime(endTime);

        mockMvc.perform(get(REST_URL + "filter?startTime=" + timeFrom + "&endTime=" + timeTo))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MealsUtil.getFilteredWithExcess(MEALS, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime)));

    }


}