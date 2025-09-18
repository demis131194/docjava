package ru.javaops.docjava.schema;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@XmlType(name = "roleTypes", namespace = "http://javaops.ru")
@XmlEnum
@Getter
@RequiredArgsConstructor
public enum RoleTypes {

    @XmlEnumValue("admin")
    ADMIN("admin"),
    @XmlEnumValue("user")
    USER("user");
    private final String value;

    public static RoleTypes fromValue(String v) {
        for (RoleTypes c : RoleTypes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
