package ru.javawebinar.topjava.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import ru.javawebinar.topjava.HasEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExistedEmailUserValidator implements ConstraintValidator<ExistedEmailUser, HasEmail> {
    @Autowired
    private UserRepository repository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean isValid(HasEmail user, ConstraintValidatorContext context) {
        User existedUser;

        try {
            existedUser = repository.getByEmail(user.getEmail());
        } catch (Exception e) {
            return true;
        }

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
