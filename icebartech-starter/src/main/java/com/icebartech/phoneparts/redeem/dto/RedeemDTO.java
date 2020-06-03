package com.icebartech.phoneparts.redeem.dto;

import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.redeem.po.Redeem;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-08-28T15:35:58.161
 * @Description 兑换码管理
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "兑换码管理")
public class RedeemDTO extends Redeem{

    private static final long serialVersionUID = 1L;

    private Agent agent;


}
