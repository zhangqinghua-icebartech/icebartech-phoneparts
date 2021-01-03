package com.icebartech.phoneparts.system.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysConfigPageParam extends PageParam {

    @ApiModelProperty(value = "代理商id", example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "类型 0 cut 1 devia 2 usams 3 byoyond cell 4 green mnky 5. 竖屏 6.横屏", example = "0")
    private Integer type;

    @ApiModelProperty(value = "版本号", example = "版本号")
    private String versionLike;

    @ApiModelProperty(value = "安装包名称", example = "安装包名称")
    private String fileNameLike;
}
