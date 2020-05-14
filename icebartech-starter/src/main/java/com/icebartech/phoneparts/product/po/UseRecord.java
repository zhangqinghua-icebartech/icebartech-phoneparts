package com.icebartech.phoneparts.product.po;

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
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "use_record", comment = "使用记录表")
public class UseRecord extends BasePo {

    @ApiModelProperty(value = "用户id",example = "用户id")
    @Column(columnDefinition = "bigint(20) NOT NULL COMMENT '用户id'")
    private Long userId;

    @ApiModelProperty(value = "产品id",example = "产品id")
    @Column(columnDefinition = "bigint(20) NOT NULL COMMENT '产品id'")
    private Long productId;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '代理商id'")
    private Long agentId;

    @ApiModelProperty(value = "次级代理商id",example = "次级代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '次级代理商id'")
    private Long secondAgentId;

}
