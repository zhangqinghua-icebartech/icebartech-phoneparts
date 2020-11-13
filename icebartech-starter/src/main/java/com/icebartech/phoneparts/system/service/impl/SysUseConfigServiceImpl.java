package com.icebartech.phoneparts.system.service.impl;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.enums.UseConfigInitEnum;
import com.icebartech.phoneparts.system.param.SysUseConfigInsertParam;
import com.icebartech.phoneparts.system.repository.SysUseConfigRepository;
import com.icebartech.phoneparts.system.service.SysUseConfigService;
import com.icebartech.phoneparts.system.dto.SysUseConfigDTO;
import com.icebartech.phoneparts.system.po.SysUseConfig;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author pc
 * @Date 2019-09-04T14:32:53.763
 * @Description 常用设置
 */

@Service
public class SysUseConfigServiceImpl extends AbstractService
<SysUseConfigDTO, SysUseConfig, SysUseConfigRepository> implements SysUseConfigService {

    @Override
    @Transactional
    public Boolean changeState(Long id, ChooseType state) {
        if(state == ChooseType.y){
            super.findList(eq(SysUseConfigDTO::getState,ChooseType.y))
                    .forEach(sysUseConfigDTO -> super.update(eq(SysUseConfigDTO::getId,sysUseConfigDTO.getId()),eq(SysUseConfigDTO::getState,ChooseType.n)));
        }
        return super.update(eq(SysUseConfigDTO::getId,id),eq(SysUseConfigDTO::getState,state));
    }

    @Override
    public SysUseConfigDTO findInUse() {
        return super.findOneOrNull(eq(SysUseConfigDTO::getUserId, UserThreadLocal.getUserId()),eq(SysUseConfigDTO::getState,ChooseType.y));
    }

    @Override
    @Async
    public void init(Long userId) {
        //默认1
        super.insert(new SysUseConfigInsertParam(userId,UseConfigInitEnum.SPEED_A.getKey()
                ,UseConfigInitEnum.PRESSURE_A.getKey(),UseConfigInitEnum.NAME_A.name(),ChooseType.y));
        //默认2
        super.insert(new SysUseConfigInsertParam(userId,UseConfigInitEnum.SPEED_B.getKey()
                ,UseConfigInitEnum.PRESSURE_B.getKey(),UseConfigInitEnum.NAME_B.name(),ChooseType.n));
    }
}