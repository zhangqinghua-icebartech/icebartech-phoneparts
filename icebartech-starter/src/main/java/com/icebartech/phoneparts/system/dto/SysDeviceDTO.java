package com.icebartech.phoneparts.system.dto;

import com.icebartech.phoneparts.system.po.SysDevice;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Created by liuao on 2020/9/27.
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDeviceDTO extends SysDevice {

    @ApiModelProperty(value = "代理商名称",example = "代理商名称")
    private String agentName;


}
