package com.icebartech.phoneparts.user.api;

import com.github.ExcelUtils;
import com.icebartech.base.message.enums.CodeTypeEnum;
import com.icebartech.base.message.service.MailService;
import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.components.AliyunOSSComponent;
import com.icebartech.core.components.LoginComponent;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import com.icebartech.phoneparts.user.dto.UserDto;
import com.icebartech.phoneparts.user.param.UserInsertParam;
import com.icebartech.phoneparts.user.param.UserOutParam;
import com.icebartech.phoneparts.user.param.UserPageParam;
import com.icebartech.phoneparts.user.param.UserUpdateParam;
import com.icebartech.phoneparts.user.po.LoginDto;
import com.icebartech.phoneparts.user.service.UserService;
import com.icebartech.phoneparts.util.CacheComponent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(tags = "用户模块接口")
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private LoginComponent loginComponent;
    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;

    @ApiOperation("数据导出")
    @GetMapping("/excelOut")
    public void excelOut(HttpServletResponse response, UserOutParam param) throws Exception {
        ExcelUtils.getInstance().
                exportObjects2Excel(userService.export(param), UserDto.class, true, null, true, response, "用户列表");
    }

    @ApiOperation("获取分页")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/find_page")
    public RespPage<UserDto> findPage(@Valid @RequestBody UserPageParam param) {
        LocalUser localUser = UserThreadLocal.getUserInfo();

        //一级代理商
        if (localUser.getLevel() == 1) {
            param.setAgentId(localUser.getUserId());
            //二级代理商
        } else if (localUser.getLevel() == 2) {
            param.setSecondAgentId(localUser.getUserId());
        }

        return getPageRtnDate(userService.findPage(param));
    }

//    @ApiOperation("获取列表")
//    @RequireLogin(UserEnum.admin)
//    @PostMapping("/find_list")
//    public RespDate<List<UserDTO>> findList() {
//        return getRtnDate(service.findList());
//    }

    @ApiOperation("获取详情")
    @RequireLogin({UserEnum.admin, UserEnum.app, UserEnum.agent})
    @PostMapping("/find_detail")
    public RespDate<UserDto> findDetail(@RequestParam(value = "id", required = false) Long id) {
        LocalUser localUser = UserThreadLocal.getUserInfo();
        if (localUser.getUserEnum() == UserEnum.app) {
            // 1. 直接从缓存读取数据
            id = UserThreadLocal.getUserId();
            UserDto user = cacheComponent.getUserDetail(id);
            if (user != null) return getRtnDate(user);

            // 2. 从数据库读取数据，然后放进缓存中
            user = userService.findDetail(id);
            cacheComponent.setUserDetail(id, user);
            getRtnDate(user);
        }
        return getRtnDate(userService.findDetail(id));
    }

//    @ApiOperation("新增")
//    @PostMapping("/insert")
//    public RespDate<Long> insert(@Valid @RequestBody UserInsertParam param) {
//        return getRtnDate(service.insert(param));
//    }

    @ApiOperation("注册")
    @PostMapping("/register")
    @RequireLogin(UserEnum.no_login)
    public RespDate<Long> register(@Valid @RequestBody UserInsertParam param) {
        //验证码校验
//        if(!mailService.verify(param.getEmail(), CodeTypeEnum.REGISTER.name(),param.getCode()))
//            throw new ServiceException(CommonResultCodeEnum.CODE_ERROR, "验证码校验失败");
        return getRtnDate(userService.register(param));
    }


    @ApiOperation("序列号登录")
    @PostMapping("/code_login")
    @RequireLogin(UserEnum.no_login)
    public RespDate<LoginDto> codeLogin(@RequestParam String serialNum,
                                        @RequestParam String code) {
        if (!code.equals("dev123")) {
            throw new ServiceException(CommonResultCodeEnum.CODE_ERROR, "验证码错误");
        }

        UserDto userDTO = userService.codeLogin(serialNum);
        LoginDto loginDto = BeanMapper.map(userDTO, LoginDto.class);
        loginDto.setHeadPortrait(aliyunOSSComponent.generateDownloadUrl(loginDto.getHeadPortrait()));
        loginDto.setSessionId(loginComponent.getLocalUser(userDTO.getId(), -1));
        return getRtnDate(loginDto);
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    @RequireLogin(UserEnum.no_login)
    public RespDate<LoginDto> login(@RequestParam("email") String email,
                                    @RequestParam("pwd") String pwd) {
        UserDto userDTO = userService.login(email, pwd);
        LoginDto loginDto = BeanMapper.map(userDTO, LoginDto.class);
        loginDto.setHeadPortrait(aliyunOSSComponent.generateDownloadUrl(loginDto.getHeadPortrait()));
        loginDto.setSessionId(loginComponent.getLocalUser(userDTO.getId(), 7 * 24 * 60 * 60));
        return getRtnDate(loginDto);
    }

    @ApiOperation("修改密码")
    @PostMapping("/changePwd")
    @RequireLogin(UserEnum.no_login)
    public RespDate<Boolean> changePwd(@RequestParam("email") String email,
                                       @RequestParam("code") String code,
                                       @RequestParam("pwd") String pwd) {
        //验证码校验
        if (!mailService.verify(email, CodeTypeEnum.REGISTER.name(), code))
            throw new ServiceException(CommonResultCodeEnum.CODE_ERROR, "验证码校验失败");
        return getRtnDate(userService.changePwd(email, pwd));
    }


    @ApiOperation("修改头像")
    @RequireLogin(UserEnum.app)
    @PostMapping("/changeHead")
    public RespDate<Boolean> changeHead(@RequestParam("headPortrait") String headPortrait) {
        return getRtnDate(userService.changeHead(UserThreadLocal.getUserId(), headPortrait));
    }


    @ApiOperation("退出登录")
    @RequireLogin(UserEnum.app)
    @PostMapping("/outLogin")
    public RespDate<Boolean> outLogin(HttpServletRequest request) {
        String sessionId = request.getHeader("sessionId");
        loginComponent.logout(sessionId);
        return getRtnDate(true);
    }


    @ApiOperation("修改")
    @RequireLogin({UserEnum.admin, UserEnum.app})
    @PostMapping("/update")
    public RespDate<Boolean> update(@Valid @RequestBody UserUpdateParam param) {
        return getRtnDate(userService.update(param));
    }

    @ApiOperation("删除")
    @RequireLogin(UserEnum.admin)
    @PostMapping("/delete")
    public RespDate<Boolean> delete(@RequestParam Long id) {
        return getRtnDate(userService.delete(id));
    }


    @ApiOperation("添加次数")
    @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/addUseCount")
    public RespDate<Boolean> addUseCount(@ApiParam("用户id") @RequestParam("userId") Long userId,
                                         @ApiParam("添加次数") @RequestParam("num") Integer num) {
        return getRtnDate(userService.addUseCount(userId, num));
    }


    @ApiOperation("兑换码添加次数")
    @RequireLogin(UserEnum.app)
    @PostMapping("/scanUseCount")
    public RespDate<Boolean> addUseCount(@ApiParam("兑换码") @RequestParam("code") String code) {
        return getRtnDate(userService.addUseCount(code));
    }

    @ApiOperation("用户使用减少次数")
    @RequireLogin(UserEnum.app)
    @PostMapping("/reduceUseCount")
    public RespDate<Boolean> reduceUseCount(@RequestParam Long productId) {
        return getRtnDate(userService.reduceUseCount(productId, UserThreadLocal.getUserId()));
    }

    @ApiOperation("后台减少次数")
    // @RequireLogin({UserEnum.admin, UserEnum.agent})
    @PostMapping("/backReduceUseCount")
    public RespDate<Boolean> reduceUseCount(@ApiParam("用户id") @RequestParam("userId") Long userId,
                                            @ApiParam("减少次数") @RequestParam("num") Integer num) {
        return getRtnDate(userService.reduceUseCount(userId, num));
    }
}
