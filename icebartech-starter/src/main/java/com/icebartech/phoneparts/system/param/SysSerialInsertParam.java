package com.icebartech.phoneparts.system.param;

import com.github.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Data
@ToString
public class SysSerialInsertParam {

    @ApiModelProperty(value = "序列号",example = "序列号")
    @NotBlank
    @ExcelField(title = "序列号", order = 1)
    private String serialNum;

    @NotNull
    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

    @NotNull
    @ApiModelProperty(value = "二级分类id",example = "二级分类id")
    private Long serialClassId;

    @ApiModelProperty(value = "来源 0 平台 1 运营商",example = "来源 0 平台 1 运营商")
    private Integer origin;

    @ApiModelProperty(value = "随机字符串",example = "随机字符串")
    private String randomStr;

    public SysSerialInsertParam(){

    }

    public SysSerialInsertParam(String serialNum,Long agentId,Long serialClassId,String randomStr){
        this.serialNum = serialNum;
        this.agentId = agentId;
        this.serialClassId = serialClassId;
        this.randomStr = randomStr;
    }

}
