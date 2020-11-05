package com.icebartech.core.vo;

import java.io.Serializable;

/**
 * 返回的json数据对象
 *
 * @author haosheng.wenhs
 */
public class BaseJson implements Serializable {
    private static final long serialVersionUID = 4065646120864495638L;
    private int status;
    private String msg;
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
