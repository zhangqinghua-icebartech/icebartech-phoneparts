package com.icebartech.phoneparts.system.param;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Created by liuao on 2020/9/27.
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysDevicePageParam extends PageParam {

    @ApiModelProperty(value = "设备名称",example = "设备名称")
    private String nameLike;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "文件名称",example = "文件名称")
    private String fileNameLike;

    @ApiModelProperty(value = "使用状态",example = "使用状态")
    private ChooseType state;

    @ApiModelProperty(value = "更新时间倒序",example = "更新时间正序")
    private Boolean gmtModifiedDESC = false;

    @ApiModelProperty(value = "更新时间倒序",example = "更新时间正序")
    private Boolean gmtModifiedASC = false;

    @ApiModelProperty(value = "用户id",example = "用户id")
    private Long userId;

}
