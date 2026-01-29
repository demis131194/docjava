package ru.javaops.docjava.excel;

import jakarta.xml.bind.JAXBException;
import lombok.experimental.UtilityClass;
import ru.javaops.docjava.schema.User;
import ru.javaops.docjava.xml.jaxb.JaxbUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ExcelJxlsUtil {
    public void convert(File inputFile, File templateFile, File outputFile, Map<String, Object> params) throws JAXBException, IOException {
        List<User> users = JaxbUtil.unmarshalAndFilter(inputFile, params).getUsers().getUser();
        ExcelJxlsProcessor processor = ExcelJxlsProcessor.of(templateFile);
        processor.process(outputFile, ctx -> {
            ctx.putVar("users", users);
            ctx.putVar("sheetNames", users.stream().map(User::getEmail).toList());
        });
        System.out.println("Convert to Excel completed successfully, result in " + outputFile.getAbsolutePath());
    }
}
