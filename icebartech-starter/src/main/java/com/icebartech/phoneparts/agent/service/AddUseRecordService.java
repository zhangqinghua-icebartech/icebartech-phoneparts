package com.icebartech.phoneparts.agent.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.agent.dto.AddUseRecordDTO;
import com.icebartech.phoneparts.agent.po.AddUseRecord;

/**
 * @author pc
 * @Date 2019-09-05T17:01:59.673
 * @Description 添加记录
 */

public interface AddUseRecordService extends BaseService
<AddUseRecordDTO, AddUseRecord> {


    void add(Long userId,Integer useNum);

}
