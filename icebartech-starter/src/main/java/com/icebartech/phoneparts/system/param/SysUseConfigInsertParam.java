package com.icebartech.phoneparts.system.param;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.local.UserThreadLocal;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Created by liuao on 2019/9/4.
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUseConfigInsertParam {

    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "速度",example = "速度")
    private Integer speed;

    @NotNull
    @ApiModelProperty(value = "压力",example = "压力")
    private Integer pressure;

    @NotBlank
    @ApiModelProperty(value = "名称",example = "名称")
    private String name;

    @ApiModelProperty(value = "使用状态",example = "使用状态")
    private ChooseType state = ChooseType.n;


}
