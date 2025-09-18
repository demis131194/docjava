package ru.javaops.docjava.schema;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "User", namespace = "http://javaops.ru")
@Data
@NoArgsConstructor
public class User {
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "email", required = true)
    protected String email;
    @XmlAttribute(name = "password", required = true)
    protected String password;
    @XmlAttribute(name = "caloriesPerDay", required = true)
    protected int caloriesPerDay;
    @XmlAttribute(name = "registered", required = true)
    @XmlSchemaType(name = "dateTime")
    protected Date registered;
    @XmlAttribute(name = "enabled", required = true)
    protected boolean enabled;
    @XmlAttribute(name = "roles")
    protected List<RoleTypes> roles = new ArrayList<>();
    @XmlElement(name = "Meals", namespace = "http://javaops.ru")
    protected Meals meals;
}
