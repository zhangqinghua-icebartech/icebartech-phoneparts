package com.icebartech.phoneparts.user.param;

import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserPageParam extends PageParam {

    @ApiModelProperty(value = "序列号",example = "序列号（选填）")
    private String serialNumLike;

    @ApiModelProperty(value = "邮箱",example = "邮箱（选填）")
    private String emailLike;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "次级代理商id",example = "次级代理商id")
    private Long secondAgentId;

    @ApiModelProperty(value = "二级分类id",example = "二级分类id")
    private Long serialClassId;

}
