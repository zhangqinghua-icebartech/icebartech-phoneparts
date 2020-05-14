package com.icebartech.phoneparts.product.dto;

import com.icebartech.phoneparts.product.po.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-06-18T11:11:51.866
 * @Description 单品表
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "单品表")
public class ProductDto extends Product{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "一级分类名称",example = "一级分类名称")
    private String oneClassName;

    @ApiModelProperty(value = "二级分类名称",example = "二级分类名称")
    private String twoClassName;

}
