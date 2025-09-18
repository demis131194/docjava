
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
@XmlRootElement(name = "Meals", namespace = "http://javaops.ru")
@Data
@NoArgsConstructor
public class Meals {
    @XmlElement(name = "Meal", namespace = "http://javaops.ru")
    protected List<Meal> meal = new ArrayList<>();
}
