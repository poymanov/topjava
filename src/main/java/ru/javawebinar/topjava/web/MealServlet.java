package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealRestController controller;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!SecurityUtil.isAuth()) {
            response.sendRedirect("users");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        int userId = SecurityUtil.getAuthUserId();

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id), userId,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (meal.isNew()) {
            log.info("Create {}", meal);
            try {
                controller.create(meal);
            } catch (Exception e) {
                throw new ServletException(e.getMessage());
            }
        } else {
            log.info("Update {}", meal);
            try {
                controller.update(meal, Integer.parseInt(id));
            } catch (Exception e) {
                throw new ServletException(e.getMessage());
            }
        }


        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!SecurityUtil.isAuth()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendRedirect("users");
            return;
        }


        String action = request.getParameter("action");
        int userId = SecurityUtil.getAuthUserId();

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                try {
                    controller.delete(id);
                } catch (Exception e) {
                    throw new ServletException(e.getMessage());
                }
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal;
                try {
                    meal = "create".equals(action) ?
                            new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                            controller.get(getId(request));
                } catch (Exception e) {
                    throw new ServletException(e.getMessage());
                }
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                log.info("getAll with filters");

                String dateFrom = request.getParameter("dateFrom");
                String dateTo = request.getParameter("dateTo");

                try {
                    request.setAttribute("meals", controller.getAllWithFilters(dateFrom, dateTo));
                } catch (Exception e) {
                    throw new ServletException(e.getMessage());
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                try {
                    request.setAttribute("meals", controller.getAllByUser());
                } catch (Exception e) {
                    throw new ServletException(e.getMessage());
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
