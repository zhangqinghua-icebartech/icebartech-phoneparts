package com.icebartech.phoneparts.company.dto;

import com.icebartech.phoneparts.company.po.CompanyInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-06-18T11:12:57.763
 * @Description 公司简介
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "公司简介")
public class CompanyInfoDto extends CompanyInfo{

    private static final long serialVersionUID = 1L;

}
