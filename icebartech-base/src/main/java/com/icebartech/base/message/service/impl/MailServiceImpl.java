package com.icebartech.base.message.service.impl;

import com.icebartech.base.message.enums.CodeTypeEnum;
import com.icebartech.base.message.service.MailService;
import com.icebartech.core.components.MailComponent;
import com.icebartech.core.components.RedisComponent;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author Created by liuao on 2019/7/2.
 * @desc
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private RedisComponent redisComponent;
    private MailComponent mailComponent;

    @Autowired
    public MailServiceImpl(RedisComponent redisComponent,
                           MailComponent mailComponent) {
        this.redisComponent = redisComponent;
        this.mailComponent = mailComponent;
    }


    @Override
    public Boolean sendCode(String mail, String type) {
        //生成验证码
        Random ne=new Random();
        String code=ne.nextInt(9999-1000+1)+1000+"";
        log.info("发送验证码，手机号{}，类型{}，验证码{}",mail,type,code);
        //验证发送次数
        varCount(mail,type);
        redisComponent.set(type,mail,code,CodeTypeEnum.TIME.getKey());
        mailComponent.send(mail,code);
        return true;
    }

    @Override
    public Boolean verify(String mail, String type, String code) {
        log.info("核实验证码，手机号{}，类型{}，验证码{}",mail,type,code);
        String codeY = (String) redisComponent.get(type,mail);
        log.info("被核实的正确验证码，验证码{}",codeY);
        if (codeY==null) return false;
        if(codeY.equals(code)){
            //验证完毕清除掉
            redisComponent.del(type,mail);
            return true;
        }
        return false;
    }


    /**
     * 验证码校验次数
     * @param mobile 手机号
     * @param type 类型
     */
    private void varCount(String mobile, String type) {
        //验证码计数
        String countType = type + CodeTypeEnum._NUM.name();
        Integer count = (Integer) redisComponent.get(countType,mobile);
        //校验次数
        if(count!=null && count>CodeTypeEnum._NUM.getKey())
            throw new ServiceException(CommonResultCodeEnum.CALL_LIMITED, "次数超出限制");
        if(count!=null) count++;
        else count = 1;
        redisComponent.set(countType,mobile,count,CodeTypeEnum.VERTIME.getKey());
    }



}
