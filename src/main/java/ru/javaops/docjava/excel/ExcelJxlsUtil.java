package ru.javaops.docjava.excel;

import jakarta.xml.bind.JAXBException;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.jxls.area.Area;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.AreaListener;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import ru.javaops.docjava.schema.Meal;
import ru.javaops.docjava.schema.User;
import ru.javaops.docjava.xml.jaxb.JaxbUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ru.javaops.docjava.excel.ApachePoiUtil.getCell;

@UtilityClass
public class ExcelJxlsUtil {
    public void convert(File inputFile, File templateFile, File outputFile, Map<String, Object> params) throws JAXBException, IOException {
        List<User> users = JaxbUtil.unmarshalAndFilter(inputFile, params).getUsers().getUser();
        ExcelJxlsProcessor processor = ExcelJxlsProcessor.of(templateFile);
        processor.process(outputFile, (ctx, transformer) -> {
            ctx.putVar("users", users);
            ctx.putVar("sheetNames", users.stream().map(User::getEmail).toList());

            //  https://stackoverflow.com/a/77251671/548473
            processor.getJxlsHelper().setAreaBuilder(new XlsCommentAreaBuilder() {
                @Override
                public List<Area> build() {
                    List<Area> areas = super.build();
                    Area mealsArea = areas.get(0).getCommandDataList().get(0).getCommand()  // users EachCommand
                            .getAreaList().get(0).getCommandDataList().get(1).getCommand() // meals EachCommand
                            .getAreaList().get(0);

                    mealsArea.addAreaListener(new AreaListener() {
                        private final Workbook workbook = transformer.getWorkbook();
                        private final CellStyle mealExcess = getCell(workbook.getSheet("usersWithMeals"), new CellAddress("A14")).getCellStyle();
                        private final CellStyle mealNotExcess = getCell(workbook.getSheet("usersWithMeals"), new CellAddress("A13")).getCellStyle();

                        @Override
                        public void beforeApplyAtCell(CellRef cellRef, Context context) {
                        }

                        @Override
                        public void afterApplyAtCell(CellRef cellRef, Context context) {
                        }

                        @Override
                        public void beforeTransformCell(CellRef srcCell, CellRef targetCell, Context context) {
                        }

                        @Override
                        public void afterTransformCell(CellRef srcCell, CellRef targetCell, Context context) {
                            Meal meal = (Meal) context.getVar("meal");
                            Cell cell = ExcelJxlsProcessor.getCell(workbook, targetCell);
                            cell.setCellStyle(meal.getExcess() ? mealExcess : mealNotExcess);
                        }
                    });
                    return areas;
                }
            });
        });
        System.out.println("Convert to Excel completed successfully, result in " + outputFile.getAbsolutePath());
    }
}
