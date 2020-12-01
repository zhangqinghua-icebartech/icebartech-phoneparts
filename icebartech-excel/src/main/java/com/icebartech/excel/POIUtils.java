/*
 * Copyright (c) 2018, 吴汶泽 (wenzewoo@gmail.com).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.icebartech.excel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuwenze
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class POIUtils {

    private static final int mDefaultRowAccessWindowSize = 100;

    private static SXSSFWorkbook newSXSSFWorkbook(int rowAccessWindowSize) {
        return new SXSSFWorkbook(rowAccessWindowSize);
    }

    public static SXSSFWorkbook newSXSSFWorkbook() {
        return POIUtils.newSXSSFWorkbook(POIUtils.mDefaultRowAccessWindowSize);
    }

    public static SXSSFSheet newSXSSFSheet(SXSSFWorkbook wb, String sheetName) {
        return wb.createSheet(sheetName);
    }

    public static SXSSFRow newSXSSFRow(SXSSFSheet sheet, int index) {
        return sheet.createRow(index);
    }

    public static SXSSFCell newSXSSFCell(SXSSFRow row, int index) {
        return row.createCell(index);
    }

    public static void write(SXSSFWorkbook wb, OutputStream out) {
        try {
            if (null != out) {
                wb.write(out);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void download(
            SXSSFWorkbook wb, HttpServletResponse response, String filename) {
        try {
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition",
                               String.format("attachment; filename=%s", filename));
            POIUtils.write(wb, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object convertByExp(Object propertyValue, String converterExp)
    throws Exception {
        try {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource) {
                String[] itemArray = item.split("=");
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return propertyValue;
    }

    public static int countNullCell(String ref, String ref2) {
        // excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
        String xfd = ref.replaceAll("\\d+", "");
        String xfd_1 = ref2.replaceAll("\\d+", "");

        xfd = POIUtils.fillChar(xfd, 3, '@', true);
        xfd_1 = POIUtils.fillChar(xfd_1, 3, '@', true);

        char[] letter = xfd.toCharArray();
        char[] letter_1 = xfd_1.toCharArray();
        int res =
                (letter[0] - letter_1[0]) * 26 * 26 + (letter[1] - letter_1[1]) * 26 + (letter[2]
                                                                                        - letter_1[2]);
        return res - 1;
    }

    private static String fillChar(String str, int len, char let, boolean isPre) {
        int len_1 = str.length();
        if (len_1 < len) {
            if (isPre) {
                for (int i = 0; i < (len - len_1); i++) {
                    str = let + str;
                }
            } else {
                for (int i = 0; i < (len - len_1); i++) {
                    str = str + let;
                }
            }
        }
        return str;
    }

    public static void checkExcelFile(File file) {
        String filename = null != file ? file.getAbsolutePath() : null;
        if (null == filename || !file.exists()) {
            throw new ExcelKitRuntimeException("Excel file[" + filename + "] does not exist.");
        }
        if (!filename.endsWith(".xlsx")) {
            throw new ExcelKitRuntimeException(
                    "[" + filename + "]Only .xlsx formatted files are supported.");
        }
    }

    /**
     * 从一个流中获取Workbook
     */
    public static XSSFWorkbook getWorkbook(InputStream in) {
        try {
            return new XSSFWorkbook(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从Workbook中获取所有的Sheet
     */
    public static List<XSSFSheet> getSheets(XSSFWorkbook workbook) {
        List<XSSFSheet> sheets = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheets.add(workbook.getSheetAt(i));
        }
        return sheets;
    }

    /**
     * 获取表格集合里面的所有行（去掉头部）
     *
     * @param sheets 表格列表
     * @return 行列表
     */
    public static List<XSSFRow> getBodyRows(List<XSSFSheet> sheets) {
        List<XSSFRow> rows = new ArrayList<>();
        for (XSSFSheet sheet : sheets) {
            for (int i = getHeaderHeight(sheet); i < sheet.getPhysicalNumberOfRows(); i++) {
                rows.add(sheet.getRow(i));
            }
        }
        return rows;
    }

    public static List<XSSFRow> getBodyRows(XSSFSheet sheet) {
        List<XSSFRow> rows = new ArrayList<>();
        for (int i = getHeaderHeight(sheet); i < sheet.getPhysicalNumberOfRows(); i++) {
            rows.add(sheet.getRow(i));
        }
        return rows;
    }

    /**
     * 从行里面提取所有的列
     *
     * @param row 行
     * @return 列集合
     */
    public static List<XSSFCell> getCells(XSSFRow row) {
        List<XSSFCell> cells = new ArrayList<>();

        // 获取合并单元格
        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            row.getCell(i).setCellType(CellType.STRING);
            if (row.getCell(i).getStringCellValue().length() == 0) continue;
            cells.add(row.getCell(i));
        }
        return cells;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     *
     * @param sheet  表格
     * @param row    行下标
     * @param column 列下标
     * @return 是否是合并单元格
     */
    private static boolean isMergedRegion(XSSFSheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取工作表的头部高度
     *
     * @param sheet 工作表
     * @return 头部高度
     */
    private static Integer getHeaderHeight(XSSFSheet sheet) {
        if (isMergedRegion(sheet, 0, 0)) {
            return sheet.getMergedRegion(0).getLastRow() + 2;
        }
        return 1;
    }

    /**
     * 获取单元格的值
     *
     * @param cell 单元格
     * @return 单元格的值
     */
    public static Object getCellValue(Cell cell) {
        Object obj = null;
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case ERROR:
                obj = cell.getErrorCellValue();
                break;
            case NUMERIC:
                obj = cell.getNumericCellValue();
                break;
            case STRING:
                obj = cell.getStringCellValue();
                break;
            default:
                break;
        }
        return obj;
    }

    /**
     * 1. 获取所有的工作表
     * 2. 获取所有的单元行
     * 3. 获取所有的单元格（合并后的）
     */
    public static List<List<XSSFCell>> getRowCells(XSSFWorkbook workbook) {
        List<List<XSSFCell>> rowCells = new ArrayList<>();
        List<XSSFSheet> sheets = getSheets(workbook);
        for (XSSFSheet sheet : sheets) {
            // 此工作表的所有合并单元格
            List<Integer[]> mergedRegions = getMergedRegions(sheet);

            // 遍历出每一个物理工作行
            List<XSSFCell> cells = new ArrayList<>();
            for (XSSFRow row : getBodyRows(sheet)) {
                Iterator<Cell>  cellIterator =row.cellIterator();
                while(cellIterator.hasNext()) {
                    XSSFCell cell = (XSSFCell) cellIterator.next();
                    // 移除掉
                    if (cell== null) {
                        continue;
                    }
                    if (cell.getCellTypeEnum() == CellType.BLANK) {
                        continue;
                    }
                    cells.add(cell);
                }
            }
            for (XSSFCell cell : cells) {
                if (cell != null && cell.getColumnIndex() == 0) {
                    int rowIndex = cell.getRowIndex();
                    int cellHeight = getCellHeight(mergedRegions, cell);
                    rowCells.add(cells.stream()
                                      .filter(z -> z != null && z.getRowIndex() >= rowIndex && z.getRowIndex() <= rowIndex + cellHeight)
                                      .collect(Collectors.toList()));
                }
            }
        }
        return rowCells;
    }

    /**
     * 获取工作表里面所有的合并单位置（上下左右）
     */
    private static List<Integer[]> getMergedRegions(XSSFSheet sheet) {
        List<Integer[]> list = new ArrayList<>();

        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();

            list.add(new Integer[]{firstRow, lastRow, firstColumn, lastColumn});
        }

        return list;
    }

    private static Integer getCellHeight(List<Integer[]> mergedRegions, XSSFCell cell) {
        for (Integer[] mergedRegion : mergedRegions) {
            Integer row = cell.getRowIndex();
            Integer col = cell.getColumnIndex();

            if (row >= mergedRegion[0] && row <= mergedRegion[1]) {
                if (col >= mergedRegion[2] && col <= mergedRegion[3]) {
                    return mergedRegion[1] - mergedRegion[0];
                }
            }
        }
        return 0;
    }

    public static void setOptions(SXSSFSheet sheet, Object options, Integer col) {
        // 生成下拉框内容
        DVConstraint constraint = DVConstraint.createExplicitListConstraint((String[]) options);
        // 区域
        CellRangeAddressList regions = new CellRangeAddressList(0, 65535, col, col);
        // 绑定下拉框和作用区域
        HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);
        // 对sheet页生效
        sheet.addValidationData(data_validation);
    }

    public static Object cellValue(XSSFCell cell) {
        if (cell == null) return null;

        CellType type = cell.getCellTypeEnum();

        // 字符串类型
        if (type == CellType.STRING) {
            String cellValue = cell.getStringCellValue().trim();
            return StringUtils.isEmpty(cellValue) ? "" : cellValue;
        }

        // 布尔类型
        if (type == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        }

        // 数字类型（转成字符串的）
        if (type == CellType.NUMERIC && !HSSFDateUtil.isCellDateFormatted(cell)) {
            return new DecimalFormat("#.######").format(cell.getNumericCellValue());
        }
        // 日期类型
        if (type == CellType.NUMERIC && !HSSFDateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        }

        return null;
    }
}
