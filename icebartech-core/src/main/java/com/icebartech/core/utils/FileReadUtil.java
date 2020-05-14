package com.icebartech.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件内容解析工具类
 *
 * @author Anler
 */
@Slf4j
public class FileReadUtil {

    /**
     * 获取带单位的文件名，单位会自动显示为合适的值，如B、KB、MB等
     *
     * @param size 文件字节大小
     */
    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0B";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + units[digitGroups];
    }

    public static void main(String[] args) {
        System.out.println(readableFileSize(10234457));
    }

    /**
     * 读取文本文件
     *
     * @param file
     * @return 每一行信息
     */
    public static List<String> readTxtOrCvs(File file) {
        List<String> dataList = new ArrayList<String>();
        LineIterator li = null;
        try {
            li = FileUtils.lineIterator(file);
            while (li.hasNext()) {
                dataList.add(li.nextLine());
            }
            LineIterator.closeQuietly(li);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (li != null) {
                try {
                    LineIterator.closeQuietly(li);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        return dataList;
    }

    /**
     * 读取excel表格文件
     *
     * @param file
     * @return Map<String ,   List < List < String>>>  页<页名, 行<单元格<值>>>
     */
    public static Map<String, List<List<String>>> readXls(File file) {
        Map<String, List<List<String>>> sheetData = new HashMap<>();
        List<List<String>> rowData;
        List<String> cellData;
        try {
            String filePath = file.getName();
            String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
            if (fileType.equals("xls") || fileType.equals("xlsx")) {
                InputStream stream = new FileInputStream(file);
                Workbook wb = WorkbookFactory.create(stream);
                // 获取sheet页数
                int sheetNum = wb.getNumberOfSheets();
                for (int i = 0; i < sheetNum; i++) {
                    // 获取每一页sheet
                    Sheet sheet = wb.getSheetAt(i);
                    // 获取当前sheet的名称
                    String sheetName = wb.getSheetName(i);
                    rowData = new ArrayList<>();
                    // 获取sheet下的每一行row
                    for (int j = 0; j < sheet.getLastRowNum() + 1; j++) {
                        cellData = new ArrayList<>();
                        Row row = sheet.getRow(j);
                        if (null != row) {
                            // 获取row下的每一个单元格cell
                            boolean allCellIsNull = true;
                            for (int k = 0; k < sheet.getRow(0).getLastCellNum(); k++) {
                                // 保存row下的每一个单元格cell
                                Cell cell = row.getCell(k);
                                if (null == cell) {
                                    cellData.add(null);
                                    continue;
                                }
                                // 将单元格内容格式设为String
                                cell.setCellType(CellType.STRING);
                                String cellValue = cell.getStringCellValue();
                                if (StringUtils.isBlank(cellValue)) {
                                    cellData.add(null);
                                    continue;
                                }
                                // 本行有单元格有内容
                                allCellIsNull = false;
                                cellData.add(cellValue);
                            }
                            // 空行
                            if (allCellIsNull) {
                                cellData = new ArrayList<>();
                            }
                        }
                        rowData.add(cellData);
                    }
                    // 保存sheet下的每一行row
                    sheetData.put(sheetName, rowData);
                }
            } else {
                log.error("excel格式不正确");
            }
        } catch (Exception e) {
            log.error("读取文件内容出错");
            e.printStackTrace();
        }
        return sheetData;
    }

}
