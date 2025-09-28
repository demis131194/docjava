package ru.javaops.docjava.xml.jaxb;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.UnmarshalException;
import org.junit.jupiter.api.Test;
import ru.javaops.docjava.schema.UsersWithMeals;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.docjava.TestData.*;

public class JaxbUtilTest {

    @Test
    public void processOK() throws JAXBException, IOException {
        UsersWithMeals usersWithMeals = JaxbUtil.process(inputFile, Map.of());
        assertEquals(new UsersWithMeals.Users(getUsers()), usersWithMeals.getUsers());
    }

    @Test
    void processFiltered() throws IOException, JAXBException {
        UsersWithMeals usersWithMeals = JaxbUtil.process(inputFile, paramsMap);
        assertEquals(new UsersWithMeals.Users(getFilteredUsers()), usersWithMeals.getUsers());
    }

    @Test
    void processNOK() {
        assertThrows(UnmarshalException.class,
                () -> JaxbUtil.unmarshal(new File("in/badXmlFile.xml")), "Except bad format exception");
    }
}