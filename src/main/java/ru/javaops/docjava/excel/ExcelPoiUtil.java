package ru.javaops.docjava.excel;

import jakarta.xml.bind.JAXBException;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Sheet;
import ru.javaops.docjava.schema.User;
import ru.javaops.docjava.xml.jaxb.JaxbUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ru.javaops.docjava.util.Util.DATE_TIME_FORMATTER;
import static ru.javaops.docjava.util.Util.toDateTime;

@UtilityClass
public class ExcelPoiUtil {
    public void convert(File inputFile, File templateFile, File outputFile, Map<String, Object> params) throws IOException, JAXBException {
        List<User> users = JaxbUtil.unmarshalAndFilter(inputFile, params).getUsers().getUser();
        ExcelPoiConverter converter = ExcelPoiConverter.of(templateFile);

        converter.convert(outputFile, workbook -> {
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                Sheet sheet = workbook.cloneSheet(0);
                workbook.setSheetName(i + 1, user.getEmail());
                converter.resolvePlaceholders(sheet,
                        Map.of("name", user::getName,
                                "enabled", () -> user.isEnabled() ? "enabled" : "disabled",
                                "email", user::getEmail,
                                "caloriesPerDay", user::getCaloriesPerDay,
                                // https://stackoverflow.com/questions/42210257/548473
                                "registered", () -> DATE_TIME_FORMATTER.format(toDateTime(user.getRegistered())),
                                "roles", () -> user.getRoles().toString())
                );
            }
            workbook.removeSheetAt(0);
        });
        System.out.println("Convert to Excel completed successfully, result in " + outputFile.getAbsolutePath());
    }
}
