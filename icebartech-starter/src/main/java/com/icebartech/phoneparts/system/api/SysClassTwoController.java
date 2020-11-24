package com.icebartech.phoneparts.system.api;

import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.vo.QueryParam;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.agent.dto.AgentDTO;
import com.icebartech.phoneparts.agent.po.Agent;
import com.icebartech.phoneparts.agent.service.AgentService;
import com.icebartech.phoneparts.product.dto.ProductDto;
import com.icebartech.phoneparts.product.service.ProductService;
import com.icebartech.phoneparts.system.dto.SysClassOneDto;
import com.icebartech.phoneparts.system.dto.SysClassThreeDTO;
import com.icebartech.phoneparts.system.dto.SysClassTwoDto;
import com.icebartech.phoneparts.system.param.*;
import com.icebartech.phoneparts.system.po.SysClassThree;
import com.icebartech.phoneparts.system.service.SysClassThreeService;
import com.icebartech.phoneparts.system.service.SysClassTwoService;
import com.icebartech.phoneparts.user.po.User;
import com.icebartech.phoneparts.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.icebartech.core.vo.QueryParam.eq;

/**
 * @author Created by liuao on 2019/6/18.
 * @desc
 */
@Api(tags = "二级分类接口")
@RestController
@RequestMapping(value = "/sysClassTwo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SysClassTwoController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SysClassTwoService sysClassTwoService;
    @Autowired
    private SysClassThreeService sysClassThreeService;


    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_page")
    public RespPage<SysClassTwoDto> findPage(@Valid @RequestBody SysClassTwoPageParam param) {
        // 1. 查询所有代理商可见和当前APP用户可见的数据
        LocalUser localUser = UserThreadLocal.getUserInfo();
        if(localUser.getUserEnum() == UserEnum.app){
            param.setAgentIdIn(new ArrayList<>());
            param.getAgentIdIn().add(0L);
            User user = userService.findOne(localUser.getUserId());
            param.getAgentIdIn().add(user.getAgentId());
        }

        // 2. 查询分页
        Page<SysClassTwoDto> page = sysClassTwoService.findPage(param);

        // 3. 获取代理商数据
        List<Long> agentIds = page.getContent().stream().map(SysClassTwoDto::getAgentId).collect(Collectors.toList());
        List<AgentDTO> agents = agentService.findList(QueryParam.in(Agent::getId, agentIds));
        page.getContent().forEach(d->d.setAgent(agents.stream().filter(a->a.getId().equals(d.getAgentId())).findAny().orElse(new AgentDTO("全部"))));
        return getPageRtnDate(page);
    }

    @ApiOperation("获取列表")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_list")
    public RespDate<List<SysClassTwoDto>> findList() {
        return getRtnDate(sysClassTwoService.findList(new SysClassTwoListParam()));
    }

    @ApiOperation("获取一级分类下的二级分类名称")
    @PostMapping("/find_name_by_one")
    public RespDate<List<SysClassTwoDto>> find_name_by_one(@RequestParam Long classOneId) {
        return getRtnDate(sysClassTwoService.find_name_by_one(classOneId));
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_detail")
    public RespDate<SysClassTwoDto> findDetail(@RequestParam Long id) {
        return getRtnDate(sysClassTwoService.findDetail(id));
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody SysClassTwoInsertParam param) {
        return getRtnDate(sysClassTwoService.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody SysClassTwoUpdateParam param) {
        return getRtnDate(sysClassTwoService.update(param));
    }

    @ApiOperation("修改排序")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/changeSort")
    public RespDate<Boolean> changeSort(@RequestParam("id") Long id,
                                        @RequestParam("sort") Integer sort) {
        return getRtnDate(sysClassTwoService.changeSort(id, sort));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        ProductDto product = productService.findByClassTwoId(id);
        if(product!=null)
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "请先删除其菜单下的单品");

        SysClassThree classThree = sysClassThreeService.findOneOrNull(eq(SysClassThree::getClassTwoId,id));
        if(classThree!=null)
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "请先删除其菜单下的三级分类");
        return getRtnDate(sysClassTwoService.delete(id));
    }

    @ApiOperation("核实密码")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/verify_pwd")
    public RespDate<Boolean> verifyPwd(@RequestParam Long id,
                                        @RequestParam String password) {
        return getRtnDate(sysClassTwoService.verifyPwd(id, password));
    }
}
