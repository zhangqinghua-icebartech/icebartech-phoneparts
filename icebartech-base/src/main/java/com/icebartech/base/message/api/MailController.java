package com.icebartech.base.message.api;

import com.icebartech.base.message.enums.CodeTypeEnum;
import com.icebartech.base.message.service.MailService;
import com.icebartech.core.annotations.RequireLogin;
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


/**
 * @author Created by liuao on 2019/6/24.
 * @desc
 */
@Api(tags = "邮箱")
@RestController
@RequestMapping(value = "/mail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MailController extends BaseController {

    private MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @ApiOperation("发送邮箱验证码")
    @RequireLogin(UserEnum.no_login)
    @PostMapping("/sendCode")
    public RespDate<Boolean> findList(@RequestParam("mail") String mail) {
        String type = CodeTypeEnum.REGISTER.name();
        return getRtnDate(mailService.sendCode(mail,type,true));
    }

}
