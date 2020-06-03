package com.icebartech.phoneparts.redeem.param;

import com.icebartech.phoneparts.enums.RedeemStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Created by liuao on 2019/8/28.
 * @desc
 */
@Data
public class RedeemCodeInsertParam {

    @ApiModelProperty(value = "兑换码标题",example = "兑换码标题")
    private String title;

    @ApiModelProperty(value = "兑换码",example = "兑换码")
    private String code;

    @ApiModelProperty(value = "兑换码次数",example = "兑换码次数")
    private Integer useNum;

    @ApiModelProperty(value = "兑换状态",example = "兑换状态")
    private RedeemStateEnum state = RedeemStateEnum.N;

    @ApiModelProperty(value = "用户id",example = "用户id")
    private Long userId;

    @ApiModelProperty(value = "兑换管理id",example = "兑换管理id")
    private Long redeemId;

    public RedeemCodeInsertParam(){}


    public RedeemCodeInsertParam(Long redeemId,String title,String code,Integer useNum){
        this.redeemId=redeemId;
        this.title=title;
        this.code=code;
        this.useNum=useNum;
    }


}
