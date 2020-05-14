package com.icebartech.phoneparts.system.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by liuao on 2020/4/2.
 * @desc
 */
@Data
public class SysConfigUpdateParam {


    @ApiModelProperty(value = "id",example = "id")
    private Long id;

    @ApiModelProperty(value = "类型 0普通 1横屏 2竖屏",example = "类型 0普通 1横屏 2竖屏")
    private Integer type;

    @ApiModelProperty(value = "安装包",example = "安装包")
    private String file;

    @ApiModelProperty(value = "版本号",example = "版本号")
    private String version;

    @ApiModelProperty(value = "版本编号",example = "0")
    private Integer versionCode;

    @ApiModelProperty(value = "国内下载链接",example = "国内下载链接")
    private String innerDownloadUrl;

    @ApiModelProperty(value = "国外下载链接",example = "国外下载链接")
    private String outerDownloadUrl;

}
