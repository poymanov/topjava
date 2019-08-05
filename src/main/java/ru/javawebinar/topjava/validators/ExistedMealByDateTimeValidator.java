package ru.javawebinar.topjava.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ExistedMealByDateTimeValidator implements ConstraintValidator<ExistedMealByDateTime, Meal> {
    @Autowired
    private MealRepository repository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean isValid(Meal meal, ConstraintValidatorContext context) {
        if (meal.getDateTime() == null) {
            return true;
        }

        int userId;
        Meal existedMeal;

        try {
            userId = SecurityUtil.authUserId();
            existedMeal = repository.getByDateTime(meal.getDateTime(), userId);
        } catch (Exception e) {
            return true;
        }

        if (existedMeal != null && (meal.isNew() || !existedMeal.getId().equals(meal.getId()))) {
            addError(context);
            return false;
        }

        return true;
    }

    private void addError(ConstraintValidatorContext context) {
        String message = messageSource.getMessage("meal.invalidDateTime", null, LocaleContextHolder.getLocale());
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("dateTime")
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }
}
