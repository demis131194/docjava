package ru.javaops.docjava;

import ru.javaops.docjava.schema.Meal;
import ru.javaops.docjava.schema.Meals;
import ru.javaops.docjava.schema.RoleTypes;
import ru.javaops.docjava.schema.User;
import ru.javaops.docjava.util.Util;

import java.io.File;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.of;

public class TestData {
    public static final File inputFile = new File("in/usersWithMeals.xml");

    public static final Map<String, Object> paramsMap =
            Util.parseParams(List.of("startDate=2023-01-30", "endDate=2023-01-30", "startTime=11:00", "endTime=19:00"));

    public static final Date registered = new Date(123, Calendar.JANUARY, 1, 13, 0, 0);

    public static final Meal userLunch = new Meal("Обед", 1000, of(2023, Month.JANUARY, 30, 13, 0, 0), false);
    public static final Meals userMeals = new Meals(List.of(
            new Meal("Завтрак", 500, of(2023, Month.JANUARY, 30, 10, 0, 0), false),
            userLunch,
            new Meal("Ужин", 500, of(2023, Month.JANUARY, 30, 20, 0, 0), false),
            new Meal("Еда на граничное значение", 100, of(2023, Month.JANUARY, 31, 0, 0, 0), true),
            new Meal("Завтрак", 500, of(2023, Month.JANUARY, 31, 10, 0, 0), true),
            new Meal("Обед", 1000, of(2023, Month.JANUARY, 31, 13, 0, 0), true),
            new Meal("Ужин", 510, of(2023, Month.JANUARY, 31, 20, 0, 0), true)
    ));
    public static final User user = new User("User", "user@yandex.ru", "password", 2005, registered, true,
            List.of(RoleTypes.USER), null);

    public static final Meal adminLunch = new Meal("Админ ланч", 510, of(2023, Month.JANUARY, 30, 14, 0, 0), false);
    public static final Meals adminMeals = new Meals(List.of(
            adminLunch,
            new Meal("Админ ужин", 730, of(2023, Month.JANUARY, 30, 21, 0, 0), false)
    ));
    public static final User admin = new User("Admin", "admin@gmail.com", "admin", 1900, registered, true,
            List.of(RoleTypes.ADMIN, RoleTypes.USER), null);

    public static final User guest = new User("Guest", "guest@gmail.com", "guest", 2000, registered, false,
            List.of(), null);

    public static List<User> getUsers() {
        user.setMeals(userMeals);
        admin.setMeals(adminMeals);
        return List.of(user, admin, guest);
    }

    public static List<User> getFilteredUsers() {
        user.setMeals(new Meals(List.of(userLunch)));
        admin.setMeals(new Meals(List.of(adminLunch)));
        return List.of(user, admin, guest);
    }
}