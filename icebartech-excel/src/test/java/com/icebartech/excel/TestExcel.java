package com.icebartech.excel;

import com.icebartech.excel.dto.OrderImportsExcel;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class TestExcel {

    public static void test1() {
        List<OrderImportsExcel> excels = ExcelUtils.imports(new File("/Users/zhangqinghua/Desktop/订单导入模版.xlsx"), OrderImportsExcel.class);

        System.out.println(excels);
        for (OrderImportsExcel excel : excels) {
            System.out.println(excel.getOrderChannel().getCargoIds().get(1).toString());
        }

    }

    public static void test2() {
        String[] list = {"JAVA", "C", "易语言", "GO"};

        // 文件初始化
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");
        // 在第一行第一个单元格，插入下拉框
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        // 普通写入操作
        cell.setCellValue("请选择");// 这是实验
        // 生成下拉列表
        // 对（row0,rowMax,col0,colMax）所有单元格有效
        int maxRowNum = SpreadsheetVersion.EXCEL97.getLastRowIndex();//maxRowNum=65535
        System.out.println("maxRowNum:" + maxRowNum);
        int maxColNum = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
        System.out.println("maxColNum:" + maxColNum);//maxColNum=255
        CellRangeAddressList regions = new CellRangeAddressList(0, maxRowNum, 0, maxColNum);
        // 生成下拉框内容
        DVConstraint constraint = DVConstraint
                .createExplicitListConstraint(list);
        // 绑定下拉框和作用区域
        HSSFDataValidation data_validation = new HSSFDataValidation(regions,
                                                                    constraint);
        // 对sheet页生效
        sheet.addValidationData(data_validation);
        // 写入文件
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream("/Users/zhangqinghua/Desktop/workbook.xls");
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 结束
        System.out.println("Over");
    }


    public static void main(String[] args) {
        test2();

        // ExcelUtils.exports(Order.createExample(), "/Users/zhangqinghua/Desktop", "订单导入模版");

//        export("订单导入模版", OrderExcel.class, "/Users/zhangqinghua/Desktop");

//        List<Order2> orders = imports(new File("/Users/zhangqinghua/Desktop/测试.xlsx"), Order2.class);
//        System.out.println(orders);
    }

//    public static void main(String[] args) {
//
//        Order2 order = new Order2();
//        Property header = CellMappingFactory.mapHeader(Order2.class);
//
//        setValue(order, header, 0, 0, 100101L);
//        setValue(order, header, 0, 1, "订单二");
//        setValue(order, header, 0, 2, "平板");
//        setValue(order, header, 1, 2, "手机");
//        setValue(order, header, 0, 3, 20);
//        setValue(order, header, 4, 3, 20);
//        setValue(order, header, 0, 4, 500);
//
//        ExcelUtils.export("测试123", new ArrayList<>(Collections.singleton(order)), "/Users/zhangqinghua/Desktop");
//    }
}
