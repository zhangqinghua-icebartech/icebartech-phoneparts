package com.icebartech.phoneparts.redeem.po;

import com.icebartech.core.po.BasePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Created by liuao on 2019/8/28.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "redeem_record", comment = "兑换记录")
public class RedeemRecord extends BasePo {

    @ApiModelProperty(value = "用户id",example = "用户id")
    @Column(columnDefinition = "bigint(20) DEFAULT NULL COMMENT '用户id'")
    private Long userId;

    @ApiModelProperty(value = "兑换码id",example = "兑换码id")
    @Column(columnDefinition = "bigint(20) DEFAULT NULL COMMENT '兑换码id'")
    private Long RedeemCodeId;

    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '兑换码数量 '")
    @ApiModelProperty(value = "兑换码次数",example = "兑换码次数")
    private String useNum;

}
