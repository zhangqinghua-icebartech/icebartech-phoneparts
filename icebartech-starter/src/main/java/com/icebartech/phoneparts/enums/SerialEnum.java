package com.icebartech.phoneparts.enums;

/**
 * @author Created by liuao on 2019/6/21.
 * @desc
 */
public enum SerialEnum {

    SERVER(0,"平台"),

    AGENT(1,"代理商"),



    NO_STATUS(2,"已过期"),
    IS_STATUS(1,"已使用"),
    MAX_USENUM(1,"使用最大次数"),
    IS_BIND(1,"已绑定邮箱");









    private int key;
    private String desc;

    SerialEnum(int key,String desc){
        this.key = key;
        this.desc = desc;
    }

    public int getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }
}
