package ru.javaops.docjava.excel;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;

import java.io.*;
import java.util.function.BiConsumer;

import static org.jxls.transform.poi.PoiTransformer.createTransformer;

public class ExcelJxlsProcessor {
    private final File templateFile;
    @Getter
    private final JxlsHelper jxlsHelper = JxlsHelper.getInstance();

    public static ExcelJxlsProcessor of(File templateFile) throws IOException {
        return new ExcelJxlsProcessor(templateFile);
    }

    private ExcelJxlsProcessor(File templateFile) {
        this.templateFile = templateFile;
    }

    public void process(File outputFile, BiConsumer<Context, PoiTransformer> contextConsumer) throws IOException {
        try (InputStream is = new FileInputStream(templateFile); OutputStream os = new FileOutputStream(outputFile)) {
            PoiTransformer transformer = createTransformer(is, os);
            Context context = new Context();
            contextConsumer.accept(context, transformer);
            jxlsHelper.processTemplate(context, transformer);
        }
    }

    public static Cell getCell(Workbook workbook, CellRef cellRef) {
        Sheet sheet = workbook.getSheet(cellRef.getSheetName());
        return sheet.getRow(cellRef.getRow()).getCell(cellRef.getCol());
    }
}
