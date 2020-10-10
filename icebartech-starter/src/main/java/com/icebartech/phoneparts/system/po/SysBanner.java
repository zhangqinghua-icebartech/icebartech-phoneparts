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

/**
 * @author Created by liuao on 2020/9/27.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "sys_banner", comment = "横幅")
public class SysBanner extends BasePo {

    @ApiModelProperty(value = "详情图",example = "详情图")
    @Column(columnDefinition = "varchar(225) NOT NULL COMMENT '详情图'")
    private String detailIcon;


}
