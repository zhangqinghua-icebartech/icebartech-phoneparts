package com.icebartech.base.message.api;

import com.icebartech.base.message.service.PictureVerifyService;
import com.icebartech.core.controller.BaseController;
import com.icebartech.core.utils.VerifyUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Created by liuao on 2020/8/5 0005$.
 * @desc
 */
@Api(tags = "图形验证码")
@RestController
@RequestMapping(value = "/img", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PictureVerifyController extends BaseController {

    @Autowired
    PictureVerifyService service;

    @Autowired
    VerifyUtil verifyUtil;

    /**
     * @author XXXXXX
     * @date 2018年7月11日
     * @desc 图形验证码生成
     */
    @RequestMapping("/createImg")
    public void createImg(HttpServletRequest request, HttpServletResponse response){

        response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        verifyUtil.getRandcode(request, response,getRemoteIP());//输出验证码图片

    }



}
