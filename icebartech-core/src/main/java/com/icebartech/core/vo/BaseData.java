package com.icebartech.core.vo;

import com.icebartech.core.enums.CommonResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 返回的数据封装对象
 *
 * @author wenhsh
 */
@Data
@ApiModel(value = "BaseData", description = "返回的数据封装对象")
public class BaseData<T> {

    /**
     * 业务状态码
     */
    @ApiModelProperty(value = "业务状态码")
    private int resultCode = CommonResultCodeEnum.SUCCESS.getCode();

    /**
     * 错误说明
     */
    @ApiModelProperty(value = "错误说明")
    private String errMsg;

    /**
     * 业务数据
     */
    @ApiModelProperty(value = "业务数据")
    private T bussData;

    public BaseData() {
    }

    public BaseData(int resultCode, String errMsg) {
        this.resultCode = resultCode;
        this.errMsg = errMsg;
    }

    public BaseData(CommonResultCodeEnum resultCode) {
        this.resultCode = resultCode.getCode();
        this.errMsg = resultCode.getDesc();
    }

    public BaseData(CommonResultCodeEnum resultCode, String errMsg) {
        this.resultCode = resultCode.getCode();
        this.errMsg = errMsg;
    }

    public BaseData(T bussData) {
        this.bussData = bussData;
    }
}
