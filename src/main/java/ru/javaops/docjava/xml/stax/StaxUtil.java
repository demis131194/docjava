package ru.javaops.docjava.xml.stax;

import jakarta.xml.bind.JAXBException;
import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;
import ru.javaops.docjava.schema.User;
import ru.javaops.docjava.util.MealsUtil;
import ru.javaops.docjava.xml.jaxb.JaxbUtil;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@UtilityClass
public class StaxUtil {
    public static void process(File inputFile, Map<String, Object> params, String email, File outputFile) throws XMLStreamException, JAXBException, IOException {
        User user = findUser(inputFile, email);
        if (user == null)
            throw new IllegalArgumentException("User '" + email + "' not found in " + inputFile.getAbsolutePath());
        MealsUtil.filterAndAddExcess(user, params);
        JaxbUtil.marshal(user, outputFile);
        System.out.println("StAX processing completed successfully, result in " + outputFile.getAbsolutePath());
    }

    public static User findUser(File inputFile, @NonNull String email) throws XMLStreamException, JAXBException {
        StaxProcessor processor = StaxProcessor.of(inputFile);
        while (processor.startElement("User", "Users")) {
            if (email.equals(processor.getAttribute("email"))) {
                return JaxbUtil.unmarshalUser(processor.getReader());
            }
        }
        return null;
    }
}
