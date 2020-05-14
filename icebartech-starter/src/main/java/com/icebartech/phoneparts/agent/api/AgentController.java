package com.icebartech.phoneparts.agent.api;

import com.icebartech.base.manager.dto.Manager;
import com.icebartech.base.manager.service.ManagerService;
import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.dto.LoginDTO;
import com.icebartech.phoneparts.agent.param.AgentInsertParam;
import com.icebartech.phoneparts.agent.param.AgentPageParam;
import com.icebartech.phoneparts.agent.param.AgentUpdateParam;
import com.icebartech.phoneparts.agent.service.AgentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author Created by liuao on 2019/9/5.
 * @desc
 */
@Slf4j
@Api(tags = "代理商模块接口")
@RestController
@RequestMapping(value = "/agent", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AgentController extends BaseController {

    @Autowired
    AgentService service;
    @Autowired
    ManagerService managerService;


    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/find_page")
    public RespPage<AgentDTO> findPage(@Valid @RequestBody AgentPageParam param) {
        LocalUser localUser = UserThreadLocal.getUserInfo();
        //总后台
        if(localUser.getLevel() == 0){
            param.setParentId(0L);
            //一级代理商
        }else if(localUser.getLevel()==1){
            param.setParentId(localUser.getUserId());
        //代理商二级
        }else if(localUser.getLevel()==2){
            param.setId(localUser.getUserId());
        }
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("获取列表")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/find_list")
    public RespDate<List<AgentDTO>> findList() {

        LocalUser localUser = UserThreadLocal.getUserInfo();
        List<AgentDTO> agentDTOS;
        //总后台
        if(localUser.getLevel() == 0){
            agentDTOS = service.findList(eq(AgentDTO::getParentId,0L));
        //一级代理商
        }else if(localUser.getLevel()==1){
            agentDTOS = service.findList(eq(AgentDTO::getParentId,localUser.getUserId()));
        //代理商二级
        }else if(localUser.getLevel()==2){
            log.info("------------------ 2 -------------------");
            agentDTOS = service.findList(eq(AgentDTO::getId,localUser.getUserId()));
        //平台
        }else {
            agentDTOS = service.findList();
        }

        return getRtnDate(agentDTOS);
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/find_detail")
    public RespDate<AgentDTO> findDetail(@RequestParam Long id) {
        return getRtnDate(service.findDetail(id));
    }

    @ApiOperation("获取登录人信息")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/find_login")
    public RespDate<LoginDTO> findLogin() {
        LocalUser localUser = UserThreadLocal.getUserInfo();
        LoginDTO login = new LoginDTO();
        //代理商信息
        if(localUser.getUserEnum()==UserEnum.agent){
            AgentDTO agent = service.findOne(localUser.getUserId());
            //代理商一级
            if(agent.getParentId() == 0){
                login.setLevel(1);
            //代理商二级
            }else {
                login.setLevel(2);
                login.setParentId(agent.getParentId());
            }
            login.setId(agent.getId());
            login.setName(agent.getCompanyName());
            login.setUserEnum(UserEnum.agent);
        }
        //总后台信息
        else if (localUser.getUserEnum()==UserEnum.admin){
           Manager manager = managerService.findOne(localUser.getUserId());
            login.setId(manager.getId());
            login.setName(manager.getUserName());
            login.setUserEnum(UserEnum.admin);
            login.setLevel(0);
        }
        return getRtnDate(login);
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody AgentInsertParam param) {
        LocalUser localUser = UserThreadLocal.getUserInfo();
        if(localUser.getLevel()==1){
            param.setParentId(localUser.getUserId());
        }
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody AgentUpdateParam param) {
        return getRtnDate(service.update(param));
    }

    @ApiOperation("上架下架")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/change_enable")
    public RespDate<Boolean> changeEnable(@RequestParam("id") Long id,
                                         @RequestParam("enable") ChooseType enable) {
        return getRtnDate(service.changeEnable(id,enable));
    }

    @ApiOperation("修改排序")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/changeSort")
    public RespDate<Boolean> changeSort(@RequestParam("id") Long id,
                                        @RequestParam("sort") Integer sort) {
        return getRtnDate(service.changeSort(id,sort));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(service.delete(id));
    }


    @ApiOperation("添加次数")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/addUseCount")
    public RespDate<Boolean> addUseCount(@ApiParam("用户id") @RequestParam("agentId") Long agentId,
                                         @ApiParam("添加次数") @RequestParam("num") Integer num) {
        return getRtnDate(service.addUseCount(agentId,num));
    }
    @ApiOperation("后台减少次数")
    @RequireLogin({UserEnum.admin,UserEnum.agent})
    @PostMapping("/backReduceUseCount")
    public RespDate<Boolean> reduceUseCount(@ApiParam("用户id") @RequestParam("agentId") Long agentId,
                                            @ApiParam("减少次数") @RequestParam("num") Integer num) {
        return getRtnDate(service.reduceUseCount(agentId,num));
    }

}
