package com.icebartech.phoneparts.product.dto;

import com.icebartech.excel.annotation.ExcelField;
import lombok.Data;

@Data
public class UseRecordExcel {

    @ExcelField(value = "序列号", columnWidth = 30)
    private String serialNum;

    @ExcelField(value = "已绑定邮箱", columnWidth = 30)
    private String email;

    @ExcelField(value = "已使用次数")
    private String useCount;
}
