package ru.javaops.docjava.excel;

import java.io.File;
import java.io.IOException;

public class ExcelJxlsProcessor {
    private final File templateFile;

    public static ExcelJxlsProcessor of(File templateFile) throws IOException {
        return new ExcelJxlsProcessor(templateFile);
    }

    private ExcelJxlsProcessor(File templateFile) {
        this.templateFile = templateFile;
    }

    public void process(File outputFile) {
    }
}
