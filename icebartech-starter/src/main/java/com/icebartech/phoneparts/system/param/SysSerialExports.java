package com.icebartech.phoneparts.system.param;

import com.icebartech.excel.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysSerialExports {

    @ExcelField(value = "序列号",columnWidth = 30)
    private String serialNum;

    @ExcelField(value = "序列号状态")
    private String statusDesc;

    @ExcelField(value = "绑定邮箱",columnWidth = 30)
    private String bindMail;

    @ExcelField(value = "一级分类名称")
    private String agentClassName;

    @ExcelField(value = "二级分类名称")
    private String serialClassName;

    @ApiModelProperty(value = "状态 0未使用 1使用中 2已过期", example = "状态 0未使用 1使用中 2已过期")
    private Integer status;

    public void setStatus(Integer status) {
        this.status = status;
        if (this.status == 0) this.statusDesc = "未使用";
        if (this.status == 1) this.statusDesc = "使用中";
        if (this.status == 2) this.statusDesc = "已过期";
    }
}
