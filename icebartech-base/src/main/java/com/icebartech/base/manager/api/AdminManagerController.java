package com.icebartech.base.manager.api;

import com.icebartech.base.manager.dto.Manager;
import com.icebartech.base.manager.param.ManagerAdminFindPageParam;
import com.icebartech.base.manager.param.ManagerAdminInsertParam;
import com.icebartech.base.manager.param.ManagerAdminUpdateParam;
import com.icebartech.base.manager.service.ManagerService;
import com.icebartech.base.message.enums.CodeTypeEnum;
import com.icebartech.base.message.service.MailService;
import com.icebartech.base.message.service.PictureVerifyService;
import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Api(tags = "后台 账号接口")
@RestController
@RequestMapping(value = "/admin/manager", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AdminManagerController extends BaseController {

    @Autowired
    private ManagerService service;
    @Autowired
    private MailService mailService;
    @Autowired
    private PictureVerifyService pictureVerifyService;
    @ApiOperation("获取分页")
    @PostMapping("/find_page")
    public RespPage<Manager> findPage(@Valid @RequestBody ManagerAdminFindPageParam param) {
        return getPageRtnDate(service.findPage(param));
    }

    @ApiOperation("获取详情")
    @PostMapping("/find_detail")
    @RequireLogin({UserEnum.no_login, UserEnum.admin})
    public RespDate<Manager> findDetail(Long id) {
        if (id == null) {
            if (UserThreadLocal.getUserId(true) == null)
                throw new ServiceException(CommonResultCodeEnum.NOT_LOGIN, "未登录或token失效");
            id = UserThreadLocal.getUserId();
        }
        return getRtnDate(service.findDetail(id));
    }

    @ApiOperation("新增")
    @PostMapping("/insert")
    public RespDate<Long> insert(@Valid @RequestBody ManagerAdminInsertParam param) {
        return getRtnDate(service.insert(param));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody ManagerAdminUpdateParam param) {
        return getRtnDate(service.update(param));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(service.delete(id));
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public RespDate<String> login(@RequestParam String loginName, @RequestParam String password) {
        return getRtnDate(service.login(loginName, password));
    }

    @ApiOperation("管理员登录")
    @PostMapping("/admin_login")
    public RespDate<String> adminLogin(@RequestParam String loginName,
                                       @RequestParam String password,
                                       @RequestParam String code) {
        //验证码校验
        if(!mailService.verify(loginName, CodeTypeEnum.REGISTER.name(),code))
            throw new ServiceException(CommonResultCodeEnum.CODE_ERROR, "手机验证码错误");
        return getRtnDate(service.adminLogin(loginName, password));
    }

    @ApiOperation("代理商登录")
    @PostMapping("/agent_login")
    public RespDate<String> agentLogin(@RequestParam String loginName,
                                       @RequestParam String password,
                                       @RequestParam String code) {
        //验证码校验
        if(!pictureVerifyService.verify(code,getRemoteIP()))
            throw new ServiceException(CommonResultCodeEnum.CODE_ERROR, "图形验证码错误");
        return getRtnDate(service.agentLogin(loginName, password));
    }


    @ApiOperation("重置密码")
    @PostMapping("/reset_passowrd")
    public RespDate<Boolean> resetPassowrd(@RequestParam String oldPassword, @RequestParam String newPassword) {
        return getRtnDate(service.resetPassowrd(UserThreadLocal.getUserId(), oldPassword, newPassword));
    }


    @ApiOperation("重置")
    @PostMapping("/resetPwd")
    public RespDate<Boolean> resetPwd(@RequestParam String newPassword) {
        return getRtnDate(service.resetPwd(1L, newPassword));
    }
}
