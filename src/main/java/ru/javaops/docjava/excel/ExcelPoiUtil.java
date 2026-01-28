package ru.javaops.docjava.excel;

import jakarta.xml.bind.JAXBException;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import ru.javaops.docjava.schema.Meal;
import ru.javaops.docjava.schema.User;
import ru.javaops.docjava.xml.jaxb.JaxbUtil;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static ru.javaops.docjava.excel.ExcelPoiConverter.*;
import static ru.javaops.docjava.util.Util.DATE_TIME_FORMATTER;
import static ru.javaops.docjava.util.Util.toDateTime;

@UtilityClass
public class ExcelPoiUtil {
    public void convert(File inputFile, File templateFile, File outputFile, Map<String, Object> params) throws IOException, JAXBException {
        List<User> users = JaxbUtil.unmarshalAndFilter(inputFile, params).getUsers().getUser();
        ExcelPoiConverter converter = of(templateFile);

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
                converter.doProcessors(Map.of(
                        "meals", ca -> {
                            if (user.getMeals() == null) {
                                removeRow(sheet, ca.getRow());
                            } else {
                                Iterator<Meal> iterator = user.getMeals().getMeal().iterator();
                                Meal first = iterator.next();
                                writeMeal(first, sheet, ca);
                                while (iterator.hasNext()) {
                                    insertRow(sheet, ca.getRow());
                                    ca = nextRow(ca, 1);
                                    writeMeal(iterator.next(), sheet, ca);
                                }
                            }
                        }));
            }
            workbook.removeSheetAt(0);
        });
        System.out.println("Convert to Excel completed successfully, result in " + outputFile.getAbsolutePath());
    }

    private void writeMeal(Meal meal, Sheet sheet, CellAddress ca) {
        setCell(sheet, ca, DATE_TIME_FORMATTER.format(meal.getDateTime()));
        setCell(sheet, nextCell(ca, 1), meal.getValue());
        setCell(sheet, nextCell(ca, 2), meal.getCalories());
        setCell(sheet, nextCell(ca, 3), meal.getExcess());
    }
}
