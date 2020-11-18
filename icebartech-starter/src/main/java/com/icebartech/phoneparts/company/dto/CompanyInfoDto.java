package com.icebartech.phoneparts.company.dto;

import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.company.po.CompanyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "公司简介")
public class CompanyInfoDto extends CompanyInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "代理商数据")
    private Agent agent;

}
