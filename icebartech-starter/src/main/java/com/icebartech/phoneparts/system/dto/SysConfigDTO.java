package com.icebartech.phoneparts.system.dto;

import com.icebartech.phoneparts.system.po.SysConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysConfigDTO extends SysConfig {

    @ApiModelProperty(value = "安装包下载地址", example = "安装包下载地址")
    private String fileUrl;

    @ApiModelProperty(value = "代理商名称", example = "代理商名称")
    private String agentName;
}
