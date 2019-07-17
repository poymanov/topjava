package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Locale;

public class CustomTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return DateTimeUtil.parseLocalTime(text);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return DateTimeUtil.toStringTime(object);
    }
}
