package com.icebartech.phoneparts.product.dto;

import com.github.annotation.ExcelField;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class UseRecordExcel {

    private LocalDateTime gmtCreated;

    @ExcelField(title = "切割时间")
    private String gmtCreatedStr;

    @ExcelField(title = "序列号")
    private String serialNum;

    @ExcelField(title = "账号")
    private String email;

    @ExcelField(title = "一级分类")
    private String classOne;

    @ExcelField(title = "二级分类")
    private String classTwo;

    @ExcelField(title = "三级分类")
    private String classThree;

    @ExcelField(title = "产品名称")
    private String product;

    public void mapTime() {
        if (gmtCreated != null) {
            this.gmtCreatedStr = gmtCreated.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }
}
