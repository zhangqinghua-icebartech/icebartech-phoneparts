package com.icebartech.core.vo;

/**
 * 微信事件回复对象
 *
 * @author Administrator
 */
public class WeiXinEventResponse {

    private String responseType;//事件类型

    private String eventContent;//事件回复内容（可支持json格式的字符串）


    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

}
