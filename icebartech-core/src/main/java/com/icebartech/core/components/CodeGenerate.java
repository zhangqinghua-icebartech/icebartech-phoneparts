package com.icebartech.core.components;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anler
 * @Date 2018/12/28 14:06
 * @Description
 */
public class CodeGenerate {

    // public static void main(String[] args) {
    //     String outPath = "dreamlot-api/src/main/java/template";
    //     String packageName = "com.icebartech.dreamlot.ai.po";
    //
    //     List<Class<?>> clazzs = PackageUtil.getClass(packageName, false);
    //     for (Class clazz : clazzs) {
    //         System.out.println(clazz.getSimpleName());
    //         Table annotation = (Table) clazz.getAnnotation(Table.class);
    //         String description = null == annotation ? "" : annotation.comment();
    //         // System.out.println(annotation.comment());
    //         new CodeGenerate().generate(outPath, packageName, clazz.getSimpleName(), description);
    //     }
    // }

    private static volatile Configuration configuration;

    private static Configuration getConf() {
        if (null == configuration) {
            synchronized (Configuration.class) {
                if (null == configuration) {
                    configuration = new Configuration(new Version("2.3.0"));
                    // 获取模版路径
                    TemplateLoader ldr = new SpringTemplateLoader(new DefaultResourceLoader(), "/ftls");
                    configuration.setTemplateLoader(ldr);
                }
            }
        }
        return configuration;
    }

    public void generate(String outPath, String packagePath, String className, String description) {
        // 创建数据模型
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("outPath", outPath);
        dataMap.put("packagePath", packagePath);
        dataMap.put("className", className);
        dataMap.put("description", description);
        dataMap.put("author", System.getProperty("user.name"));
        dataMap.put("date", LocalDateTime.now());

        //生成Repository文件
        generate(dataMap, "repository", "Repository");
        //生成DTO文件
        generate(dataMap, "dto", "DTO");
        //生成服务层接口文件
        generate(dataMap, "service", "Service");
        //生成服务实现层文件
        generate(dataMap, "service/impl", "ServiceImpl");
    }

    private void generate(Map<String, Object> dataMap, String path, String fileName) {
        // 加载模版文件
        Template template;
        Writer out = null;
        try {
            template = getConf().getTemplate(fileName + ".ftl");
            File outFile = new File(dataMap.get("outPath") + "/" + path + "/" + dataMap.get("className") + fileName + ".java");
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            if (outFile.exists()) {
                outFile.delete();
            }
            outFile.createNewFile();
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outFile.getPath()))));
            // 输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}