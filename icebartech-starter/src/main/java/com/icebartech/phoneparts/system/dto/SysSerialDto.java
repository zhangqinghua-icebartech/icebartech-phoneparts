package com.icebartech.phoneparts.system.dto;

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

    @ApiModelProperty(value = "一级分类名称", example = "一级分类名称")
    private String agentClassName;

    @ApiModelProperty(value = "二级分类名称", example = "二级分类名称")
    private String serialClassName;

    @ApiModelProperty(value = "批次", example = "批次")
    private String batchName;
}
