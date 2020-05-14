package com.icebartech.phoneparts.company.po;

import com.icebartech.core.enums.ChooseType;
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
 * @author Created by liuao on 2019/6/17.
 * @desc
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(appliesTo = "company_info", comment = "公司简介")
public class CompanyInfo extends BasePo {

    @ApiModelProperty(value = "代理商id",example = "代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '代理商id'")
    private Long agentId;

    @ApiModelProperty(value = "次级代理商id",example = "次级代理商id")
    @Column(columnDefinition = "bigint(20) NOT NULL DEFAULT '0' COMMENT '次级代理商id'")
    private Long secondAgentId;

    @ApiModelProperty(value = "图标",example = "图标")
    @Column(columnDefinition = "varchar(225) DEFAULT NULL COMMENT '图标'")
    private String icon;

    @ApiModelProperty(value = "代理商名称",example = "代理商名称")
    @Column(columnDefinition = "varchar(225) DEFAULT NULL COMMENT '代理商名称'")
    private String agentName;

    @ApiModelProperty(value = "次级代理商名称",example = "次级代理商名称")
    @Column(columnDefinition = "varchar(225) DEFAULT NULL COMMENT '次级代理商名称'")
    private String secondAgentName;

    @ApiModelProperty(value = "是否上架",example = "是否上架")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "CHAR(1) NOT NULL DEFAULT 'n' COMMENT '是否下架'")
    private ChooseType enable;

}
