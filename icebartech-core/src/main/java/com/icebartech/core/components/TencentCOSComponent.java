package com.icebartech.core.components;

import com.icebartech.core.properties.QCloudProperties;
import com.icebartech.core.properties.SiteProperties;
import com.icebartech.core.utils.IdGenerator;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.region.Region;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云oss操作组件类
 *
 * @author wenhsh
 */
@Component
public class TencentCOSComponent {

    @Autowired
    private QCloudProperties qCloudProperties;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SiteProperties siteProperties;

    private COSClient getClient() {
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(qCloudProperties.getSecretId(), qCloudProperties.getSecretKey());
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(qCloudProperties.getRegion()));
        // 3 生成cos客户端
        COSClient client = new COSClient(cred, clientConfig);
        return client;
    }

    private void closeClient(COSClient client) {
        if (null != client) {
            client.shutdown();
        }
    }

    private String getOSSUrl() {
        String url = qCloudProperties.getCosUrl();
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    /**
     * 生成预签名的上传链接, 可直接分发给客户端进行文件的上传
     *
     * @return url
     */
    public Map<String, String> generateUploadUrl(String suffix) {
        Map<String, String> rtnMap = new HashMap<>();
        // bucket 的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = qCloudProperties.getBucketName() + "-" + qCloudProperties.getAppid();

        String fileKey = siteProperties.getAppName() + "/" + idGenerator.getFileKey(suffix);
        COSClient client = getClient();
        try {
            // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(5分钟)
            // 这里设置签名在半个小时后过期
            Date expirationTime = DateUtils.addMinutes(new Date(), 30);
            URL url = client.generatePresignedUrl(bucketName, fileKey, expirationTime, HttpMethodName.PUT);
            rtnMap.put("fileKey", fileKey);
            rtnMap.put("uploadUrl", getOSSUrl() + url.getFile());
            rtnMap.put("downloadUrl", generateDownloadUrl(fileKey));
            return rtnMap;
        } finally {
            closeClient(client);
        }
    }

    /**
     * 生成文件下载链接
     *
     * @param fileKey 文件ID
     * @param fileKey
     * @return
     */
    public String generateDownloadUrl(String fileKey) {
        // 生成一个下载链接
        // bucket 的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = qCloudProperties.getBucketName() + "-" + qCloudProperties.getAppid();
        String key = fileKey;
        // return "https://" + bucketName + ".file.myqcloud.com/" + fileKey;
        COSClient cosClient = getClient();
        try {
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, fileKey, HttpMethodName.GET);
            // 设置签名过期时间(可选), 若未进行设置, 则默认使用 ClientConfig 中的签名过期时间(5分钟)
            // 这里设置签名在半个小时后过期
            Date expirationDate = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
            req.setExpiration(expirationDate);
            URL downloadUrl = cosClient.generatePresignedUrl(req);
            return getOSSUrl() + downloadUrl.getFile();
        } finally {
            closeClient(cosClient);
        }
    }

}
