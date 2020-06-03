package com.icebartech.phoneparts.enums;

/**
 * @author Created by liuao on 2019/8/28.
 * @desc
 */
public enum RedeemStateEnum {

    Y(1,"已使用"),
    N(0,"未使用");


    private int key;
    private String desc;

    RedeemStateEnum(int key,String desc){
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
