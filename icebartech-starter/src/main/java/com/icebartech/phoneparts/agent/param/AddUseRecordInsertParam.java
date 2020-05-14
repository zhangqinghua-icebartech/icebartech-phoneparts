package com.icebartech.phoneparts.agent.param;

import com.icebartech.phoneparts.user.po.User;
import com.icebartech.phoneparts.user.service.UserService;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

/**
 * @author Created by liuao on 2019/9/5.
 * @desc
 */
@Data
public class AddUseRecordInsertParam {


    @NotNull
    @ApiModelProperty(value = "用户id",example = "用户id")
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "代理商id",example = "代理商id")
    private Long agentId;

    @NotNull
    @ApiModelProperty(value = "新增次数",example = "新增次数")
    private Integer useNum;


    @ApiModelProperty(value = "序列号",example = "序列号")
    private String serialNum;

    @ApiModelProperty(value = "绑定邮箱",example = "绑定邮箱")
    private String bindMail;

    public AddUseRecordInsertParam(){

    }


    public AddUseRecordInsertParam(Long agentId,Integer useNum,User user){
        this.userId = user.getId();
        this.agentId = agentId;
        this.useNum = useNum;
        serialNum = user.getSerialNum();
        bindMail = user.getEmail();
    }
}
