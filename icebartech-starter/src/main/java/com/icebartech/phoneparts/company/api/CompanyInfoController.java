package com.icebartech.phoneparts.company.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.company.dto.CompanyInfoDto;
import com.icebartech.phoneparts.company.param.CompanyInsertParam;
import com.icebartech.phoneparts.company.param.CompanyPageParam;
import com.icebartech.phoneparts.company.param.CompanyUpdateParam;
import com.icebartech.phoneparts.company.service.CompanyInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.icebartech.core.vo.QueryParam.in;

/**
 * 此业务非常凌乱。
 *
 * 一个公司有一级代理商和二级代理商。
 *
 * 新增/修改的时候动态的分别设置一级代理商和二级代理商
 */
@Api(tags = "关于我们模块的接口")
@RestController
@RequestMapping(value = "/company", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CompanyInfoController extends BaseController {

    @Autowired
    private AgentService agentService;
    @Autowired
    private CompanyInfoService companyInfoService;


    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/find_page")
    public RespPage<CompanyInfoDto> findPage(@Valid @RequestBody CompanyPageParam param) {
        LocalUser localUser = UserThreadLocal.getUserInfo();

        // 1. 超级管理员，查询一级代理商
        if (localUser.getLevel() == 0) {
            param.setSecondAgentId(0L);
        }
        // 2. 一级代理商，查询二级代理商
        if (localUser.getLevel() == 1) {
            param.setAgentId(localUser.getUserId());
            param.setSecondAgentIdNotEq(0L);
        }
        // 3. 二级代理商，查询三级代理商
        if (localUser.getLevel() == 2) {
            param.setSecondAgentId(localUser.getUserId());
        }

        // 3. 设置次级代理商数据
        Page<CompanyInfoDto> page = companyInfoService.findPage(param);
        List<Long> agentIds = page.getContent().stream().filter(a->a.getSecondAgentId() != 0L).map(CompanyInfoDto::getSecondAgentId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(agentIds)) {
            List<AgentDTO> agents = agentService.findList(in(Agent::getId, agentIds));
            page.getContent().forEach(d -> d.setAgent(agents.stream().filter(a -> a.getId().equals(d.getSecondAgentId())).findAny().orElse(null)));
        }

        // 4. 设置一级代理商数据
        agentIds = page.getContent().stream().filter(d -> d.getAgent() == null).map(CompanyInfoDto::getAgentId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(agentIds)) {
            List<AgentDTO> agents = agentService.findList(in(Agent::getId, agentIds));
            page.getContent().forEach(d -> agents.stream().filter(a -> a.getId().equals(d.getAgentId())).findAny().ifPresent(d::setAgent));
        }

        return getPageRtnDate(page);
    }

    @ApiOperation("获取使用中配置")
    @RequireLogin({UserEnum.app})
    @PostMapping("/find_in_company")
    public RespDate<CompanyInfoDto> findInCompany() {
        return getRtnDate(companyInfoService.findInCompany());
    }

//    @ApiOperation("获取列表")
//    @PostMapping("/find_list")git
//    public RespDate<List<CompanyInfoDto>> findList() {
//        return getRtnDate(service.findList());
//    }


    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/find_detail")
    public RespDate<CompanyInfoDto> findDetail(@RequestParam Long id) {
        return getRtnDate(companyInfoService.findDetail(id));
    }

    @ApiOperation("上架下架")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/changeShow")
    public RespDate<Boolean> changeEnable(@RequestParam("id") Long id,
                                          @RequestParam("enable") ChooseType enable) {
        return getRtnDate(companyInfoService.changeEnable(id, enable));
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody CompanyInsertParam param) {
        return getRtnDate(companyInfoService.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody CompanyUpdateParam param) {
        return getRtnDate(companyInfoService.update(param));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(companyInfoService.delete(id));
    }
}
