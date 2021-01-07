package com.icebartech.phoneparts.redeem.service.impl;

import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.redeem.dto.RedeemDTO;
import com.icebartech.phoneparts.redeem.param.RedeemCodeInsertParam;
import com.icebartech.phoneparts.redeem.param.RedeemCustomInsertParam;
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

import java.util.List;

import static com.icebartech.core.vo.QueryParam.eq;

@Service
public class RedeemServiceImpl extends AbstractService<RedeemDTO, Redeem, RedeemRepository> implements RedeemService {

    @Autowired
    AgentService agentService;
    @Autowired
    RedeemCodeService redeemCodeService;


    @Override
    protected void warpDTO(Long id, RedeemDTO redeemDTO) {
        redeemDTO.setAgent(agentService.findOneOrNull(redeemDTO.getAgentId()));
    }


    /**
     * 新增兑换码
     */
    @Override
    @Transactional
    public Boolean insertAll(RedeemInsertParam param) {
        Long id = super.insert(param);

        // 查询代理商所属的设备分类名称
        String className = "ALL";
        if (param.getAgentId() != null && param.getAgentId() != 0L) {
            className = agentService.findOne(param.getAgentId()).getClassName();
        }

        //添加兑换码
        for (int i = 0; i < param.getRedeemNum(); i++) {
            // code 需要加上agentId设备一级分类
            redeemCodeService.insert(new RedeemCodeInsertParam(id,
                                                               param.getTitle(),
                                                               className + ProduceCodeUtil.findRedeemCode(),
                                                               param.getUseNum()));
        }
        return true;
    }

    /**
     * 新增自定义兑换码
     */
    @Override
    @Transactional
    public Boolean insertCustom(RedeemCustomInsertParam param) {
        Long id = super.insert(param);

        // 查询代理商所属的设备分类名称
        String className = "";
        if (param.getAgentId() != null) {
            className = agentService.findOne(param.getAgentId()).getClassName();
        }

        //添加兑换码
        // code 需要加上agentId设备一级分类
        redeemCodeService.insert(new RedeemCodeInsertParam(id,
                                                           param.getTitle(),
                                                           param.getCode(),
                                                           param.getUseNum()));
        return true;
    }

    @Override
    public Boolean insertCustom(String title, Long agentId, List<RedeemCode> redeemCodes) {
        // 1. 创建兑换码
        Redeem redeem = new Redeem();
        redeem.setTitle(title);
        redeem.setAgentId(agentId);
        redeem.setRedeemNum(redeemCodes.size() + "");
        redeem.setUseNum("1");
        Long redeemId = super.insert(redeem);

        // 2. 创建兑换码明细
        for (RedeemCode redeemCode : redeemCodes) {
            redeemCode.setRedeemId(redeemId);
            redeemCode.setTitle(title);
            redeemCodeService.insert(redeemCode);
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteAll(Long id) {
        if (!super.delete(id)) throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "邮箱不存在");
        return redeemCodeService.delete(eq(RedeemCode::getRedeemId, id));
    }
}