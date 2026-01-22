package ru.javaops.docjava.pdf;

import org.junit.jupiter.api.Test;

import javax.xml.transform.TransformerException;
import java.io.File;

import static ru.javaops.docjava.TestData.inputFile;

class PdfFopUtilTest {

    @Test
    void convert() throws TransformerException {
        PdfFopUtil.convert(inputFile, new File("in/fop.xsl"), new File("out/fop.xml"));
    }
}