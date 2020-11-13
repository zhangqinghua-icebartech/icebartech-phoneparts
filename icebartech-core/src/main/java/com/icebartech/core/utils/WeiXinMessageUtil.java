package com.icebartech.core.utils;

import com.icebartech.core.constants.WeiXinConstant;
import com.icebartech.core.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 微信公众平台消息处理工具类
 *
 * @author Administrator
 */
public class WeiXinMessageUtil {
    /**
     * 获取text类型回复消息的xml
     *
     * @param toUserName
     * @param fromUserName
     * @param createTime
     * @param item
     * @return
     */
    public static String getTextXml(String toUserName, String fromUserName, int createTime, TextItem item) {

        if (StringUtils.isBlank(toUserName) || createTime <= 0 || StringUtils.isBlank(item.getContent())) {
            return null;
        }

        return "<xml>" +
                "<ToUserName>" + String.format(WeiXinConstant.CDATA_FORMAT, toUserName) + "</ToUserName>" +
                "<FromUserName>" + String.format(WeiXinConstant.CDATA_FORMAT, fromUserName) + "</FromUserName>" +
                "<CreateTime>" + String.format(WeiXinConstant.CDATA_FORMAT, createTime) + "</CreateTime>" +
                "<MsgType>" + String.format(WeiXinConstant.CDATA_FORMAT, WeiXinConstant.TEXT_TYPE) + "</MsgType>" +
                "<Content>" + String.format(WeiXinConstant.CDATA_FORMAT, item.getContent()) + "</Content>" +
                "</xml>";
    }

    /**
     * 获取image类型回复的xml
     *
     * @param toUserName
     * @param fromUserName
     * @param createTime
     * @param item
     * @return
     */
    public static String getImageXml(String toUserName, String fromUserName, int createTime, ImageItem item) {
        if (StringUtils.isBlank(toUserName) || createTime <= 0 || StringUtils.isBlank(item.getMediaId())) {
            return null;
        }

        return "<xml>" +
                "<ToUserName>" + String.format(WeiXinConstant.CDATA_FORMAT, toUserName) + "</ToUserName>" +
                "<FromUserName>" + String.format(WeiXinConstant.CDATA_FORMAT, fromUserName) + "</FromUserName>" +
                "<CreateTime>" + String.format(WeiXinConstant.CDATA_FORMAT, createTime) + "</CreateTime>" +
                "<MsgType>" + String.format(WeiXinConstant.CDATA_FORMAT, WeiXinConstant.IMAGE_TYPE) + "</MsgType>" +
                "<Image>" +
                "<MediaId>" + String.format(WeiXinConstant.CDATA_FORMAT, item.getMediaId()) + "</MediaId>" +
                "</Image>" +
                "</xml>";
    }

    /**
     * 获取voice类型值的xml
     *
     * @param toUserName
     * @param fromUserName
     * @param createTime
     * @param item
     * @return
     */
    public static String getVoiceXml(String toUserName, String fromUserName, int createTime, VoiceItem item) {
        if (StringUtils.isBlank(toUserName) || createTime <= 0 || StringUtils.isBlank(item.getMediaId())) {
            return null;
        }

        return "<xml>" +
                "<ToUserName>" + String.format(WeiXinConstant.CDATA_FORMAT, toUserName) + "</ToUserName>" +
                "<FromUserName>" + String.format(WeiXinConstant.CDATA_FORMAT, fromUserName) + "</FromUserName>" +
                "<CreateTime>" + String.format(WeiXinConstant.CDATA_FORMAT, createTime) + "</CreateTime>" +
                "<MsgType>" + String.format(WeiXinConstant.CDATA_FORMAT, WeiXinConstant.VOICE_TYPE) + "</MsgType>" +
                "<Voice>" +
                "<MediaId>" + String.format(WeiXinConstant.CDATA_FORMAT, item.getMediaId()) + "</MediaId>" +
                "</Voice>" +
                "</xml>";
    }

    /**
     * 获取video类型的xml值
     *
     * @param toUserName
     * @param fromUserName
     * @param createTime
     * @param item
     * @return
     */
    public static String getVideoXml(String toUserName, String fromUserName, int createTime, VideoItem item) {
        if (StringUtils.isBlank(toUserName) || createTime <= 0
                || item == null || StringUtils.isBlank(item.getMediaId())
                || StringUtils.isBlank(item.getTitle())
                || StringUtils.isBlank(item.getDescription())) {
            return null;
        }

        return "<xml>" +
                "<ToUserName>" + String.format(WeiXinConstant.CDATA_FORMAT, toUserName) + "</ToUserName>" +
                "<FromUserName>" + String.format(WeiXinConstant.CDATA_FORMAT, fromUserName) + "</FromUserName>" +
                "<CreateTime>" + String.format(WeiXinConstant.CDATA_FORMAT, createTime) + "</CreateTime>" +
                "<MsgType>" + String.format(WeiXinConstant.CDATA_FORMAT, WeiXinConstant.VOICE_TYPE) + "</MsgType>" +
                "<Video>" +
                "<MediaId>" + String.format(WeiXinConstant.CDATA_FORMAT, item.getMediaId()) + "</MediaId>" +
                "<Title>" + String.format(WeiXinConstant.CDATA_FORMAT, item.getTitle()) + "</Title>" +
                "<Description>" + String.format(WeiXinConstant.CDATA_FORMAT, item.getDescription()) + "</Description>" +
                "</Video>" +
                "</xml>";
    }

    /**
     * 获取music类型的xml值
     *
     * @param toUserName
     * @param fromUserName
     * @param createTime
     * @param item
     * @return
     */
    public static String getMusicXml(String toUserName, String fromUserName, int createTime, MusicItem item) {
        if (StringUtils.isBlank(toUserName)
                || createTime <= 0
                || item == null
                || StringUtils.isBlank(item.getTitle())
                || StringUtils.isBlank(item.getThumbMediaId())
                || StringUtils.isBlank(item.getDescription())
                || StringUtils.isBlank(item.getMusicUrl())
                || StringUtils.isBlank(item.getHQMusicUrl())) {
            return null;
        }

        return "<xml>" +
                "<ToUserName>" + String.format(WeiXinConstant.CDATA_FORMAT, toUserName) + "</ToUserName>" +
                "<FromUserName>" + String.format(WeiXinConstant.CDATA_FORMAT, fromUserName) + "</FromUserName>" +
                "<CreateTime>" + String.format(WeiXinConstant.CDATA_FORMAT, createTime) + "</CreateTime>" +
                "<MsgType>" + String.format(WeiXinConstant.CDATA_FORMAT, WeiXinConstant.MUSIC_TYPE) + "</MsgType>" +
                "<Music>" +
                "<Title>" + String.format(WeiXinConstant.CDATA_FORMAT, item.getTitle()) + "</Title>" +
                "<Description>" + String.format(WeiXinConstant.CDATA_FORMAT, item.getDescription()) + "</Description>" +
                "<MusicUrl>" + String.format(WeiXinConstant.CDATA_FORMAT, item.getMusicUrl()) + "</MusicUrl>" +
                "<HQMusicUrl>" + String.format(WeiXinConstant.CDATA_FORMAT, item.getHQMusicUrl()) + "</HQMusicUrl>" +
                "<ThumbMediaId>" + String.format(WeiXinConstant.CDATA_FORMAT, item.getThumbMediaId()) + "</ThumbMediaId>" +
                "</Music>" +
                "</xml>";
    }

    /**
     * 获取图文类型的xml
     *
     * @param toUserName
     * @param fromUserName
     * @param createTime
     * @param newsList
     * @return
     */
    public static String getNewsXml(String toUserName, String fromUserName, int createTime, List<NewsItem> newsList) {
        if (StringUtils.isBlank(toUserName) || createTime <= 0 || CollectionUtils.isEmpty(newsList)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append("<ToUserName>").append(String.format(WeiXinConstant.CDATA_FORMAT, toUserName)).append("</ToUserName>");
        sb.append("<FromUserName>").append(String.format(WeiXinConstant.CDATA_FORMAT, fromUserName)).append("</FromUserName>");
        sb.append("<CreateTime>").append(String.format(WeiXinConstant.CDATA_FORMAT, createTime)).append("</CreateTime>");
        sb.append("<MsgType>").append(String.format(WeiXinConstant.CDATA_FORMAT, WeiXinConstant.NEWS_TYPE)).append("</MsgType>");
        sb.append("<ArticleCount>").append(String.format(WeiXinConstant.CDATA_FORMAT, newsList.size())).append("</ArticleCount>");
        sb.append("<Articles>");
        for (NewsItem newsItem : newsList) {
            sb.append("<item>");
            sb.append("<Title>").append(String.format(WeiXinConstant.CDATA_FORMAT, newsItem.getTitle())).append("</Title>");
            sb.append("<Description>").append(String.format(WeiXinConstant.CDATA_FORMAT, newsItem.getDescription())).append("</Description>");
            sb.append("<PicUrl>").append(String.format(WeiXinConstant.CDATA_FORMAT, newsItem.getPicUrl())).append("</PicUrl>");
            sb.append("<Url>").append(String.format(WeiXinConstant.CDATA_FORMAT, newsItem.getUrl())).append("</Url>");
            sb.append("</item>");
        }
        sb.append("</Articles>");
        sb.append("</xml>");

        return sb.toString();
    }

    /**
     * 构造微信支付中传递的xml，可通用
     *
     * @param paramMap
     * @return
     */
    public static String getWeiXinPayXml(Map<String, String> paramMap) {
        StringBuilder sb = new StringBuilder();
        if (paramMap != null && paramMap.size() > 0) {
            sb.append("<xml>");
            for (Entry<String, String> entry : paramMap.entrySet()) {
                sb.append("<").append(entry.getKey()).append(">");
                sb.append(String.format(WeiXinConstant.CDATA_FORMAT, entry.getValue()));
                sb.append("</").append(entry.getKey()).append(">");
            }
            sb.append("</xml>");
            return sb.toString();
        } else {
            return null;
        }
    }
}
