package com.icebartech.phoneparts.redeem.service.impl;

import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.redeem.dto.RedeemCodeDTO;
import com.icebartech.phoneparts.redeem.dto.RedeemDTO;
import com.icebartech.phoneparts.redeem.param.RedeemCodeInsertParam;
import com.icebartech.phoneparts.redeem.param.RedeemInsertParam;
import com.icebartech.phoneparts.redeem.po.Redeem;
import com.icebartech.phoneparts.redeem.po.RedeemCode;
import com.icebartech.phoneparts.redeem.repository.RedeemRepository;
import com.icebartech.phoneparts.redeem.service.RedeemCodeService;
import com.icebartech.phoneparts.redeem.service.RedeemService;
import com.icebartech.phoneparts.util.ProduceCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.icebartech.core.vo.QueryParam.eq;
/**
 * @author pc
 * @Date 2019-08-28T15:35:58.161
 * @Description 兑换码管理
 */

@Service
public class RedeemServiceImpl extends AbstractService
<RedeemDTO, Redeem, RedeemRepository> implements RedeemService {

    @Autowired
    RedeemCodeService redeemCodeService;

    @Autowired
    AgentService agentService;

    @Override
    protected void warpDTO(Long id, RedeemDTO redeemDTO) {

        redeemDTO.setAgent(agentService.findOneOrNull(redeemDTO.getAgentId()));

    }


    @Override
    @Transactional
    public Boolean insertAll(RedeemInsertParam param) {
        Long id = super.insert(param);
        //添加兑换码
        for(int i=0;i<param.getRedeemNum();i++){
            redeemCodeService.insert(new RedeemCodeInsertParam(id,param.getTitle(),
                    ProduceCodeUtil.findRedeemCode(),param.getUseNum()));
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteAll(Long id) {
        if(!super.delete(id)) throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "邮箱不存在");
        return redeemCodeService.delete(eq(RedeemCode::getRedeemId,id));
    }
}