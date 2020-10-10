package com.icebartech.phoneparts.system.dto;

import com.icebartech.phoneparts.system.po.SysClassTwo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-06-18T11:11:08.280
 * @Description 二级分类表
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "二级分类表")
public class SysClassTwoDto extends SysClassTwo{

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "一级分类中文名称",example = "一级分类中文名称")
    private String classOneName;

}
