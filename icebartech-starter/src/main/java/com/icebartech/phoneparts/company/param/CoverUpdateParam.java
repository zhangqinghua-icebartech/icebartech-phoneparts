package com.icebartech.phoneparts.company.param;

import com.icebartech.core.enums.ChooseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Created by liuao on 2019/9/10.
 * @desc
 */
@Data
public class CoverUpdateParam {

    @NotNull
    private long id;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "图标",example = "图标")
    private String iconOne;

    @ApiModelProperty(value = "图标",example = "图标")
    private String iconTwo;

    @ApiModelProperty(value = "是否上架",example = "是否上架")
    private ChooseType enable;

    @ApiModelProperty(value = "代理商名称",hidden = true)
    private String agentName;

}
