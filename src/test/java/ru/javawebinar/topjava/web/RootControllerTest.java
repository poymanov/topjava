package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.TestUtil.userAuth;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.USER;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void testUsers() throws Exception {
        mockMvc.perform(get("/users")
                .with(userAuth(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"));
    }

    @Test
    void testUnAuth() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void testMeals() throws Exception {
        mockMvc.perform(get("/meals")
                .with(userAuth(USER)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"));
    }

    @Test
    void testRegisterWithExistedEmail() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "test");
        params.add("email", "user@yandex.ru");
        params.add("password", "123qwe");
        params.add("calories", "2000");

        mockMvc.perform(post("/register").with(csrf())
                .params(params))
                .andDo(print())
                .andExpect(model().attributeHasFieldErrors("userTo", "email"))
                .andExpect(view().name("profile"));


    }

    @Test
    void testUpdateProfileWithSameEmail() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", String.valueOf(USER.getId()));
        params.add("name", USER.getName());
        params.add("email", USER.getEmail());
        params.add("password", USER.getPassword());
        params.add("calories", String.valueOf(USER.getCaloriesPerDay()));

        mockMvc.perform(post("/profile").with(csrf()).with(userAuth(USER))
                .params(params))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:meals"));
    }

    @Test
    void testUpdateProfileWithExistedEmail() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", String.valueOf(USER.getId()));
        params.add("name", USER.getName());
        params.add("email", ADMIN.getEmail());
        params.add("password", USER.getPassword());
        params.add("calories", String.valueOf(USER.getCaloriesPerDay()));

        mockMvc.perform(post("/profile").with(csrf()).with(userAuth(USER))
                .params(params))
                .andDo(print())
                .andExpect(model().attributeHasFieldErrors("userTo", "email"))
                .andExpect(view().name("profile"));
    }
}