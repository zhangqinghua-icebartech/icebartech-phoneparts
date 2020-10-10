package com.icebartech.base.message.api;

import com.icebartech.base.message.enums.CodeTypeEnum;
import com.icebartech.base.message.service.MailService;
import com.icebartech.base.message.service.PictureVerifyService;
import com.icebartech.core.annotations.RequireLogin;
import com.icebartech.core.components.RedisComponent;
import com.icebartech.core.constants.UserEnum;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.vo.RespDate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Created by liuao on 2020/8/6 0006$.
 * @desc
 */
@Api(tags = "手机验证码")
@RestController
@RequestMapping(value = "/sms", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SmsController extends BaseController {

    @Autowired
    private MailService mailService;
    @Autowired
    private PictureVerifyService pictureVerifyService;

    @ApiOperation("发送手机验证码")
    @RequireLogin(UserEnum.no_login)
    @PostMapping("/sendCode")
    public RespDate<Boolean> findList(HttpServletRequest request,
                                      @RequestParam("phone") String phone,
                                      @RequestParam("code") String code) {
        //验证码校验
        if(!pictureVerifyService.verify(code,getRemoteIP()))
            throw new ServiceException(CommonResultCodeEnum.CODE_ERROR, "图形验证码错误");
        String type = CodeTypeEnum.REGISTER.name();
        return getRtnDate(mailService.sendCode(phone,type,false));
    }
}
