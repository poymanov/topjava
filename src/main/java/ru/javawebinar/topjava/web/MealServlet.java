package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MemoryStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MealServlet extends HttpServlet {
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private Storage storage = new MemoryStorage();

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
            meal = storage.get(Integer.parseInt(id));
            meal.setDateTime(date);
            meal.setDescription(description);
            meal.setCalories(calories);
        } else {
            isNew = true;
            meal = new Meal(date, description, calories);
        }

        if (isNew) {
            storage.save(meal);
        } else {
            storage.update(meal);
        }

        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String paramAction = request.getParameter("action");
        String paramId = request.getParameter("id");

        String action = paramAction != null ? paramAction : "list";
        int mealId = paramId != null ? Integer.parseInt(paramId) : 0;

        Meal meal;
        String template;

        switch (action) {
            case "create":
                meal = new Meal(null, null, 0);
                template = "jsp/meals/edit.jsp";
                request.setAttribute("meal", meal);
                break;
            case "delete":
                storage.delete(mealId);
                response.sendRedirect("meals");
                return;
            case "update":
                meal = storage.get(mealId);
                template = "jsp/meals/edit.jsp";
                request.setAttribute("meal", meal);
                break;
            default:
                List<MealTo> meals = MealsUtil.getFilteredWithExcess(storage.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
                request.setAttribute("meals", meals);
                template = "jsp/meals/list.jsp";
        }

        request.setAttribute("dateTimeFormatter", dateTimeFormatter);
        request.getRequestDispatcher(template).forward(request, response);
    }
}
