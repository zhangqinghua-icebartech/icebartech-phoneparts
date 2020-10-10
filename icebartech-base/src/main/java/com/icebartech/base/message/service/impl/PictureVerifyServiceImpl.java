package com.icebartech.base.message.service.impl;

import com.icebartech.base.message.service.PictureVerifyService;
import com.icebartech.core.components.RedisComponent;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Created by liuao on 2020/8/5 0005$.
 * @desc
 */
@Service
@XSlf4j
public class PictureVerifyServiceImpl implements PictureVerifyService {

    @Autowired
    private RedisComponent redisComponent;

    public static final String RANDOMCODEKEY= "RANDOMREDISKEY";//放到session中的key

    @Override
    public boolean verify(String code,String ip) {
        log.info("验证码={}，ip={}",code,ip);
        String code1 = (String) redisComponent.get(RANDOMCODEKEY,ip);
        if(code1==null) return false;
        return code1.equalsIgnoreCase(code);
    }

}
