package com.icebartech.phoneparts.company.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.phoneparts.agent.repository.AgentRepository;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.company.dto.CompanyInfoDto;
import com.icebartech.phoneparts.company.po.CompanyInfo;
import com.icebartech.phoneparts.company.repository.CompanyInfoRepository;
import com.icebartech.phoneparts.company.service.CompanyInfoService;
import com.icebartech.phoneparts.user.dto.UserDto;
import com.icebartech.phoneparts.user.po.User;
import com.icebartech.phoneparts.user.repository.UserRepository;
import com.icebartech.phoneparts.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author pc
 * @Date 2019-06-18T11:12:57.763
 * @Description 公司简介
 */

@Service
public class CompanyInfoServiceImpl extends AbstractService<CompanyInfoDto, CompanyInfo, CompanyInfoRepository> implements CompanyInfoService {

    @Autowired
    private UserService userService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;


    @Override
    protected void warpDTO(Long id, CompanyInfoDto companyInfo) {
        companyInfo.setIcon(aliyunOSSComponent.generateDownloadUrl(companyInfo.getIcon()));
        LocalUser localUser = UserThreadLocal.getUserInfo();
        if (localUser.getLevel() == 1) {
            if (companyInfo.getSecondAgentId() != 0) {
                companyInfo.setAgentName(agentRepository.companyName(companyInfo.getSecondAgentId()));
                // companyInfo.setAgentName( agentService.findOne(companyInfo.getSecondAgentId()).getCompanyName());
            }
        } else {
            if (companyInfo.getAgentId() != 0) {
                companyInfo.setAgentName(agentRepository.companyName(companyInfo.getAgentId()));
                // companyInfo.setAgentName(agentService.findOne(companyInfo.getAgentId()).getCompanyName());
            }
        }
    }

    @Override
    protected void preInsert(CompanyInfoDto companyInfo) {
        LocalUser localUser = UserThreadLocal.getUserInfo();

        //一级代理商
        if (localUser.getLevel() == 1) {
            companyInfo.setSecondAgentId(companyInfo.getAgentId());
            companyInfo.setAgentId(localUser.getUserId());
            if (companyInfo.getEnable() == ChooseType.y) {
                super.findList(eq(CompanyInfoDto::getSecondAgentId, companyInfo.getSecondAgentId()), eq(CompanyInfoDto::getEnable, ChooseType.y))
                     .forEach(companyInfoDto -> super.update(eq(CompanyInfo::getId, companyInfoDto.getId()), eq(CompanyInfoDto::getEnable, ChooseType.n)));
            }

        } else {

            if (companyInfo.getEnable() == ChooseType.y) {
                super.findList(eq(CompanyInfoDto::getAgentId, companyInfo.getAgentId()), eq(CompanyInfoDto::getEnable, ChooseType.y))
                     .forEach(companyInfoDto -> super.update(eq(CompanyInfo::getId, companyInfoDto.getId()), eq(CompanyInfoDto::getEnable, ChooseType.n)));
            }

        }


    }

    @Override
    protected void preUpdate(CompanyInfoDto companyInfo) {
        companyInfo.setAgentName(agentService.findOne(companyInfo.getAgentId()).getCompanyName());
    }


    @Override
    public Boolean changeEnable(Long id, ChooseType enable) {
        CompanyInfo companyInfo = super.findOne(id);
        if (enable == ChooseType.y) {

            if (companyInfo.getSecondAgentId() != 0) {
                super.findList(eq(CompanyInfoDto::getSecondAgentId, companyInfo.getSecondAgentId()), eq(CompanyInfoDto::getEnable, ChooseType.y))
                     .forEach(companyInfoDto -> super.update(eq(CompanyInfo::getId, companyInfoDto.getId()), eq(CompanyInfoDto::getEnable, ChooseType.n)));
            } else {
                super.findList(eq(CompanyInfoDto::getAgentId, companyInfo.getAgentId()), eq(CompanyInfoDto::getEnable, ChooseType.y))
                     .forEach(companyInfoDto -> super.update(eq(CompanyInfo::getId, companyInfoDto.getId()), eq(CompanyInfoDto::getEnable, ChooseType.n)));

            }
        }
        return super.update(eq(CompanyInfoDto::getId, id), eq(CompanyInfoDto::getEnable, enable));
    }

    @Override
    public CompanyInfoDto findInCompany(Long userId) {
        UserDto user = BeanMapper.map(userRepository.agentId(userId), UserDto.class);
        if (user.getSecondAgentId() == 0L) {
            return super.findOneOrNull(eq(CompanyInfoDto::getAgentId, user.getAgentId()),
                                       eq(CompanyInfoDto::getEnable, ChooseType.y));
        }
        return super.findOneOrNull(eq(CompanyInfoDto::getSecondAgentId, user.getSecondAgentId()),
                                   eq(CompanyInfoDto::getEnable, ChooseType.y));
    }
}