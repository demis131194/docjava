package ru.javaops.docjava.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelPoiConverter {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(\\w+)}");
    private static final Pattern PROCESSOR_PATTERN = Pattern.compile("\\\\\\{(\\w+)}");
    private final File templateFile;
    private final List<Placeholder> placeholders = new ArrayList<>();
    private final List<Processor> processors = new ArrayList<>();

    public record Placeholder(Matcher matcher, CellAddress cellAddress) {
    }

    public record Processor(String key, CellAddress cellAddress) {
    }

    public static ExcelPoiConverter of(File templateFile) throws IOException {
        return new ExcelPoiConverter(templateFile);
    }

    private ExcelPoiConverter(File templateFile) throws IOException {
        this.templateFile = templateFile;
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream((templateFile)))) {
            // find all placeholders on sheet 0
            workbook.getSheetAt(0).forEach(row ->
                    row.forEach(cell -> {
                        String value = cell.getStringCellValue();
                        Matcher matcher = PLACEHOLDER_PATTERN.matcher(value);
                        if (matcher.find()) {
                            placeholders.add(new Placeholder(matcher, cell.getAddress()));
                        } else {
                            matcher = PROCESSOR_PATTERN.matcher(value);
                            if (matcher.find()) {
                                processors.add(new Processor(matcher.group(1), cell.getAddress()));
                            }
                        }
                    }));
        }
    }

    public void convert(File outputFile, Consumer<Workbook> workbookComposer) throws IOException {
        Files.copy(templateFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        // https://stackoverflow.com/a/54695626/548473
        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(outputFile))) {
            workbookComposer.accept(workbook);
            try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(outputFile.toPath()))) {
                workbook.write(outputStream);
            }
        }
    }

    public void resolvePlaceholders(Sheet sheet, Map<String, Supplier<Object>> supplierMap) {
        placeholders.forEach(p -> {
            p.matcher.reset();
            // https://stackoverflow.com/questions/38376584/548473
            StringBuilder sb = new StringBuilder();
            while (p.matcher.find()) {
                String key = p.matcher.group(1);
                Object obj = supplierMap.get(key).get();
                p.matcher.appendReplacement(sb, obj.toString());
            }
            p.matcher.appendTail(sb);
            setCell(sheet, p.cellAddress, sb.toString());
        });
    }

    public void doProcessors(Map<String, Consumer<CellAddress>> consumerMap) {
        processors.forEach(p -> consumerMap.get(p.key).accept(p.cellAddress));
    }

    public static Cell getCell(Sheet sheet, CellAddress cellAddress) {
        return sheet.getRow(cellAddress.getRow()).getCell(cellAddress.getColumn());
    }

    public static void setCell(Sheet sheet, CellAddress cellAddress, Object value) {
        getCell(sheet, cellAddress).setCellValue(value.toString());
    }

    public static CellAddress nextRow(CellAddress ca, int shift) {
        return new CellAddress(ca.getRow() + shift, ca.getColumn());
    }

    public static CellAddress nextCell(CellAddress ca, int shift) {
        return new CellAddress(ca.getRow(), ca.getColumn() + shift);
    }

    //  https://stackoverflow.com/a/49153445/548473
    public static void insertRow(Sheet sheet, int rowNum) {
        Row templateRow = sheet.getRow(rowNum);
        if (sheet.getLastRowNum() > rowNum) {
            sheet.shiftRows(rowNum + 1, sheet.getLastRowNum(), 1);
        }
        Row newRow = sheet.createRow(rowNum + 1);
        templateRow.cellIterator().forEachRemaining(
                cell -> newRow.createCell(cell.getColumnIndex()).setCellStyle(cell.getCellStyle())
        );
    }

    //    https://www.tabnine.com/code/java/methods/org.apache.poi.hssf.usermodel.HSSFSheet/removeRow
    public static void removeRow(Sheet sheet, int rowNum) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowNum >= 0 && rowNum < lastRowNum) {
            sheet.shiftRows(rowNum + 1, lastRowNum, -1);
        } else if (rowNum == lastRowNum) {
            sheet.removeRow(sheet.getRow(rowNum));
        }
    }
}
