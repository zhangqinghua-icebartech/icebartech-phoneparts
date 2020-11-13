package com.icebartech.phoneparts.agent.po;

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
 * @author Created by liuao on 2019/9/5.
 * @desc
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "add_use_record", comment = "添加记录")
public class AddUseRecord extends BasePo {

    @ApiModelProperty(value = "用户id",example = "用户id")
    @Column(columnDefinition = "bigint(20) NOT NULL COMMENT '用户id'")
    private Long userId;

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL COMMENT '代理商id'")
    private Long agentId;

    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '新增次数 '")
    @ApiModelProperty(value = "新增次数",example = "新增次数")
    private Integer useNum;

    @ApiModelProperty(value = "序列号",example = "序列号")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '序列号'")
    private String serialNum;

    @ApiModelProperty(value = "绑定邮箱",example = "绑定邮箱")
    @Column(columnDefinition = "varchar(225) DEFAULT NULL COMMENT '绑定邮箱'")
    private String bindMail;

}
