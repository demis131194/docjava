package ru.javaops.docjava.excel;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;
import ru.javaops.docjava.TestData;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static ru.javaops.docjava.TestData.inputFile;

public class ExcelPoiUtilTest {
    private static final File templateFile = new File("in/poiTemplate.xlsx");

    @Test
    public void convert() throws IOException, JAXBException {
        ExcelPoiUtil.convert(inputFile, templateFile, new File("out/poi.xlsx"), Map.of());
    }

    @Test
    public void convertFiltered() throws IOException, JAXBException {
        ExcelPoiUtil.convert(inputFile, templateFile, new File("out/poiFiltered.xlsx"), TestData.paramsMap);
    }
}