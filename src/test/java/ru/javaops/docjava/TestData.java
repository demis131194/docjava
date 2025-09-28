package ru.javaops.docjava;

import ru.javaops.docjava.schema.Meal;
import ru.javaops.docjava.schema.Meals;
import ru.javaops.docjava.schema.RoleTypes;
import ru.javaops.docjava.schema.User;

import java.io.File;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.time.LocalDateTime.of;

public class TestData {
    public static final File inputFile = new File("in/usersWithMeals.xml");

    public static final Date registered = new Date(123, Calendar.JANUARY, 1, 13, 0, 0);

    public static final Meals userMeals = new Meals(List.of(
            new Meal("Завтрак", 500, of(2023, Month.JANUARY, 30, 10, 0, 0), null),
            new Meal("Обед", 1000, of(2023, Month.JANUARY, 30, 13, 0, 0), null),
            new Meal("Ужин", 500, of(2023, Month.JANUARY, 30, 20, 0, 0), null),
            new Meal("Еда на граничное значение", 100, of(2023, Month.JANUARY, 31, 0, 0, 0), null),
            new Meal("Завтрак", 500, of(2023, Month.JANUARY, 31, 10, 0, 0), null),
            new Meal("Обед", 1000, of(2023, Month.JANUARY, 31, 13, 0, 0), null),
            new Meal("Ужин", 510, of(2023, Month.JANUARY, 31, 20, 0, 0), null)
    ));
    public static final User user = new User("User", "user@yandex.ru", "password", 2005, registered, true,
            List.of(RoleTypes.USER), userMeals);

    public static final Meals adminMeals = new Meals(List.of(
            new Meal("Админ ланч", 510, of(2023, Month.JANUARY, 30, 14, 0, 0), null),
            new Meal("Админ ужин", 730, of(2023, Month.JANUARY, 30, 21, 0, 0), null)
    ));
    public static final User admin = new User("Admin", "admin@gmail.com", "admin", 1900, registered, true,
            List.of(RoleTypes.ADMIN, RoleTypes.USER), adminMeals);

    public static final User guest = new User("Guest", "guest@gmail.com", "guest", 2000, registered, false,
            List.of(), null);
}