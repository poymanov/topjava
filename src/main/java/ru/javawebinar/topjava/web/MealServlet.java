package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MapStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private Storage storage = new MapStorage();

    @Override
    public void init() throws ServletException {
        super.init();

        List<Meal> baseMeals = MealsUtil.initMeals();

        for (Meal meal : baseMeals) {
            storage.save(meal);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        LocalDateTime date = LocalDateTime.parse(request.getParameter("date"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal meal;
        boolean isNew = false;

        if (id != null && id.trim().length() != 0) {
            Meal currentMeal = storage.get(Integer.parseInt(id));
            meal = new Meal(currentMeal.getId(), date, description, calories);
        } else {
            isNew = true;
            meal = new Meal(0, date, description, calories);
        }

        if (isNew) {
            storage.save(meal);
        } else {
            storage.update(meal);
        }

        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            List<MealTo> meals = new ArrayList<>();

            for (Meal meal : storage.getAll()) {
                meals.add(MealsUtil.createWithExcess(meal, meal.getId() < 4));
            }

            request.setAttribute("meals", meals);
            request.setAttribute("dateTimeFormatter", dateTimeFormatter);
            request.getRequestDispatcher("jsp/meals/list.jsp").forward(request, response);

            return;
        }

        String id = request.getParameter("id");
        int mealId = id != null ? Integer.parseInt(id) : 0;

        Meal meal;
        String template;

        switch (action) {
            case "create":
                meal = new Meal(mealId, null, null, 0);
                template = "jsp/meals/edit.jsp";
                break;
            case "delete":
                storage.delete(mealId);
                response.sendRedirect("meals");
                return;
            case "view":
                meal = storage.get(mealId);
                template = "jsp/meals/view.jsp";
                break;
            case "update":
                meal = storage.get(mealId);
                template = "jsp/meals/edit.jsp";
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }

        request.setAttribute("meal", meal);
        request.setAttribute("dateTimeFormatter", dateTimeFormatter);

        request.getRequestDispatcher(template).forward(request, response);
    }
}
