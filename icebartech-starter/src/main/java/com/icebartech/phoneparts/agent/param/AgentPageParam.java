package com.icebartech.phoneparts.agent.param;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Created by liuao on 2019/9/5.
 * @desc
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AgentPageParam extends PageParam{

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty("公司名称")
    private String companyNameLike;

    @ApiModelProperty("账号")
    private String loginNameLike;

    @ApiModelProperty("分类名称")
    private String classNameLike;

    @ApiModelProperty("手机号")
    private String phoneLike;

    @ApiModelProperty(value = "是否下架",example = "是否下架")
    private ChooseType enable;

    @ApiModelProperty(value = "代理商上级id",example = "代理商上级id")
    private Long parentId;

    @ApiModelProperty(value = "递减排序", hidden = true)
    private Boolean sortDESC = true;

    @ApiModelProperty(value = "递增排序", hidden = true)
    private Boolean idASC = true;


}
