package com.icebartech.phoneparts.product.dto;

import com.icebartech.excel.annotation.ExcelField;
import lombok.Data;

@Data
public class UseRecordDetailExcel {

    @ExcelField(value = "切割时间", columnWidth = 25)
    private String gmtCreated;

    @ExcelField(value = "序列号", columnWidth = 30)
    private String serialNum;

    @ExcelField(value = "账号", columnWidth = 30)
    private String email;

    @ExcelField(value = "一级分类")
    private String classOne;

    @ExcelField(value = "二级分类")
    private String classTwo;

    @ExcelField(value = "三级分类")
    private String classThree;

    @ExcelField(value = "产品名称")
    private String product;
}
