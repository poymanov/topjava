package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class MealController extends AbstractMealController {

    @Autowired
    public MealController(MealService service) {
        super(service);
    }

    @GetMapping("filter")
    public String search(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));

        return "meals";
    }

    @GetMapping("get")
    public String get(HttpServletRequest request, Model model) {
        Meal meal;

        if (request.getParameter("id") == null) {
            meal = new Meal();
        } else {
            meal = super.get(getId(request));
        }

        model.addAttribute("meal", meal);
        return "mealsForm";
    }

    @PostMapping("save")
    public String save(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (request.getParameter("id").isBlank()) {
            super.create(meal);
        } else {
            int id = getId(request);
            super.update(meal, id);
        }

        return "redirect:/meals";
    }

    @DeleteMapping("delete")
    public String delete(HttpServletRequest request) {
        super.delete(getId(request));
        return "redirect:/meals";
    }
}
