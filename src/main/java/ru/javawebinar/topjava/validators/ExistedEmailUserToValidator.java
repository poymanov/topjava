package ru.javawebinar.topjava.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistedEmailUserToValidator implements ConstraintValidator<ExistedEmailUserTo, UserTo> {
    @Autowired
    private UserRepository repository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean isValid(UserTo user, ConstraintValidatorContext context) {
        User existedUser = repository.getByEmail(user.getEmail());

        if (existedUser != null && (user.isNew() || !existedUser.getId().equals(user.getId()))) {
            addError(context);
            return false;
        }

        return true;
    }

    private void addError(ConstraintValidatorContext context) {
        String message = messageSource.getMessage("user.invalidEmail", null, LocaleContextHolder.getLocale());
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("email")
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }
}
