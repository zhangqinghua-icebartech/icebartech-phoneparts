package com.icebartech.phoneparts.company.dto;

import com.icebartech.phoneparts.company.po.CoverInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author pc
 * @Date 2019-09-10T15:13:02.532
 * @Description 启动页
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "启动页")
public class CoverInfoDTO extends CoverInfo{

    private static final long serialVersionUID = 1L;

}
