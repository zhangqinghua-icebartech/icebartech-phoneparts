package com.icebartech.phoneparts.agent.dto;

import com.icebartech.phoneparts.agent.po.AddUseRecord;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.user.po.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-09-05T17:01:59.673
 * @Description 添加记录
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "添加记录")
public class AddUseRecordDTO extends AddUseRecord {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户",example = "用户")
    private User user;

    @ApiModelProperty(value = "代理商",example = "代理商")
    private Agent agent;
}
