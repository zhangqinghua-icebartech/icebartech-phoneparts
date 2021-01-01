package com.icebartech.phoneparts.redeem.excel;

import com.icebartech.excel.annotation.ExcelField;
import com.icebartech.phoneparts.enums.RedeemStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class RedeemExports {

    @ExcelField(value = "兑换码", columnWidth = 20)
    private String code;

    @ExcelField(value = "兑换码标题")
    private String title;

    private RedeemStateEnum state;

    @ExcelField(value = "兑换码状态")
    private String stateDesc;

    @ExcelField(value = "绑定邮箱")
    private String email;

    @ExcelField(value = "激活时间")
    private Date useTime;

    @ExcelField(value = "兑换次数")
    private Integer useNum;

    @ExcelField(value = "归属代理商")
    private String agentName;

    public void setState(RedeemStateEnum state) {
        this.state = state;
        this.stateDesc = state.getDesc();
    }
}
