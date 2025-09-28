package ru.javaops.docjava.xml.jaxb;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.UnmarshalException;
import org.junit.jupiter.api.Test;
import ru.javaops.docjava.schema.UsersWithMeals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.docjava.TestData.*;

public class JaxbUtilTest {

    @Test
    public void unmarshalOK() throws JAXBException, IOException {
        UsersWithMeals usersWithMeals = JaxbUtil.unmarshal(inputFile);
        assertEquals(new UsersWithMeals.Users(List.of(user, admin, guest)), usersWithMeals.getUsers());
    }

    @Test
    void unmarshalNOK() {
        assertThrows(UnmarshalException.class,
                () -> JaxbUtil.unmarshal(new File("in/badXmlFile.xml")), "Except bad format exception");
    }
}