package com.icebartech.phoneparts.system.param;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Created by liuao on 2019/9/4.
 * @desc
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUseConfigPageParam extends PageParam {

    @ApiModelProperty(value = "名称",example = "名称")
    private String nameLike;

    @ApiModelProperty(value = "使用状态",example = "使用状态")
    private ChooseType state;

    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId = UserThreadLocal.getUserId();

}
