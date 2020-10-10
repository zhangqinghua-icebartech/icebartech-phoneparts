package com.icebartech.core.components;

import com.icebartech.core.properties.MailProperties;
import com.icebartech.core.utils.UTF8PostMethod;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Created by liuao on 2019/7/2.
 * @desc
 */
@Component
public class MailComponent {

    private MailProperties mailProperties;

    @Autowired
    public MailComponent(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    public void send(String mail,String code){
        try {
            HttpClient client = new HttpClient();
            JSONObject codeTemplate = new JSONObject();
            codeTemplate.put("code",code);
            UTF8PostMethod post = new UTF8PostMethod(mailProperties.getUrl());
            post.addParameter("appid", mailProperties.getAppid());
            post.addParameter("to", mail);
            post.addParameter("project", mailProperties.getTemplates().get(0));
            post.addParameter("vars", codeTemplate.toString());
            post.addParameter("signature", mailProperties.getAppKey());
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
