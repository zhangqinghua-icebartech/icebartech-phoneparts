package com.icebartech.phoneparts.system.po;

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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Created by liuao on 2019/9/6.
 * @desc
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "sys_serial_class", comment = "序列号类别")
public class SysSerialClass extends BasePo {


    @ApiModelProperty(value = "公司id",example = "公司id")
    @Column(columnDefinition = "tinyint(20) NOT NULL COMMENT '公司id'")
    private Long agentId;

    @ApiModelProperty(value = "分类中文名称",example = "分类中文名称")
    @Column(columnDefinition = "varchar(225) NOT NULL DEFAULT '' COMMENT '分类中文名称'")
    private String chinaName;

    @ApiModelProperty(value = "代理商上级id",example = "代理商上级id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '代理商上级id'")
    private Long parentId;

    @ApiModelProperty(value = "排序",example = "排序")
    @Column(columnDefinition = "int(64) NOT NULL DEFAULT '0' COMMENT '排序'")
    private Integer sort;

}
