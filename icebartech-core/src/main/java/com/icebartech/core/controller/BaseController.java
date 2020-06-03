package com.icebartech.core.controller;

import com.icebartech.core.vo.BaseData;
import com.icebartech.core.vo.PageData;
import com.icebartech.core.vo.RespDate;
import com.icebartech.core.vo.RespPage;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseController {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    protected static <E> RespPage<E> getPageRtnDate(Page<E> page) {
        RespPage<E> respJson = new RespPage<>();
        respJson.setData(new PageData<>((int) page.getTotalElements(), page.getTotalPages(), page.getContent()));
        return respJson;
    }

    protected static <E> RespDate<E> getRtnDate(E e) {
        RespDate<E> respJson = new RespDate<>();
        respJson.setData(new BaseData<>(e));
        return respJson;
    }

    /**
     * 获得系统访问的前缀地址，如：http://www.xxx.com:8080/
     *
     * @return
     */
    protected String getBasePath() {
        String path = request.getContextPath();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    }

    /**
     * 获取客户端真实ip
     *
     * @return
     */
    protected String getRemoteIP() {
        String ip = request.getHeader("X-nginx-real-ip");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_REAL_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            return "";
        }
        return ip;
    }
}
