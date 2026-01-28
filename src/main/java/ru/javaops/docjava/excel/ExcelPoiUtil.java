package ru.javaops.docjava.excel;

import jakarta.xml.bind.JAXBException;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import ru.javaops.docjava.schema.Meal;
import ru.javaops.docjava.schema.User;
import ru.javaops.docjava.xml.jaxb.JaxbUtil;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static ru.javaops.docjava.excel.ExcelPoiConverter.*;
import static ru.javaops.docjava.excel.ExcelPoiUtil.CellType.*;
import static ru.javaops.docjava.util.Util.DATE_TIME_FORMATTER;
import static ru.javaops.docjava.util.Util.toDateTime;

@UtilityClass
public class ExcelPoiUtil {
    enum CellType {
        USER_ENABLED,
        USER_DISABLED,
        MEAL_NOT_EXCEEDED,
        MEAL_EXCEEDED,
    }

    //    https://stackoverflow.com/a/77197761/548473
    private final EnumMap<CellType, CellStyle> cellStyles = new EnumMap<>(CellType.class);

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
                                "email", user::getEmail,
                                "caloriesPerDay", user::getCaloriesPerDay,
                                // https://stackoverflow.com/questions/42210257/548473
                                "registered", () -> DATE_TIME_FORMATTER.format(toDateTime(user.getRegistered())),
                                "roles", () -> user.getRoles().toString())
                );
                converter.doProcessors(Map.of(
                        "enabled", cellAddr -> {
                            Cell cell = getCell(sheet, cellAddr);
                            cell.setCellValue(user.isEnabled() ? "enabled" : "disabled");
                            cellStyles.computeIfAbsent(USER_ENABLED, cellType -> ExcelPoiConverter.createColorCellStyle(sheet, cellAddr, IndexedColors.GREEN.getIndex()));
                            cellStyles.computeIfAbsent(USER_DISABLED, cellType -> ExcelPoiConverter.createColorCellStyle(sheet, cellAddr, IndexedColors.RED.getIndex()));
                            cell.setCellStyle(cellStyles.get(user.isEnabled() ? USER_ENABLED : USER_DISABLED));
                        },
                        "meals", ca -> {
                            if (user.getMeals() == null) {
                                removeRow(sheet, ca.getRow());
                            } else {
                                final CellAddress fca = ca;
                                cellStyles.computeIfAbsent(MEAL_NOT_EXCEEDED, cellType -> ExcelPoiConverter.createColorCellStyle(sheet, fca, IndexedColors.GREEN.getIndex()));
                                cellStyles.computeIfAbsent(MEAL_EXCEEDED, cellType -> ExcelPoiConverter.createColorCellStyle(sheet, fca, IndexedColors.RED.getIndex()));
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
            cellStyles.clear();
        });
        System.out.println("Convert to Excel completed successfully, result in " + outputFile.getAbsolutePath());
    }

    private void writeMeal(Meal meal, Sheet sheet, CellAddress ca) {
        CellStyle style = cellStyles.get(meal.getExcess() ? CellType.MEAL_EXCEEDED : MEAL_NOT_EXCEEDED);
        setCell(sheet, ca, DATE_TIME_FORMATTER.format(meal.getDateTime()), style);
        setCell(sheet, nextCell(ca, 1), meal.getValue(), style);
        setCell(sheet, nextCell(ca, 2), meal.getCalories(), style);
        setCell(sheet, nextCell(ca, 3), meal.getExcess(), style);
    }
}
