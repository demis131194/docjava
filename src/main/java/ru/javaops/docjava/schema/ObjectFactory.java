package ru.javaops.docjava.schema;

import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.javaops.docjava.schema
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UsersWithMeals }
     */
    public UsersWithMeals createUsersWithMeals() {
        return new UsersWithMeals();
    }

    /**
     * Create an instance of {@link UsersWithMeals.Users }
     */
    public UsersWithMeals.Users createUsersWithMealsUsers() {
        return new UsersWithMeals.Users();
    }

    /**
     * Create an instance of {@link User }
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link Meals }
     */
    public Meals createMeals() {
        return new Meals();
    }

    /**
     * Create an instance of {@link Meal }
     */
    public Meal createMeal() {
        return new Meal();
    }

}
