package ru.javaops.docjava.excel;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;

import java.util.EnumMap;

@UtilityClass
public class ApachePoiUtil {

    public static class CellStyleFactory<K extends Enum<K>> {
        private final Workbook workbook;
        private final EnumMap<K, CellStyle> cellStyleMap;

        public static <K extends Enum<K>> CellStyleFactory<K> of(Workbook workbook, Class<K> keyType) {
            return new CellStyleFactory<>(workbook, keyType);
        }

        private CellStyleFactory(Workbook workbook, Class<K> keyType) {
            this.workbook = workbook;
            cellStyleMap = new EnumMap<>(keyType);
        }

        public CellStyle createColorCellStyle(K key, CellStyle cloneStyle, IndexedColors color) {
            return cellStyleMap.computeIfAbsent(key, k -> {
                Font font = workbook.getFontAt(cloneStyle.getFontIndex());
                Font newFont = workbook.createFont();
                newFont.setFontName(font.getFontName());
                newFont.setFontHeightInPoints(font.getFontHeightInPoints());
                newFont.setFontHeight(font.getFontHeight());
                newFont.setBold(font.getBold());
                newFont.setItalic(font.getItalic());
                newFont.setUnderline(font.getUnderline());
                newFont.setStrikeout(font.getStrikeout());
                newFont.setColor(color.getIndex());
                CellStyle newCellStyle = workbook.createCellStyle();
                newCellStyle.cloneStyleFrom(cloneStyle);
                newCellStyle.setFont(newFont);
                return newCellStyle;
            });
        }
    }

    public static Cell getCell(Sheet sheet, CellAddress cellAddress) {
        return sheet.getRow(cellAddress.getRow()).getCell(cellAddress.getColumn());
    }

    public static void setCell(Sheet sheet, CellAddress cellAddress, Object value, CellStyle style) {
        setCell(getCell(sheet, cellAddress), value, style);
    }

    public static void setCell(Cell cell, Object value, CellStyle style) {
        cell.setCellValue(value.toString());
        if (style != null) {
            cell.setCellStyle(style);
        }
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