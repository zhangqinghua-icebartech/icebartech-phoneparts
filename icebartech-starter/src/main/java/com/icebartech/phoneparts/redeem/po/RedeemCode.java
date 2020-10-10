package com.icebartech.phoneparts.redeem.po;

import com.github.annotation.ExcelField;
import com.icebartech.core.po.BasePo;
import com.icebartech.phoneparts.enums.RedeemStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

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
@Table(appliesTo = "redeem_code", comment = "兑换码")
public class RedeemCode extends BasePo {

    @Column(columnDefinition = "varchar(225) NOT NULL DEFAULT '' COMMENT '兑换码标题'")
    @ApiModelProperty(value = "兑换码标题",example = "兑换码标题")
    @ExcelField(title = "兑换码标题", order = 1)
    private String title;

    @Column(columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '兑换码'")
    @ApiModelProperty(value = "兑换码",example = "兑换码")
    @ExcelField(title = "兑换码", order = 2)
    private String code;

    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '兑换码数量 '")
    @ApiModelProperty(value = "兑换码次数",example = "兑换码次数")
    @ExcelField(title = "兑换码次数", order = 3)
    private Integer useNum;

    @Column(columnDefinition = "varchar(64) NOT NULL DEFAULT 'N' COMMENT '兑换码'")
    @ApiModelProperty(value = "兑换状态",example = "兑换状态")
    @Enumerated(EnumType.STRING)
    private RedeemStateEnum state;

    @ApiModelProperty(value = "兑换管理id",example = "兑换管理id")
    @Column(columnDefinition = "bigint(20) DEFAULT NULL COMMENT '兑换管理id'")
    private Long redeemId;

    @ApiModelProperty(value = "用户id",example = "用户id")
    @Column(columnDefinition = "bigint(20) DEFAULT NULL COMMENT '用户id'")
    private Long userId;

    @ApiModelProperty(value = "邮箱",example = "邮箱")
    @Column(columnDefinition = "varchar(225) DEFAULT NULL COMMENT '邮箱'")
    private String email;

    @ApiModelProperty(value = "绑定时间",example = "绑定时间")
    @Column(columnDefinition = "datetime DEFAULT NULL COMMENT '绑定时间'")
    private Date useTime;

}
