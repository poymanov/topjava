package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @GetMapping
    public String index(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals/index";
    }

    @GetMapping("filter")
    public String search(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endDate);

        return "meals/index";
    }

    @RequestMapping(value = "create", method = {RequestMethod.GET, RequestMethod.POST})
    public String create(HttpServletRequest request, Model model) {
        String currentMethod = request.getMethod();

        if (currentMethod.equals(RequestMethod.GET.name())) {
            model.addAttribute("meal", new Meal());
            return "meals/create";
        } else if (currentMethod.equals(RequestMethod.POST.name())) {
            Meal meal = new Meal(
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));
            super.create(meal);
        }

        return "redirect:/meals";
    }

    @RequestMapping(value = "update", method = {RequestMethod.GET, RequestMethod.PUT})
    public String update(HttpServletRequest request, Model model) {
        String currentMethod = request.getMethod();
        int id = getId(request);

        if (currentMethod.equals(RequestMethod.GET.name())) {
            model.addAttribute("meal", super.get(id));
            return "meals/update";
        } else if (currentMethod.equals(RequestMethod.PUT.name())) {
            Meal meal = new Meal(
                    id,
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

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
