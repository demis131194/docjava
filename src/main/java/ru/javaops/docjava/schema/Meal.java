package ru.javaops.docjava.schema;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Meal", namespace = "http://javaops.ru")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
    @XmlValue
    protected String value;
    @XmlAttribute(name = "calories", required = true)
    protected int calories;
    @XmlAttribute(name = "dateTime", required = true)
    @XmlJavaTypeAdapter(type = LocalDateTime.class, value = LocalDateTimeXmlAdapter.class)
    protected LocalDateTime dateTime;
    @XmlAttribute(name = "excess")
    protected Boolean excess;
}
