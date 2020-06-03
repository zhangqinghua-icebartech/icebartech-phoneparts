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
@Table(appliesTo = "redeem", comment = "兑换码管理")
public class Redeem extends BasePo {

    @Column(columnDefinition = "varchar(225) NOT NULL DEFAULT '' COMMENT '兑换码标题'")
    @ApiModelProperty(value = "兑换码标题",example = "兑换码标题")
    private String title;

    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '兑换码数量 '")
    @ApiModelProperty(value = "兑换码数量",example = "兑换码数量")
    private String redeemNum;

    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '兑换码数量 '")
    @ApiModelProperty(value = "兑换码次数",example = "兑换码次数")
    private String useNum;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '代理商id'")
    private Long agentId;


}
