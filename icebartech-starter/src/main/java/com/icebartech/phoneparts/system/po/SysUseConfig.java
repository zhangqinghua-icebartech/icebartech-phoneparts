package com.icebartech.phoneparts.system.po;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.po.BasePo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Created by liuao on 2019/9/4.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "sys_use_config", comment = "常用设置")
public class SysUseConfig extends BasePo {

    @Index(name = "userId")
    @ApiModelProperty(value = "用户id",example = "用户id")
    @Column(columnDefinition = "bigint(20) NOT NULL COMMENT '用户id'")
    private Long userId;

    @ApiModelProperty(value = "速度",example = "速度")
    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '速度'")
    private Integer speed;

    @ApiModelProperty(value = "压力",example = "压力")
    @Column(columnDefinition = "int(11) NOT NULL DEFAULT '0' COMMENT '压力'")
    private Integer pressure;

    @ApiModelProperty(value = "名称",example = "名称")
    @Column(columnDefinition = "varchar(20) NOT NULL COMMENT '名称'")
    private String name;

    @ApiModelProperty(value = "使用状态",example = "使用状态")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "CHAR(1) NOT NULL DEFAULT 'n' COMMENT '使用状态'")
    private ChooseType state;

}
