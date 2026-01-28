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
public class ExcelPoiUtil {
    public void convert(File inputFile, File templateFile, File outputFile, Map<String, Object> params) throws JAXBException, IOException {
        List<User> users = JaxbUtil.unmarshalAndFilter(inputFile, params).getUsers().getUser();
        ExcelPoiConverter converter = ExcelPoiConverter.of(templateFile);

        converter.convert(outputFile, workbook -> {
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                workbook.cloneSheet(0);
                workbook.setSheetName(i + 1, user.getEmail());
            }
            workbook.removeSheetAt(0);
        });
        System.out.println("Convert to Excel completed successfully, result in " + outputFile.getAbsolutePath());
    }
}
