package com.icebartech.phoneparts.enums;

/**
 * @author Created by liuao on 2019/6/21.
 * @desc
 */
public enum UserEnableEnum {

    NO_ENABLE(1,"用户失效"),
    Y_ENABLE(0,"用户有效");


    private int key;
    private String desc;

    UserEnableEnum(int key, String desc){
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
