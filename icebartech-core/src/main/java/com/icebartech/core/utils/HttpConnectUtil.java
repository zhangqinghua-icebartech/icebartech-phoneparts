package com.icebartech.core.utils;


import com.alibaba.fastjson.JSON;
import lombok.Cleanup;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Http请求工具类
 *
 * @author haosheng.wenhs
 * @version 创建时间：2011-3-9 下午01:17:44
 * 类说明
 */
public class HttpConnectUtil {
    /**
     * 连接超时
     */
    private static int connectTimeOut = 50000;

    /**
     * 读取数据超时
     */
    private static int readTimeOut = 100000;

    /**
     * 请求编码
     */
    private static String requestEncoding = "UTF-8";

    private static Logger logger = LoggerFactory.getLogger(HttpConnectUtil.class);

    /**
     * 代理连接
     *
     * @param urlvalue
     * @param params
     * @param transway
     * @return
     */
    public static String proxyConnect(String urlvalue, Map<?, ?> params, String transway) {
        String paramvalue = JSON.toJSONString(params);
        Map<String, String> m = new HashMap<String, String>();
        m.put("urlvalue", urlvalue);
        m.put("paramjson", paramvalue);
        String returnDate = null;
        if (transway == null || transway.toLowerCase().equals("post")) {
            returnDate = doPost(urlvalue, m, "UTF-8");
        } else {
            returnDate = doGet(urlvalue, m, "UTF-8");
        }
        return returnDate;
    }

    /**
     * <pre>
     * 发送带参数的GET的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    @SuppressWarnings("rawtypes")
    public static String doGet(String reqUrl, Map parameters,
                               String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter
                    .hasNext(); ) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(),
                        HttpConnectUtil.requestEncoding));
                params.append("&");
            }

            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");

            url_con.setConnectTimeout(50000);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(50000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes("UTF-8");
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            logger.error("网络故障", e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }

        return responseContent;
    }

    /**
     * 发送get请求，请求参数在方法中未做encode处理
     *
     * @param reqUrl   请求url
     * @param reEncode 返回编码 默认为GBK
     * @return
     */
    public static String get(String reqUrl, String reEncode) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        reEncode = (reEncode == null || reEncode.trim().equals("")) ? requestEncoding : reEncode;
        try {
            StringBuffer params = new StringBuffer();
            String queryUrl = reqUrl;
            int paramIndex = reqUrl.indexOf("?");

            if (paramIndex > 0) {
                queryUrl = reqUrl.substring(0, paramIndex);
                String parameters = reqUrl.substring(paramIndex + 1, reqUrl
                        .length());
                String[] paramArray = parameters.split("&");
                for (int i = 0; i < paramArray.length; i++) {
                    String string = paramArray[i];
                    int index = string.indexOf("=");
                    if (index > 0) {
                        String parameter = string.substring(0, index);
                        String value = string.substring(index + 1, string
                                .length());
                        params.append(parameter);
                        params.append("=");
                        params.append(value);
                        params.append("&");
                    }
                }

                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(queryUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            url_con.setConnectTimeout(50000);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(50000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes("UTF-8");
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, reEncode));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            logger.error("网络故障", e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }

        return responseContent;
    }

    /**
     * <pre>
     * 发送不带参数的GET的HTTP请求
     * </pre>
     *
     * @param reqUrl HTTP请求URL
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            String queryUrl = reqUrl;
            int paramIndex = reqUrl.indexOf("?");

            if (paramIndex > 0) {
                queryUrl = reqUrl.substring(0, paramIndex);
                String parameters = reqUrl.substring(paramIndex + 1, reqUrl
                        .length());
                String[] paramArray = parameters.split("&");
                for (int i = 0; i < paramArray.length; i++) {
                    String string = paramArray[i];
                    int index = string.indexOf("=");
                    if (index > 0) {
                        String parameter = string.substring(0, index);
                        String value = string.substring(index + 1, string
                                .length());
                        params.append(parameter);
                        params.append("=");
                        params.append(URLEncoder.encode(value,
                                HttpConnectUtil.requestEncoding));
                        params.append("&");
                    }
                }

                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(queryUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            url_con.setConnectTimeout(50000);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(50000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes("UTF-8");
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            logger.error("网络故障", e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }

        return responseContent;
    }

    /**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     *
     * @param reqUrl     HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    @SuppressWarnings("rawtypes")
    public static String doPost(String reqUrl, Map parameters, String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext(); ) {
                Entry element = (Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(),
                        HttpConnectUtil.requestEncoding));
                params.append("&");
            }

            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            url_con.setConnectTimeout(50000);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            url_con.setReadTimeout(50000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes("UTF-8");
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            logger.error("网络故障", e);
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;
    }

    /**
     * post请求，仅限于post json流
     *
     * @param strURL url
     * @param params json流
     * @return
     */
    public static String doJdkBodyPost(String strURL, String params) {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(strURL);// 创建连接
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.connect();
            if (org.apache.commons.lang.StringUtils.isNotBlank(params)) {
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
                out.append(params);
                out.flush();
                out.close();
            }

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "UTF-8");
                sb.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
            return sb.toString();

        } catch (IOException e) {
            logger.error("doJdkBodypost return exception, errorMsg:" + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null; // 自定义错误信息
    }

    /**
     * 拥有参数和body体的post请求方法
     *
     * @param requestUrl
     * @param params
     * @param bodyStr
     * @return
     */
    public static String doBodyPost(String requestUrl, Map<String, String> params, String bodyStr) {
        String rtnStr = null;
        PostMethod postMethod = new PostMethod(requestUrl);
        try {
            //设置请求头
            if (params != null && params.size() > 0) {
                Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, String> entry = iterator.next();
                    postMethod.setParameter(entry.getKey(), entry.getValue());
                }
            }
            if (StringUtils.isNotBlank(bodyStr)) {
                postMethod.setRequestEntity(new StringRequestEntity(bodyStr, "application/json", "UTF-8"));
            }
            HttpClient httpClient = new HttpClient();
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeOut);
            httpClient.getHttpConnectionManager().getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            int status = httpClient.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(), "UTF-8"));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                rtnStr = new String(stringBuffer.toString().getBytes("UTF-8"), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnStr;
    }

    /**
     * 模拟form表单请求，比如包含文件与其他参数值
     *
     * @param requestUrl
     * @param params
     * @return
     */
    public static String doFormPost(String requestUrl, Map<String, String> params, File file, String fileName) {
        String rtnStr = null;
        PostMethod postMethod = new PostMethod(requestUrl);
        try {
            if (params != null && params.size() > 0) {
                Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, String> entry = iterator.next();
                    postMethod.setParameter(entry.getKey(), entry.getValue());
                }
            }
            Part[] parts = {new FilePart(fileName, file)};
            postMethod.setRequestEntity(new MultipartRequestEntity(parts, postMethod.getParams()));
            HttpClient httpClient = new HttpClient();
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeOut);
            httpClient.getHttpConnectionManager().getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            int status = httpClient.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(), "UTF-8"));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                rtnStr = new String(stringBuffer.toString().getBytes("UTF-8"), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnStr;
    }

    /**
     * 发送带证书的ssl请求
     */
    @SuppressWarnings("deprecation")
    public static String sslPost(String url, String data, File file, String password) {
        StringBuffer message = new StringBuffer();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            @Cleanup FileInputStream instream = new FileInputStream(file);
            keyStore.load(instream, password.toCharArray());
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, password.toCharArray())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            HttpPost httpost = new HttpPost(url);

            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//	            httpost.addHeader("Host", "api.mch.weixin.qq.com");  
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));

            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    @Cleanup BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        message.append(text);
                    }
                }
                EntityUtils.consume(entity);
            } catch (IOException e) {
                logger.error("httpclient ssl execute return exception, errorMsg:" + e.getMessage(), e);
            } finally {
                response.close();
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e1) {
            logger.error("httpclient ssl execute return exception, errorMsg:" + e1.getMessage(), e1);
        }
        return message.toString();
    }

    /**
     * 拥有参数和body体的post请求方法
     *
     * @param requestUrl
     * @param params
     * @param bodyStr
     * @return
     */
    public static String doBodyPostXml(String requestUrl, Map<String, String> params, String bodyStr) {
        String rtnStr = null;
        PostMethod postMethod = new PostMethod(requestUrl);
        try {
            //设置请求头
            if (params != null && params.size() > 0) {
                Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<String, String> entry = iterator.next();
                    postMethod.setParameter(entry.getKey(), entry.getValue());
                }
            }
            if (StringUtils.isNotBlank(bodyStr)) {
                postMethod.setRequestEntity(new StringRequestEntity(bodyStr, "text/xml", "UTF-8"));
            }
            HttpClient httpClient = new HttpClient();
            httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeOut);
            httpClient.getHttpConnectionManager().getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            int status = httpClient.executeMethod(postMethod);
            if (status == HttpStatus.SC_OK) {
                @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(), "UTF-8"));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while ((str = reader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                rtnStr = stringBuffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnStr;
    }

    /**
     * @return 连接超时(毫秒)
     * @see com.icebartech.core.utils
     */
    public static int getConnectTimeOut() {
        return HttpConnectUtil.connectTimeOut;
    }

    /**
     * @return 读取数据超时(毫秒)
     * @see com.icebartech.core.utils
     */
    public static int getReadTimeOut() {
        return HttpConnectUtil.readTimeOut;
    }

    /**
     * @return 请求编码
     * @see com.icebartech.core.utils
     */
    public static String getRequestEncoding() {
        return requestEncoding;
    }

    /**
     * @param connectTimeOut 连接超时(毫秒)
     * @see com.icebartech.core.utils
     */
    public static void setConnectTimeOut(int connectTimeOut) {
        HttpConnectUtil.connectTimeOut = connectTimeOut;
    }

    /**
     * @param readTimeOut 读取数据超时(毫秒)
     * @see com.icebartech.core.utils
     */
    public static void setReadTimeOut(int readTimeOut) {
        HttpConnectUtil.readTimeOut = readTimeOut;
    }

    /**
     * @param requestEncoding 请求编码
     * @see com.icebartech.core.utils
     */
    public static void setRequestEncoding(String requestEncoding) {
        HttpConnectUtil.requestEncoding = requestEncoding;
    }
}
 