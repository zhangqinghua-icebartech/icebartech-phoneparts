package com.icebartech.phoneparts.product.dto;

import com.icebartech.phoneparts.product.po.UseRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UseRecordDTO extends UseRecord {

    @ApiModelProperty(value = "产品信息",example = "产品信息")
    private ProductDto product;
}
