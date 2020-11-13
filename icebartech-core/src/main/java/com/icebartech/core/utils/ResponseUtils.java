package com.icebartech.core.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Response公用类
 *
 * @author wenhsh
 */
public class ResponseUtils {

    /**
     * 输出Json
     *
     * @param response
     * @param text
     */
    public static void renderJson(HttpServletResponse response, String text) {
        render(response, "application/json", text);
    }

    /**
     * 输出Json
     *
     * @param response
     * @param text
     * @param callback 回调
     */
    public static void renderJson(HttpServletResponse response, String text, String callback) {
        if (callback == null || "".equals(callback.trim())) {
            renderJson(response, text);
        } else {
            renderJson(response, callback + "(" + text + ");");
        }

    }

    /**
     * 输出text
     *
     * @param response
     * @param text
     */
    public static void renderText(HttpServletResponse response, String text) {
        render(response, "text/plain", text);
    }

    /**
     * 输出text
     *
     * @param response
     * @param text
     */
    public static void renderText(HttpServletResponse response, String text, String callback) {
        if (callback == null || "".equals(callback.trim())) {
            renderText(response, text);
        } else {
            renderText(response, callback + "(\"" + text + "\");");
        }
    }

    /**
     * 输出text
     *
     * @param response
     * @param text
     */
    public static void renderXml(HttpServletResponse response, String text) {
        render(response, "text/xml", text);
    }

    /**
     * 输出html
     *
     * @param response
     * @param text
     */
    public static void renderHtml(HttpServletResponse response, String text) {
        render(response, "text/html", text);
    }


    /**
     * 输出javascript
     *
     * @param response
     * @param text
     */
    public static void renderJavaScript(HttpServletResponse response, String text) {
        render(response, "text/javascript", text);
    }

    /**
     * 输出x-javascript
     *
     * @param response
     * @param text
     */
    public static void renderXJavaScript(HttpServletResponse response, String text) {
        render(response, "application/x-javascriptt", text);
    }

    /**
     * 直接输出
     *
     * @param response
     * @param contentType
     * @param text
     */
    public static void render(HttpServletResponse response, String contentType, String text) {
        response.setContentType(contentType + ";charset=UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            PrintWriter writer = response.getWriter();
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
        }
    }
}
