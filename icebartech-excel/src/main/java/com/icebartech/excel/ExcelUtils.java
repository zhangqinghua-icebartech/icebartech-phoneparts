package com.icebartech.excel;

import com.icebartech.core.exception.ServiceException;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;

public class ExcelUtils {

    /**
     * 导出模版（自带头部）
     */
    public static void exports(Class<?> cls, String path, String fileName) {
        List<Property> rows = CellMappingFactory.mapRows(cls);

        SXSSFWorkbook workbook = generateWorkbook(fileName, rows);

        try {
            POIUtils.write(workbook, new FileOutputStream(path + "/" + fileName + ".xlsx"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出模版（头部 + 示例数据）
     */
    public static void exports(List data, String path, String fileName) {
        List<Property> rows = CellMappingFactory.mapRows(data);

        SXSSFWorkbook workbook = generateWorkbook(fileName, rows);

        try {
            POIUtils.write(workbook, new FileOutputStream(path + "/" + fileName + ".xlsx"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exports(Class<?> cls, HttpServletResponse response, String fileName) {
        List<Property> rows = CellMappingFactory.mapRows(cls);

        SXSSFWorkbook workbook = generateWorkbook(fileName, rows);

        try {
            POIUtils.download(workbook, response, URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exports(List<?> data, HttpServletResponse response, String fileName) {
        if (CollectionUtils.isEmpty(data)) {
            throw new ServiceException("数据为空，无法导出！！！");
        }
        List<Property> rows = CellMappingFactory.mapRows(data);

        SXSSFWorkbook workbook = generateWorkbook(fileName, rows);

        try {
            POIUtils.download(workbook, response, URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入
     */
    public static <T> List<T> imports(File excelFile, Class<T> clz) {
        try {
            // 1. 获取文件流
            InputStream inputStream = new FileInputStream(excelFile);

            // 2. 生成Workbook
            XSSFWorkbook workbook = POIUtils.getWorkbook(inputStream);

            // 3. 后续操作
            return imports(workbook, clz);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导入
     */
    public static <T> List<T> imports(InputStream inputStream, Class<T> clz) {
        try {
            // 2. 生成Workbook
            XSSFWorkbook workbook = POIUtils.getWorkbook(inputStream);

            // 3. 后续操作
            return imports(workbook, clz);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("导入数据异常：" + e.getMessage(), e);
        }
    }

    private static <T> List<T> imports(XSSFWorkbook workbook, Class<T> clz) {
        // 2. 获取此工作表的结构
        Property header = CellMappingFactory.mapHeader(clz);

        // 1. 获取所有有效的单元格（嵌套结构，第一层代表行，第二层代表单元格）
        List<List<XSSFCell>> rowCells = POIUtils.getRowCells(workbook);

        // 3. 转换过程
        List<T> datas = new ArrayList<>();
        for (List<XSSFCell> cells : rowCells) {
            if (cells.size() == 0) continue;
            datas.add(PropertyUtil.mapping(cells, header, clz));
        }

        return datas;
    }

    private static SXSSFWorkbook generateWorkbook(String excelName, List<Property> rows) {
        // 非法字符 * /
        excelName = excelName.replace("*", "").replace("/", "");

        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        CellStyle headerStyle = headerStyle(workbook);
        CellStyle contentStyle = contentStyle(workbook);

        SXSSFSheet sheet = POIUtils.newSXSSFSheet(workbook, excelName);

        // 设置单元格头部
        Property header = rows.remove(0);
        generateRow(true, sheet, header, headerStyle);

        // 设置单元格内容
        for (Property row : rows) {
            generateRow(false, sheet, row, contentStyle);
        }

        return workbook;
    }

    private static void generateRow(Boolean isHeader, SXSSFSheet sheet, Property property, CellStyle cellStyle) {
        // 头部样式
        // 第一行用做表单头部
        Map<Integer, SXSSFRow> rows = new HashMap<>();
        // 一维展开
        for (Property cellProperty : property.findFlatProperties()) {
            SXSSFRow header = rows.computeIfAbsent(cellProperty.getRow(), k -> POIUtils.newSXSSFRow(sheet, cellProperty.getRow()));
            header.setHeight((short) (2 * 256));

            SXSSFCell cell = POIUtils.newSXSSFCell(header, cellProperty.getCol());
            cell.setCellStyle(cellStyle);

            if (cellProperty.getValue() != null) {
                switch (cellProperty.getType()) {
                    case DATE:
                        cell.setCellValue((Date) cellProperty.getValue());
                        break;
                    case STRING:
                        cell.setCellValue((String) cellProperty.getValue());
                        break;
                    case BOOLEAN:
                        cell.setCellValue((Boolean) cellProperty.getValue());
                        break;
                    case DOUBLE:
                        cell.setCellValue(Double.parseDouble(cellProperty.getValue() + ""));
                        break;
                    case RICHTEXT:
                        cell.setCellValue(new HSSFRichTextString((String) cellProperty.getValue()));
                        break;
                    case OPTIONS:
                        POIUtils.setOptions(sheet, cellProperty.getValue(), cellProperty.getCol());
                        break;
                }
            }

            // 只有头部栏才需要设置宽度（父级的宽度由子级决定）
            if (isHeader && CollectionUtils.isEmpty(cellProperty.getChildren())) {
                Integer colLength = cellProperty.getColumnWidth();
                if (colLength == null || colLength == 0)
                    colLength = (String.valueOf(cellProperty.getValue()).getBytes().length + 10);
                colLength *= 256;
                sheet.setColumnWidth(cellProperty.getCol(), colLength);
            }

            // 合并单元格
            if (cellProperty.getWidth() > 1 || cellProperty.getHeight() > 1) {
                // 创建出合并区域（起始行，结束行，起始列，结束列。从0开始）
                CellRangeAddress cra = new CellRangeAddress(cellProperty.getRow(), cellProperty.getRow() + cellProperty.getHeight() - 1,
                                                            cellProperty.getCol(), cellProperty.getCol() + cellProperty.getWidth() - 1);

                // 需要给单元格设置样式
                RegionUtil.setBorderBottom(1, cra, sheet); // 下边框
                RegionUtil.setBorderLeft(1, cra, sheet); // 左边框
                RegionUtil.setBorderRight(1, cra, sheet); // 有边框
                RegionUtil.setBorderTop(1, cra, sheet); // 上边框
                sheet.addMergedRegion(cra);
            }
        }
    }

    /**
     * 创建表头样式
     */
    private static CellStyle headerStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直对齐
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());//背景颜色

        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
        cellStyle.setBorderRight(BorderStyle.THIN); //右边框
        cellStyle.setBorderTop(BorderStyle.THIN); //上边框

        Font headerFont1 = wb.createFont(); // 创建字体样式
        headerFont1.setBold(true); //字体加粗
        headerFont1.setFontName("黑体"); // 设置字体类型
        headerFont1.setFontHeightInPoints((short) 13); // 设置字体大小
        cellStyle.setFont(headerFont1); // 为标题样式设置字体样式

        return cellStyle;
    }

    /**
     * 创建内容样式
     */
    private static CellStyle contentStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);// 水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        // cellStyle.setWrapText(true);// 设置自动换行
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN); //左边框
        cellStyle.setBorderRight(BorderStyle.THIN); //右边框
        cellStyle.setBorderTop(BorderStyle.THIN); //上边框

        // 生成12号字体
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setColor((short) 8);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);
        return cellStyle;
    }
}
