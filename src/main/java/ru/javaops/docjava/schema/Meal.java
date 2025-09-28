package ru.javaops.docjava.schema;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}
