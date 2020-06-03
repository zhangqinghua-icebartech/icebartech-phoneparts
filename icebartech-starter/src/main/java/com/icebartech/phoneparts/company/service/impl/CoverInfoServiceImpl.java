package com.icebartech.phoneparts.company.service.impl;

import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.company.dto.CompanyInfoDto;
import com.icebartech.phoneparts.company.po.CompanyInfo;
import com.icebartech.phoneparts.company.repository.CoverInfoRepository;
import com.icebartech.phoneparts.company.service.CoverInfoService;
import com.icebartech.phoneparts.company.dto.CoverInfoDTO;
import com.icebartech.phoneparts.company.po.CoverInfo;
import com.icebartech.phoneparts.user.po.User;
import com.icebartech.phoneparts.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author pc
 * @Date 2019-09-10T15:13:02.532
 * @Description 启动页
 */

@Service
public class CoverInfoServiceImpl extends AbstractService
<CoverInfoDTO, CoverInfo, CoverInfoRepository> implements CoverInfoService {

    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;
    @Autowired
    private AgentService agentService;
    @Autowired
    private UserService userService;

    @Override
    protected void warpDTO(Long id, CoverInfoDTO companyInfo) {
        companyInfo.setIconOne(aliyunOSSComponent.generateDownloadUrl(companyInfo.getIconOne()));
        companyInfo.setIconTwo(aliyunOSSComponent.generateDownloadUrl(companyInfo.getIconTwo()));
    }

    @Override
    protected void preInsert(CoverInfoDTO coverInfo) {

        LocalUser localUser = UserThreadLocal.getUserInfo();

        //一级代理商
        if(localUser.getLevel() == 1){

            coverInfo.setAgentId(localUser.getUserId());
            coverInfo.setAgentName(agentService.findOne(coverInfo.getAgentId()).getCompanyName());

            if(coverInfo.getEnable() == ChooseType.y){
                super.findList(eq(CoverInfoDTO::getAgentId,coverInfo.getAgentId()),eq(CoverInfoDTO::getEnable,ChooseType.y))
                        .forEach(coverInfoDTO -> super.update(eq(CoverInfoDTO::getId,coverInfoDTO.getId()),eq(CoverInfoDTO::getEnable,ChooseType.n)));
            }

        }else if(localUser.getLevel() == 2){

            AgentDTO agent = agentService.findOne(localUser.getUserId());
            coverInfo.setAgentId(agent.getParentId());
            coverInfo.setAgentName(agentService.findOne(agent.getParentId()).getCompanyName());
            coverInfo.setSecondAgentId(localUser.getUserId());
            coverInfo.setAgentName(agentService.findOne(coverInfo.getSecondAgentId()).getCompanyName());

            if(coverInfo.getEnable() == ChooseType.y){
                super.findList(eq(CoverInfoDTO::getSecondAgentId,agent.getParentId()),eq(CoverInfoDTO::getEnable,ChooseType.y))
                        .forEach(coverInfoDTO -> super.update(eq(CoverInfoDTO::getId,coverInfoDTO.getId()),eq(CoverInfoDTO::getEnable,ChooseType.n)));
            }

        }

    }

    @Override
    protected void preUpdate(CoverInfoDTO coverInfoDTO) {
        coverInfoDTO.setAgentName(agentService.findOne(coverInfoDTO.getAgentId()).getCompanyName());
    }


    @Override
    public Boolean changeEnable(Long id, ChooseType enable) {
        CoverInfoDTO coverInfo = super.findOne(id);
        if(enable == ChooseType.y){
            if(coverInfo.getSecondAgentId()!=0){
                super.findList(eq(CoverInfoDTO::getAgentId,coverInfo.getAgentId()),eq(CoverInfoDTO::getEnable,ChooseType.y))
                        .forEach(coverInfoDTO -> super.update(eq(CoverInfoDTO::getId,coverInfoDTO.getId()),eq(CoverInfoDTO::getEnable,ChooseType.n)));
            }else {
                super.findList(eq(CoverInfoDTO::getSecondAgentId,coverInfo.getSecondAgentId()),eq(CoverInfoDTO::getEnable,ChooseType.y))
                        .forEach(coverInfoDTO -> super.update(eq(CoverInfoDTO::getId,coverInfoDTO.getId()),eq(CoverInfoDTO::getEnable,ChooseType.n)));
            }

        }
        return super.update(eq(CoverInfoDTO::getId,id),eq(CoverInfoDTO::getEnable,enable));
    }

    @Override
    public CoverInfoDTO findInCover() {
        User user = userService.findOne(UserThreadLocal.getUserId());
        if(user.getSecondAgentId() == 0L){
            return super.findOneOrNull(eq(CoverInfoDTO::getAgentId,user.getAgentId()),eq(CoverInfoDTO::getEnable,ChooseType.y));
        }
        return super.findOneOrNull(eq(CoverInfoDTO::getSecondAgentId,user.getSecondAgentId()),eq(CoverInfoDTO::getEnable,ChooseType.y));
    }

}