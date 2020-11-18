package com.icebartech.phoneparts.company.param;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.params.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CompanyPageParam extends PageParam {

    @ApiModelProperty(value = "代理商id", example = "代理商id")
    private Long agentId;

    @ApiModelProperty(value = "代理商id", example = "代理商id")
    private List<Long> agentIdIn;

    @ApiModelProperty(value = "是否上架", example = "是否上架")
    private ChooseType enable;

    @ApiModelProperty(value = "次级代理商id", example = "次级代理商id")
    private Long secondAgentId;

    @ApiModelProperty(value = "代理商名称", hidden = true)
    private String agentNameLike;

    @ApiModelProperty(value = "次级代理商id", hidden = true)
    private Long secondAgentIdNotEq;
}
