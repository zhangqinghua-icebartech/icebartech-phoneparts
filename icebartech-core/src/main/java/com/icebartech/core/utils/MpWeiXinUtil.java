package com.icebartech.core.utils;

import com.alibaba.fastjson.JSON;
import com.icebartech.core.components.RedisComponent;
import com.icebartech.core.constants.IcebartechConstants;
import com.icebartech.core.constants.WeiXinConstant;
import com.icebartech.core.properties.SiteProperties;
import com.icebartech.core.properties.WeixinMpProperties;
import com.icebartech.core.vo.WeiXinMpUserInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 接入微信平台处理工具类（适用于公众号）
 *
 * @author haosheng.wenhs
 */
@Component
public class MpWeiXinUtil {

    private static Logger logger = LoggerFactory.getLogger("weiXinMessage");

    @Autowired
    private SiteProperties siteProperties;


    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private WeixinMpProperties mpWeixinProperties;

    private static String default_key = "_weixin_mp";


    /**
     * 校验微信的token
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public boolean validToken(String signature, String timestamp, String nonce) {
        boolean flag = false;
        if (StringUtils.isBlank(signature) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce)) {
            return flag;
        }
        String[] arrays = new String[]{mpWeixinProperties.getToken(), timestamp, nonce};
        Arrays.sort(arrays);
        StringBuffer sha1Before = new StringBuffer();
        for (String array : arrays) {
            sha1Before.append(array);
        }

        if (StringUtils.isNotBlank(sha1Before.toString())) {
            String sha1After = DigestUtils.sha1Hex(sha1Before.toString());
            if (sha1After.equals(signature)) {
                //表示验证通过
                flag = true;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 对页面上微信js使用config生成sign签名
     *
     * @param noncestr  随机字符串
     * @param ticket    从微信处获取到的ticket
     * @param timestamp 时间戳
     * @param url       页面完整url
     * @return
     */
    public String signJsConfig(String noncestr, String ticket, String timestamp, String url) {
        StringBuffer prestr = new StringBuffer();
        Map<String, String> params = new HashMap<>();
        params.put("noncestr", noncestr);
        params.put("jsapi_ticket", ticket);
        params.put("timestamp", timestamp);
        params.put("url", url);

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr.append(key + "=" + value);
            } else {
                prestr.append(key + "=" + value + "&");
            }
        }
        String sha1Sign = DigestUtils.sha1Hex(prestr.toString());
        return sha1Sign;
    }

    /**
     * 从微信处获取access_token
     *
     * @return
     */
    public String getAccessTokenFromWeiXin(boolean fresh) {
        String accessToken = null;
        try {
            //先从缓存里面取访问的accessToke
            accessToken = (String) redisComponent.get(IcebartechConstants.ACCESS_TOKEN_GROUP, siteProperties.getAppName() + default_key);

            if (StringUtils.isBlank(accessToken) || fresh) {
                String requestUrl = String.format(WeiXinConstant.ACCESS_TOKEN_URL, mpWeixinProperties.getAppId(), mpWeixinProperties.getAppSecret());
                accessToken = parseAccessToken(requestUrl);
            }
            if (fresh) {
                logger.info("getAccessTokenFromWeiXin refresh token at :{}", LocalDateTime.now());
            }
        } catch (Exception e) {
            logger.error("getAccessTokenFromWeiXin return exception, errorMsg:" + e.getMessage(), e);
        }
        return accessToken;
    }

    /**
     * 获取jsapi_ticket凭证
     *
     * @return
     */
    public String getJsapiTicket() {
        String jsapiTicket = null;
        try {
            //先从缓存里面拿ticket
            jsapiTicket = (String) redisComponent.get(IcebartechConstants.JSAPI_TICKET_GROUP, siteProperties.getAppName() + default_key);
            if (StringUtils.isBlank(jsapiTicket)) {
                String requestUrl = String.format(WeiXinConstant.JSAPI_TICKET_URL, getAccessTokenFromWeiXin(false));
                String rtnJson = HttpConnectUtil.doGet(requestUrl, WeiXinConstant.DEFAULT_CHARSET);
                jsapiTicket = parseJsapiTicket(rtnJson);
            }
        } catch (Exception e) {
            logger.error("getJsapiTicket return exception, errorMsg:" + e.getMessage(), e);
        }
        return jsapiTicket;
    }

    /**
     * 解析返回的jsapi_ticket
     *
     * @param result 返回的结果json字符串
     * @return
     */
    private String parseJsapiTicket(String result) {
        String rtnTicket = null;
        if (StringUtils.isNotBlank(result)) {
            Map<String, Object> rtnMap = JSON.parseObject(result);
            if (rtnMap != null && rtnMap.size() > 0) {
                Object errorCodeObject = rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM);
                //先判断是否发生错误
                if (errorCodeObject != null && !WeiXinConstant.SUCCESS_CODE.equals(errorCodeObject.toString())) {
                    logger.info("获取jsapi_ticket发生错误，{}，{}", rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM), rtnMap.get(WeiXinConstant.ERROR_MGS_PARAM));
                    return null;
                }
                Object token = rtnMap.get(WeiXinConstant.JSAPI_TICKET_PARAM);
                if (token != null) {
                    rtnTicket = token.toString();
                }
                Object expires = rtnMap.get(WeiXinConstant.EXPIRES_IN_PARAM);
                if (expires != null) {
                    //设置到缓存里面
                    redisComponent.set(IcebartechConstants.JSAPI_TICKET_GROUP, siteProperties.getAppName() + default_key, rtnTicket, Long.parseLong(String.valueOf(expires)));
                }
                logger.info("获取jsapi_ticket成功，值：{}，过期时间（秒）：{}", rtnTicket, expires);
            }
        }
        return rtnTicket;
    }

    /**
     * 解析返回的access_token
     *
     * @param requestUrl
     * @return
     */
    private String parseAccessToken(String requestUrl) {
        String rtnJson = HttpConnectUtil.doGet(requestUrl, WeiXinConstant.DEFAULT_CHARSET);
        String rtnAccessToken = null;
        if (StringUtils.isNotBlank(rtnJson)) {
            Map<String, Object> rtnMap = JSON.parseObject(rtnJson);
            if (rtnMap != null && rtnMap.size() > 0) {
                Object errorCodeObject = rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM);
                //先判断是否发生错误
                if (errorCodeObject != null && !WeiXinConstant.SUCCESS_CODE.equals(errorCodeObject.toString())) {
                    logger.info("获取access_token发生错误，{}，{}", rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM), rtnMap.get(WeiXinConstant.ERROR_MGS_PARAM));
                    return null;
                }

                Object token = rtnMap.get(WeiXinConstant.ACCESS_TOKEN_PARAM);
                if (token != null) {
                    rtnAccessToken = token.toString();
                }
                Object expires = rtnMap.get(WeiXinConstant.EXPIRES_IN_PARAM);
                if (expires != null) {
                    //由于腾讯服务器和自己服务器的时间差，把redis里面的近期时间设置得短一些，这里：7200秒-600秒=6600秒，提前大概10分钟过期
                    long expireSecond = Long.parseLong(String.valueOf(expires)) - 600;
                    //设置到缓存里面
                    redisComponent.set(IcebartechConstants.ACCESS_TOKEN_GROUP, siteProperties.getAppName() + default_key, rtnAccessToken, expireSecond);
                }
                logger.info("获取access_token成功，值：{}，过期时间（秒）：{}", rtnAccessToken, expires);
                return rtnAccessToken;
            }
        }
        return null;
    }

    /**
     * 上传图片、音乐或者视频到微信并返回微信的media_id
     *
     * @param fileUrl
     * @return
     */
    public List<String> uploadFileToWeiXin(List<String> fileUrl, String type) {
        List<String> rtnList = new ArrayList<String>();
        List<File> removeFileList = new ArrayList<File>();
        try {
            for (String url : fileUrl) {
                InputStream in = new URL(url).openStream();
                byte[] fileByte = IOUtils.toByteArray(in);
                String uploadFileName = String.valueOf(System.currentTimeMillis());
                String urlFileName = url.substring(url.lastIndexOf(".") + 1);
                String totalFileName = getCurrentFilePath() + uploadFileName + urlFileName;
                FileUtils.writeByteArrayToFile(new File(totalFileName), fileByte);

                //放到待删除文件的list里面去
                removeFileList.add(new File(totalFileName));

                //下一步上传音乐、图片或者视频到微信
                //获取到access_token
                String access_token = getAccessTokenFromWeiXin(false);
                if (StringUtils.isNotBlank(access_token)) {
                    logger.info("上传媒体文件时获取access_token成功，{}", access_token);
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put(WeiXinConstant.ACCESS_TOKEN_PARAM, access_token);
                    paramsMap.put(WeiXinConstant.TYPE_PARAM, type);

                    String rtnJson = HttpConnectUtil.doFormPost(WeiXinConstant.UPLOAD_FILE_URL, paramsMap, new File(totalFileName), WeiXinConstant.MEDIA_PARAM);
                    if (StringUtils.isNotBlank(rtnJson)) {
                        logger.info("上传媒体文件返回json串：{}", rtnJson);
                        Map<String, Object> rtnJsonMap = JSON.parseObject(rtnJson);
                        if (rtnJsonMap != null && rtnJsonMap.size() > 0) {
                            Object errorCodeObject = rtnJsonMap.get(WeiXinConstant.ERROR_CODE_PARAM);
                            //先判断是否发生错误
                            if (errorCodeObject != null && !WeiXinConstant.SUCCESS_CODE.equals(errorCodeObject.toString())) {
                                logger.info("上传媒体文件出错，{}，{}", rtnJsonMap.get(WeiXinConstant.ERROR_CODE_PARAM), rtnJsonMap.get(WeiXinConstant.ERROR_MGS_PARAM));

                                continue;//继续下一个文件上传
                            }

                            Object mediaIdObject = rtnJsonMap.get(WeiXinConstant.MEDIA_ID_PARAM);
                            if (mediaIdObject != null) {
                                logger.info("上传文件成功，{}", url);
                                rtnList.add(mediaIdObject.toString());
                            }
                        }
                    }
                } else {
                    logger.info("上传媒体文件时获取access_token失败");
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            //如果抛异常，那么可能解析文件名或者上传失败，那么方法处理失败
            logger.error("uploadFileToWeiXin return exception, errorMsg:" + e.getMessage(), e);
            e.printStackTrace();
        }
        //删除掉此次的文件
        removeFileAtTempDirectory(removeFileList);
        return rtnList;
    }

    /**
     * 删除掉指定文件名的文件
     *
     * @param files
     */
    public void removeFileAtTempDirectory(List<File> files) {
        if (files != null && files.size() > 0) {
            for (File file : files) {
                if (file.exists()) {
                    //如果文件存在，那么删除该文件
                    if (file.delete()) continue;
                }
            }
        }
    }

    /**
     * 创建微信服务号的自定义菜单
     *
     * @return
     */
    public boolean createMenu(String json) {
        boolean rtnFlag = false;
        try {
            //获取到access_token
            String access_token = getAccessTokenFromWeiXin(false);
            if (StringUtils.isNotBlank(json)) {

                String rtnJson = HttpConnectUtil.doBodyPost(String.format(WeiXinConstant.MENU_CREATE_URL, access_token), null, json);
                if (StringUtils.isNotBlank(rtnJson)) {
                    logger.info("创建自定义菜单返回json串：{}", rtnJson);
                    Map<String, Object> rtnJsonMap = JSON.parseObject(rtnJson);
                    if (rtnJsonMap != null && rtnJsonMap.size() > 0) {
                        Object errorCodeObject = rtnJsonMap.get(WeiXinConstant.ERROR_CODE_PARAM);
                        //先判断是否发生错误
                        if (errorCodeObject != null && !WeiXinConstant.SUCCESS_CODE.equals(errorCodeObject.toString())) {
                            logger.info("创建自定义菜单出错，{}，{}", rtnJsonMap.get(WeiXinConstant.ERROR_CODE_PARAM), rtnJsonMap.get(WeiXinConstant.ERROR_MGS_PARAM));
                            return rtnFlag;
                        }
                        String errorCode = null == errorCodeObject ? null : errorCodeObject.toString();
                        if (StringUtils.isNotBlank(errorCode) && WeiXinConstant.SUCCESS_CODE.equals(errorCode)) {
                            logger.info("创建自定义菜单成功，{}", errorCode);
                            rtnFlag = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("创建自定义菜单出错, errorMsg:" + e.getMessage(), e);
        }
        return rtnFlag;
    }

    /**
     * 获取自定义菜单json串
     *
     * @return
     */
    public String getMenu() {
        String rtnJson = null;
        try {
            String access_token = getAccessTokenFromWeiXin(false);
            if (StringUtils.isNotBlank(access_token)) {
                String responseStr = HttpConnectUtil.doBodyPost(String.format(WeiXinConstant.MENU_GET_URL, access_token), null, null);
                if (StringUtils.isNotBlank(responseStr)) {
                    rtnJson = responseStr;
                } else {
                    logger.error("获取自定义菜单json串失败");
                }
            }
        } catch (Exception e) {
            logger.error("获取自定义菜单出错,errorMsg:" + e.getMessage(), e);
        }
        return rtnJson;
    }

    /**
     * 删除自定义菜单结构
     *
     * @return
     */
    public boolean deleteMenu() {
        boolean rtnFlag = false;
        try {
            String access_token = getAccessTokenFromWeiXin(false);
            if (StringUtils.isNotBlank(access_token)) {
                String responseStr = HttpConnectUtil.doBodyPost(String.format(WeiXinConstant.MENU_DELETE_URL, access_token), null, null);
                if (StringUtils.isNotBlank(responseStr)) {
                    Map<String, Object> rtnMap = JSON.parseObject(responseStr);
                    if (rtnMap != null && rtnMap.size() > 0) {
                        Object errorCodeObject = rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM);
                        if (errorCodeObject != null && WeiXinConstant.SUCCESS_CODE.equals(errorCodeObject.toString())) {
                            rtnFlag = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("删除自定义菜单结构失败,errorMsg:" + e.getMessage(), e);
            e.printStackTrace();
        }
        return rtnFlag;
    }

    /**
     * 调用客服接口来向微信用户发送消息
     *
     * @param content
     * @return
     */
    public boolean sendCustomerMsg(String content) {
        boolean rtnFlag = false;
        if (StringUtils.isNotBlank(content)) {
            //获取到access_token
            String access_token = getAccessTokenFromWeiXin(false);
            if (StringUtils.isNotBlank(access_token)) {
                String rtnJson = HttpConnectUtil.doBodyPost(String.format(WeiXinConstant.CUSTOMER_SEND, access_token), null, content);
                logger.info("调用微信客服接口发消息返回：{}", rtnJson);
                if (StringUtils.isNotBlank(rtnJson)) {
                    Map<String, Object> jsonMap = JSON.parseObject(rtnJson);
                    if (jsonMap != null && jsonMap.get(WeiXinConstant.ERROR_CODE_PARAM) != null && Integer.parseInt(jsonMap.get(WeiXinConstant.ERROR_CODE_PARAM).toString()) == 0) {
                        logger.info("调用微信客服接口发消息返回成功，{}，{}", jsonMap.get(WeiXinConstant.ERROR_CODE_PARAM), jsonMap.get(WeiXinConstant.ERROR_MGS_PARAM));
                        rtnFlag = true;
                    } else if (jsonMap != null && jsonMap.get(WeiXinConstant.ERROR_CODE_PARAM) != null && jsonMap.get(WeiXinConstant.ERROR_CODE_PARAM).toString().equals("40001")) {
                        getAccessTokenFromWeiXin(true);
                        logger.info("sendCustomerMsg refresh token at :{}", LocalDateTime.now());
                        return sendCustomerMsg(content);
                    }
                }
            }
        }

        return rtnFlag;
    }

    /**
     * 发送模板消息给微信用户
     *
     * @param postJson post消息体
     * @return
     */
    public boolean sendTemplateMessage(String postJson) {
        boolean rtnFlag = false;
        if (StringUtils.isNotEmpty(postJson)) {
            //获取到access_token
            String access_token = getAccessTokenFromWeiXin(false);
            if (StringUtils.isNotBlank(access_token)) {
                String rtnJson = HttpConnectUtil.doJdkBodyPost(String.format(WeiXinConstant.MP_MESSAGE_TEMPLATE_URL, access_token), postJson);
                logger.info("调用微信模板消息接口发消息返回：{}", rtnJson);
                if (StringUtils.isNotBlank(rtnJson)) {
                    Map<String, Object> jsonMap = JSON.parseObject(rtnJson);
                    if (jsonMap != null && jsonMap.get(WeiXinConstant.ERROR_CODE_PARAM) != null && Integer.parseInt(jsonMap.get(WeiXinConstant.ERROR_CODE_PARAM).toString()) == 0) {
                        logger.info("调用微信模板消息接口发消息返回成功，{}，{}", jsonMap.get(WeiXinConstant.ERROR_CODE_PARAM), jsonMap.get(WeiXinConstant.ERROR_MGS_PARAM));
                        rtnFlag = true;
                    } else if (jsonMap != null && jsonMap.get(WeiXinConstant.ERROR_CODE_PARAM) != null && jsonMap.get(WeiXinConstant.ERROR_CODE_PARAM).toString().equals("40001")) {
                        getAccessTokenFromWeiXin(true);
                        logger.info("sendTemplateMessage refresh token at :{}", LocalDateTime.now());
                        return sendTemplateMessage(postJson);
                    } else {
                        logger.error("调用微信模板消息接口发消息返回失败，{}", jsonMap);
                    }
                }
            }
        }
        return rtnFlag;
    }

    /**
     * 获取微信公众号已关注的用户openId列表
     *
     * @param nextOpenId
     * @return
     */
    public List<String> getUsers(String nextOpenId) {
        List<String> rtnList = new ArrayList<>();
        String next = StringUtils.isNotEmpty(nextOpenId) ? nextOpenId : "";
        //获取到access_token
        String access_token = getAccessTokenFromWeiXin(false);
        String requestUrl = String.format(WeiXinConstant.USER_GET, access_token, next);
        String rtnJson = HttpConnectUtil.doGet(requestUrl, WeiXinConstant.DEFAULT_CHARSET);
        logger.info("getUsers rtnJson:" + rtnJson);
        if (StringUtils.isNotBlank(rtnJson)) {
            Map<String, Object> rtnMap = JSON.parseObject(rtnJson);
            if (rtnMap != null && rtnMap.size() > 0) {
                if (rtnMap.get("count") != null) {
                    Long count = Long.valueOf(rtnMap.get("count").toString());
                    if (count > 0) {
                        Map<String, Object> dataMap = (Map<String, Object>) rtnMap.get("data");
                        if (dataMap != null) {
                            rtnList = (List<String>) dataMap.get("openid");
                        }
                    }
                    if (rtnMap.get("next_openid") != null && count > 0) {
                        String nextStr = rtnMap.get("next_openid").toString();
                        List<String> nextList = getUsers(nextStr);
                        rtnList.addAll(nextList);
                    }
                } else if (rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM) != null && rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM).toString().equals("40001")) {
                    getAccessTokenFromWeiXin(true);
                    logger.info("getUsers_ss refresh token at :{}", LocalDateTime.now());
                    return getUsers(nextOpenId);
                }
            }
        }
        return rtnList;
    }

    /**
     * 根据openId获取对应的微信用户基本信息
     *
     * @param openId 微信openId
     * @return
     */
    public WeiXinMpUserInfo getUserInfo(String openId) {
        //获取到access_token
        String access_token = getAccessTokenFromWeiXin(false);
        String requestUrl = String.format(WeiXinConstant.USER_INFO_CGI, access_token, openId);
        String rtnJson = HttpConnectUtil.doGet(requestUrl, WeiXinConstant.DEFAULT_CHARSET);
        logger.info("getUserInfo_ss rtnJson:" + rtnJson);
        if (StringUtils.isNotBlank(rtnJson)) {
            Map<String, Object> rtnMap = JSON.parseObject(rtnJson);
            if (rtnMap != null && rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM) == null) {
                //String openId = rtnMap.is("openId").toString();
                WeiXinMpUserInfo userInfo = JSON.parseObject(rtnJson, WeiXinMpUserInfo.class);
                logger.info("userInfoJson:{}", JSON.toJSONString(userInfo));
                return userInfo;
            } else if (null != rtnMap && rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM) != null && rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM).toString().equals("40001")) {
                getAccessTokenFromWeiXin(true);
                logger.info("getUserInfo refresh token at :{}", LocalDateTime.now());
                return getUserInfo(openId);
            }
        }
        return null;
    }

    /**
     * 批量获取微信已关注用户基本信息
     *
     * @param openIdList openId列表
     * @return
     */
    public List<WeiXinMpUserInfo> getUserInfoBatch(List<String> openIdList) {
        List<WeiXinMpUserInfo> rtnList = new ArrayList<>();
        List<Map<String, Object>> userList = new ArrayList<>();
        String access_token = getAccessTokenFromWeiXin(false);

        if (CollectionUtils.isNotEmpty(openIdList)) {
            for (String openId : openIdList) {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("openid", openId);
                tempMap.put("lang", "zh-CN");
                userList.add(tempMap);
            }
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("user_list", userList);
            String rtnJson = HttpConnectUtil.doJdkBodyPost(String.format(WeiXinConstant.BATCH_USER_GET, access_token), JSON.toJSONString(dataMap));
            logger.info("BATCH_USER_GET return：{}", rtnJson);
            if (StringUtils.isNotBlank(rtnJson)) {
                Map<String, Object> rtnMap = JSON.parseObject(rtnJson);
                if (rtnMap != null && rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM) == null) {
                    List<Map<String, Object>> jsonList = (List<Map<String, Object>>) rtnMap.get("user_info_list");
                    if (CollectionUtils.isNotEmpty(jsonList)) {
                        rtnList = JSON.parseArray(JSON.toJSONString(jsonList), WeiXinMpUserInfo.class);
                    }
                } else if (null != rtnMap && rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM) != null && rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM).toString().equals("40001")) {
                    getAccessTokenFromWeiXin(true);
                    return getUserInfoBatch(openIdList);
                }
            }
        }
        return rtnList;
    }


    private String getCurrentFilePath() {
        String FILE_TEMP_PATH = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        if (StringUtils.isNotBlank(FILE_TEMP_PATH)) {
            FILE_TEMP_PATH = FILE_TEMP_PATH.substring(1, FILE_TEMP_PATH.length() - 8);//去掉开头的"/"和末尾的"classes/"
            try {
                FILE_TEMP_PATH = URLDecoder.decode(FILE_TEMP_PATH, WeiXinConstant.DEFAULT_CHARSET);
                FILE_TEMP_PATH += WeiXinConstant.FILE_UPLOAD;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return FILE_TEMP_PATH;
    }
}
