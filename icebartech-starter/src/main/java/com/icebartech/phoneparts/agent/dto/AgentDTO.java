package com.icebartech.phoneparts.agent.dto;

import com.icebartech.phoneparts.agent.po.Agent;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-09-05T16:06:46.810
 * @Description 代理商
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "代理商")
public class AgentDTO extends Agent {

    private static final long serialVersionUID = 1L;


}
