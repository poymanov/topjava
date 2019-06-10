package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private static final List <Meal> BASE_MEALS = MealsUtil.initMeals();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List <MealTo> meals = new ArrayList<>();
        meals.add(MealsUtil.createWithExcess(BASE_MEALS.get(0), true));
        meals.add(MealsUtil.createWithExcess(BASE_MEALS.get(1), true));
        meals.add(MealsUtil.createWithExcess(BASE_MEALS.get(2), true));
        meals.add(MealsUtil.createWithExcess(BASE_MEALS.get(3), false));
        meals.add(MealsUtil.createWithExcess(BASE_MEALS.get(4), false));
        meals.add(MealsUtil.createWithExcess(BASE_MEALS.get(5), false));

        log.warn(meals.toString());

        request.setAttribute("meals", meals);
        request.setAttribute("dateTimeFormatter", dateTimeFormatter);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}
