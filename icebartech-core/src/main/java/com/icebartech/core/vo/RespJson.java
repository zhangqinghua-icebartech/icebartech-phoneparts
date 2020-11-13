package com.icebartech.core.vo;

import com.alibaba.fastjson.JSON;
import com.icebartech.core.constants.IcebartechConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回的json数据对象
 *
 * @author haosheng.wenhs
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "RespJson", description = "返回的json数据对象")
public class RespJson<T extends BaseData> {

    @ApiModelProperty(value = "系统返回码，200：成功；500：失败；401：未登录", example = "200")
    private int code = IcebartechConstants.RESULT_OK;

    @ApiModelProperty(value = "系统状态", example = "success/fail")
    private String status = IcebartechConstants.RESULT_STATUS_SUCCESS;

    @ApiModelProperty(value = "业务返回数据")
    private T data;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
