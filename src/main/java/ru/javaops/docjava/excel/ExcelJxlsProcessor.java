package ru.javaops.docjava.excel;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import java.io.*;
import java.util.function.Consumer;

public class ExcelJxlsProcessor {
    private final File templateFile;
    private final JxlsHelper jxlsHelper = JxlsHelper.getInstance();

    public static ExcelJxlsProcessor of(File templateFile) throws IOException {
        return new ExcelJxlsProcessor(templateFile);
    }

    private ExcelJxlsProcessor(File templateFile) {
        this.templateFile = templateFile;
    }

    public void process(File outputFile, Consumer<Context> contextConsumer) throws IOException {
        try (InputStream is = new FileInputStream(templateFile); OutputStream os = new FileOutputStream(outputFile)) {
            Context context = new Context();
            contextConsumer.accept(context);
            jxlsHelper.processTemplate(is, os, context);
        }
    }
}
