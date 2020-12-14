package com.icebartech.phoneparts.user.param;

import com.icebartech.excel.annotation.ExcelField;
import lombok.Data;

@Data
public class UserExports {

    @ExcelField(value = "序列号", columnWidth = 20)
    private String serialNum;

    @ExcelField(value = "邮箱", columnWidth = 20)
    private String email;

    @ExcelField(value = "一级分类")
    private String agentClassName;

    @ExcelField(value = "总切割次数")
    private Integer useCount;

    @ExcelField(value = "剩余切割次数")
    private Integer mayUseCount;
}
