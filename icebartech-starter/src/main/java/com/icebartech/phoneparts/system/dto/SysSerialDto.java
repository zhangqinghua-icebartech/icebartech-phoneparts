package com.icebartech.phoneparts.system.dto;

import com.icebartech.excel.annotation.ExcelField;
import com.icebartech.phoneparts.system.po.SysSerial;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysSerialDto extends SysSerial {

    private static final long serialVersionUID = 1L;

    @ExcelField(value = "一级分类名称")
    @ApiModelProperty(value = "一级分类名称", example = "一级分类名称")
    private String agentClassName;

    @ExcelField(value = "二级分类名称")
    @ApiModelProperty(value = "二级分类名称", example = "二级分类名称")
    private String serialClassName;

    @ExcelField(value = "批次")
    @ApiModelProperty(value = "批次", example = "批次")
    private String batchName;
}
