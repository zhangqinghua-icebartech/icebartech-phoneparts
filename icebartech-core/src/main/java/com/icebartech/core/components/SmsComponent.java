package com.icebartech.core.components;

import com.icebartech.core.properties.MailProperties;
import com.icebartech.core.properties.SmsProperties;
import com.icebartech.core.utils.UTF8PostMethod;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Created by liuao on 2020/8/6 0006$.
 * @desc
 */
@Component
public class SmsComponent {

    @Autowired
    private SmsProperties smsProperties;

    public void send(String phone,String code){
        try {
            HttpClient client = new HttpClient();
            JSONObject codeTemplate = new JSONObject();
            codeTemplate.put("code",code);
            codeTemplate.put("time","10分钟");
            UTF8PostMethod post = new UTF8PostMethod(smsProperties.getUrl().get(0));
            post.addParameter("appid", smsProperties.getAppId().get(0));
            post.addParameter("to", phone);
            post.addParameter("project", smsProperties.getProjectCodes().get(0));
            post.addParameter("vars", codeTemplate.toString());
            post.addParameter("signature", smsProperties.getAppKey().get(0));
            client.executeMethod(post);
            InputStream in = post.getResponseBodyAsStream();
            BufferedReader re = new BufferedReader(new InputStreamReader(in));
            String line = "";
            StringBuilder res = new StringBuilder();
            while ((line = re.readLine()) != null) {
                res.append(line);
            }
            System.out.println("##########" + res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
