package ru.javaops.docjava.excel;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static ru.javaops.docjava.TestData.inputFile;

public class ExcelJxlsUtilTest {
    private static final File templateFile = new File("in/jxlsTemplate.xlsx");

    @Test
    public void convert() throws IOException, JAXBException {
        ExcelJxlsUtil.convert(inputFile, templateFile, new File("out/jxls.xlsx"), Map.of());
    }
}