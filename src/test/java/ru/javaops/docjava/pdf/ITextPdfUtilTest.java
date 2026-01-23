package ru.javaops.docjava.pdf;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;
import ru.javaops.docjava.TestData;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static ru.javaops.docjava.TestData.inputFile;

public class ITextPdfUtilTest {

    @Test
    void convert() throws JAXBException, IOException {
        ITextPdfUtil.convert(inputFile, new File("out/iText.pdf"), Map.of());
    }

    @Test
    void convertFiltered() throws JAXBException, IOException {
        ITextPdfUtil.convert(inputFile, new File("out/iTextFiltered.pdf"), TestData.paramsMap);
    }
}