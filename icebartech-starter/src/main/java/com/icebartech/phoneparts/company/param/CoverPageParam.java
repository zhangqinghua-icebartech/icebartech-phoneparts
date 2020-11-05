package com.icebartech.phoneparts.company.param;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Created by liuao on 2019/9/10.
 * @desc
 */
@Data
public class CoverPageParam extends PageParam {

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "是否上架",example = "是否上架")
    private ChooseType enable;

    @ApiModelProperty(value = "次级代理商id",example = "次级代理商id")
    private Long secondAgentId;

    @ApiModelProperty(value = "代理商名称",hidden = true)
    private String agentNameLike;

    @ApiModelProperty(value = "次级代理商id",hidden = true)
    private Long secondAgentIdNotEq;


}
