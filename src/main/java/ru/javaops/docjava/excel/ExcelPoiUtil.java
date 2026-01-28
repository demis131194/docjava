package ru.javaops.docjava.excel;

import jakarta.xml.bind.JAXBException;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.*;
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
import java.util.function.Consumer;

import static ru.javaops.docjava.excel.ApachePoiUtil.*;
import static ru.javaops.docjava.excel.ExcelPoiConverter.of;
import static ru.javaops.docjava.excel.ExcelPoiUtil.CellType.*;
import static ru.javaops.docjava.util.Util.format;

@UtilityClass
public class ExcelPoiUtil {
    enum CellType {
        USER_ENABLED,
        USER_DISABLED,
        MEAL_NOT_EXCEEDED,
        MEAL_EXCEEDED,
    }

    private static final EnumMap<CellType, IndexedColors> COLOR_MAP = new EnumMap<>(Map.of(
            USER_ENABLED, IndexedColors.GREEN,
            USER_DISABLED, IndexedColors.RED,
            MEAL_NOT_EXCEEDED, IndexedColors.GREEN,
            MEAL_EXCEEDED, IndexedColors.RED
    ));

    //    https://stackoverflow.com/a/77197761/548473
    public void convert(File inputFile, File templateFile, File outputFile, Map<String, Object> params) throws IOException, JAXBException {
        List<User> users = JaxbUtil.unmarshalAndFilter(inputFile, params).getUsers().getUser();
        ExcelPoiConverter converter = of(templateFile);
        converter.convert(outputFile, new Consumer<>() {
            CellStyleFactory<CellType> cellStyleFactory;

            @Override
            public void accept(Workbook workbook) {
                cellStyleFactory = CellStyleFactory.of(workbook, CellType.class);
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    Sheet sheet = workbook.cloneSheet(0);
                    workbook.setSheetName(i + 1, user.getEmail());
                    converter.resolvePlaceholders(sheet,
                            Map.of("name", user::getName,
                                    "email", user::getEmail,
                                    "caloriesPerDay", user::getCaloriesPerDay,
                                    // https://stackoverflow.com/questions/42210257/548473
                                    "registered", () -> format(user.getRegistered()),
                                    "roles", () -> user.getRoles().toString())
                    );
                    converter.doProcessors(Map.of(
                            "enabled", cellAddr -> {
                                Cell cell = getCell(sheet, cellAddr);
                                setCell(cell, user.isEnabled() ? "enabled" : "disabled",
                                        createColorCellStyle(user.isEnabled() ? USER_ENABLED : USER_DISABLED, cell.getCellStyle()));
                            },
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
            }

            private CellStyle createColorCellStyle(CellType key, CellStyle cloneStyle) {
                return cellStyleFactory.createColorCellStyle(key, cloneStyle, COLOR_MAP.get(key));
            }

            private void writeMeal(Meal meal, Sheet sheet, CellAddress ca) {
                Cell cloneCell = getCell(sheet, ca);
                CellStyle style = createColorCellStyle(meal.getExcess() ? MEAL_EXCEEDED : MEAL_NOT_EXCEEDED, cloneCell.getCellStyle());
                setCell(cloneCell, format(meal.getDateTime()), style);
                setCell(sheet, nextCell(ca, 1), meal.getValue(), style);
                setCell(sheet, nextCell(ca, 2), meal.getCalories(), style);
                setCell(sheet, nextCell(ca, 3), meal.getExcess(), style);
            }
        });
        System.out.println("Convert to Excel completed successfully, result in " + outputFile.getAbsolutePath());
    }
}
