package com.icebartech.phoneparts.system.dto;

import com.github.annotation.ExcelField;
import com.icebartech.phoneparts.system.po.SysSerial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-06-18T11:09:42.138
 * @Description 序列号表
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysSerialDto extends SysSerial {

    private static final long serialVersionUID = 1L;

    @ExcelField(title = "一级分类名称", order = 2)
    @ApiModelProperty(value = "一级分类名称", example = "一级分类名称")
    private String agentClassName;

    @ExcelField(title = "二级分类名称", order = 2)
    @ApiModelProperty(value = "二级分类名称", example = "二级分类名称")
    private String serialClassName;

    @ExcelField(title = "批次", order = 2)
    @ApiModelProperty(value = "批次", example = "批次")
    private String batchName;
}
