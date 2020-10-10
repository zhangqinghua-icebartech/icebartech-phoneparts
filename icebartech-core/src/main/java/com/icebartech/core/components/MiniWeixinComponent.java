package com.icebartech.core.components;

import com.alibaba.fastjson.JSON;
import com.icebartech.core.constants.IcebartechConstants;
import com.icebartech.core.constants.WeiXinConstant;
import com.icebartech.core.properties.SiteProperties;
import com.icebartech.core.properties.WeixinMiniProperties;
import com.icebartech.core.utils.HttpConnectUtil;
import com.icebartech.core.vo.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 接入微信平台处理工具类
 *
 * @author haosheng.wenhs
 */
@Component
@Slf4j
public class MiniWeixinComponent {


    @Autowired
    private WeixinMiniProperties weixinMiniProperties;

    @Autowired
    private SiteProperties siteProperties;

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private AliyunOSSComponent aliyunOSSComponent;


    /**
     * 从微信处获取access_token
     *
     * @param fresh
     * @return
     */
    public String getAccessTokenFromWeiXin(boolean fresh) {
        String accessToken = null;
        try {
            //先从缓存里面取访问的accessToke
            accessToken = (String) redisComponent.get(IcebartechConstants.ACCESS_TOKEN_GROUP, getDefaultKey());

            if (StringUtils.isBlank(accessToken) || fresh) {
                String requestUrl = String.format(WeiXinConstant.ACCESS_TOKEN_URL, weixinMiniProperties.getAppId(), weixinMiniProperties.getAppSecret());
                accessToken = parseAccessToken(requestUrl);
            }
            if (fresh) {
                log.info("getAccessTokenFromWeiXin refresh token at :{}", LocalDateTime.now());
            }
        } catch (Exception e) {
            log.error("getAccessTokenFromWeiXin return exception, errorMsg:" + e.getMessage(), e);
        }
        return accessToken;
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
                    log.info("获取access_token发生错误，{}，{}", rtnMap.get(WeiXinConstant.ERROR_CODE_PARAM), rtnMap.get(WeiXinConstant.ERROR_MGS_PARAM));
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
                    redisComponent.set(IcebartechConstants.ACCESS_TOKEN_GROUP, getDefaultKey(), rtnAccessToken, expireSecond);
                }
                log.info("获取access_token成功，值：{}，过期时间（秒）：{}", rtnAccessToken, expires);
                return rtnAccessToken;
            }
        }
        return null;
    }


    /**
     * 生成小程序码并上传到阿里云
     *
     * @param url
     * @return
     */
    public FileInfo downloadQrCodePhotoFromMini(String url, int width) {
        String accessToken = getAccessTokenFromWeiXin(false);
        FileInfo fileInfo = null;
        String requestUrl = String.format(WeiXinConstant.GET_WX_ACODE, accessToken);
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("path", url);
        bodyMap.put("width", width);
        bodyMap.put("auto_color", false);
        bodyMap.put("line_color", new HashMap<>());

        PostMethod postMethod = new PostMethod(requestUrl);
        try {
            postMethod.setRequestEntity(new StringRequestEntity(JSON.toJSONString(bodyMap), "application/json", "UTF-8"));
            HttpClient httpClient = new HttpClient();
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(50000);
            httpClient.getHttpConnectionManager().getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            int status = httpClient.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                InputStream is = postMethod.getResponseBodyAsStream();
                long contentLength = postMethod.getResponseContentLength();
                byte[] data = new byte[(int) contentLength];
                byte[] tempData = new byte[1024];
                int len = 0;
                int startIndex = 0;
                while ((len = is.read(tempData)) != -1) {
                    System.arraycopy(tempData, 0, data, startIndex, len);
                    startIndex += len;
                }
                //把图片上传到阿里去
                String fileKey = aliyunOSSComponent.uploadFile(data, "jpg", null);
                if (StringUtils.isNotBlank(fileKey)) {
                    log.info("uploadAttachmentFile success, fileKey:" + fileKey);
                    fileInfo = new FileInfo();
                    fileInfo.setFileKey(fileKey);
                    fileInfo.setFileName(fileKey);
                    String fileUrl = aliyunOSSComponent.generateDownloadUrl(fileKey);
                    fileInfo.setFileUrl(fileUrl);
                } else {
                    log.error("uploadAttachmentFile update failed, please check!");
                }
            }
        } catch (Exception e) {
            log.error("downloadQrCodePhotoFromMini return exception, errorMsg:" + e.getMessage(), e);
        }
        return fileInfo;
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

    private String getDefaultKey() {
        return siteProperties.getAppName() + ":miniweixin";
    }
}
