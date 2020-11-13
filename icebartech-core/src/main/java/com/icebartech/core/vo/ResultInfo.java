package com.icebartech.core.vo;

public class ResultInfo {

    /**
     * 是否成功
     */
    private boolean isSuccess = false;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 携带的数据
     */
    private Object data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
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
