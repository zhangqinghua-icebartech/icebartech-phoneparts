package com.icebartech.phoneparts.system.dto;

import com.icebartech.phoneparts.system.po.SysConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Created by liuao on 2020/4/2.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysConfigDTO extends SysConfig {

    @ApiModelProperty(value = "安装包下载地址",example = "安装包下载地址")
    private String fileUrl;


}
