package ru.javaops.docjava.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "UsersWithMeals", namespace = "http://javaops.ru")
@Data
public class UsersWithMeals {

    @XmlElement(name = "Users", namespace = "http://javaops.ru", required = true)
    protected UsersWithMeals.Users users;

    @XmlAccessorType(XmlAccessType.FIELD)
    @Data
    @NoArgsConstructor
    public static class Users {
        @XmlElement(name = "User", namespace = "http://javaops.ru")
        protected List<User> user = new ArrayList<>();
    }
}
