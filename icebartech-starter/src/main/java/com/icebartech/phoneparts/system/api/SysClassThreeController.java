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
import com.icebartech.phoneparts.system.param.SysClassThreeInsertParam;
import com.icebartech.phoneparts.system.param.SysClassThreeListParam;
import com.icebartech.phoneparts.system.param.SysClassThreePageParam;
import com.icebartech.phoneparts.system.param.SysClassThreeUpdateParam;
import com.icebartech.phoneparts.system.service.SysClassThreeService;
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


/**
 * @author Created by liuao on 2020/6/8 0008$.
 * @desc
 */
@Api(tags = "三级分类接口")
@RestController
@RequestMapping(value = "/sysClassThree", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SysClassThreeController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SysClassThreeService sysClassThreeService;

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_page")
    public RespPage<SysClassThreeDTO> findPage(@Valid @RequestBody SysClassThreePageParam param) {
        // 1. 查询所有代理商可见和当前APP用户可见的数据
        LocalUser localUser = UserThreadLocal.getUserInfo();
        if(localUser.getUserEnum() == UserEnum.app){
            param.setAgentIdIn(new ArrayList<>());
            param.getAgentIdIn().add(0L);
            User user = userService.findOne(localUser.getUserId());
            param.getAgentIdIn().add(user.getAgentId());
        }

        // 2. 查询分页
        Page<SysClassThreeDTO> page = sysClassThreeService.findPage(param);

        // 3. 获取代理商数据
        List<Long> agentIds = page.getContent().stream().map(SysClassThreeDTO::getAgentId).collect(Collectors.toList());
        List<AgentDTO> agents = agentService.findList(QueryParam.in(Agent::getId, agentIds));
        page.getContent().forEach(d->d.setAgent(agents.stream().filter(a->a.getId().equals(d.getAgentId())).findAny().orElse(new AgentDTO("全部"))));
        return getPageRtnDate(page);
    }

    @ApiOperation("获取列表")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_list")
    public RespDate<List<SysClassThreeDTO>> findList() {
        return getRtnDate(sysClassThreeService.findList(new SysClassThreeListParam()));
    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/find_detail")
    public RespDate<SysClassThreeDTO> findDetail(@RequestParam Long id) {
        return getRtnDate(sysClassThreeService.findDetail(id));
    }

    @ApiOperation("新增")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody SysClassThreeInsertParam param) {
        return getRtnDate(sysClassThreeService.insert(param));
    }

    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody SysClassThreeUpdateParam param) {
        return getRtnDate(sysClassThreeService.update(param));
    }

    @ApiOperation("修改排序")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/changeSort")
    public RespDate<Boolean> changeSort(@RequestParam("id") Long id,
                                        @RequestParam("sort") Integer sort) {
        return getRtnDate(sysClassThreeService.changeSort(id, sort));
    }

    @ApiOperation("删除")
    @RequireLogin({UserEnum.admin,UserEnum.app})
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        ProductDto product = productService.findByClassThreeId(id);
        if(product!=null)
            throw new ServiceException(CommonResultCodeEnum.INVALID_OPERATION, "请先删除其菜单下的单品");
        return getRtnDate(sysClassThreeService.delete(id));
    }
}
