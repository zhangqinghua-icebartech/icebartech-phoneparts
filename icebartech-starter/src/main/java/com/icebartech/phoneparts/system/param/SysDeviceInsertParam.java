package com.icebartech.phoneparts.system.param;

import com.icebartech.core.enums.ChooseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by liuao on 2020/9/27.
 * @desc
 */
@Data
public class SysDeviceInsertParam {

    @ApiModelProperty(value = "设备类型 0-手机版 1-竖屏版 2-横屏版", example = "0")
    private Integer type;

    @ApiModelProperty(value = "设备名称", example = "设备名称")
    private String name;

    @ApiModelProperty(value = "代理商id", example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "详情图", example = "详情图")
    private String detailIcon;

    @ApiModelProperty(value = "文件", example = "文件")
    private String file;

    @ApiModelProperty(value = "文件名称", example = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "使用状态", example = "使用状态")
    private ChooseType state;

}
