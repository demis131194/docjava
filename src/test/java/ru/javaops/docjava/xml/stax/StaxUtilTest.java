package ru.javaops.docjava.xml.stax;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.javaops.docjava.schema.User;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static ru.javaops.docjava.TestData.inputFile;
import static ru.javaops.docjava.TestData.paramsMap;

public class StaxUtilTest {

    @ParameterizedTest
    @MethodSource("ru.javaops.docjava.TestData#getUsers")
    void process(User user) throws XMLStreamException, JAXBException, IOException {
        StaxUtil.process(inputFile, Map.of(), user.getEmail(), new File("out/stax" + user.getName() + ".xml"));
    }


    @ParameterizedTest
    @MethodSource("ru.javaops.docjava.TestData#getFilteredUsers")
    void processFiltered(User user) throws XMLStreamException, JAXBException, IOException {
        StaxUtil.process(inputFile, paramsMap, user.getEmail(), new File("out/stax" + user.getName() + "Filtered.xml"));
    }
}