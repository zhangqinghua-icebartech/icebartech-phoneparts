package com.icebartech.core.utils;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * java调用shell脚本工具类
 *
 * @author wenhsh
 */
@Slf4j
public class ShellKit {

    /**
     * 运行shell脚本，适合于一般的脚本
     *
     * @param shell 需要运行的shell脚本
     */
    public static String execShell(String shell) {
        String result = null;
        try {
            Process ps = Runtime.getRuntime().exec(shell);
            ps.waitFor();

            @Cleanup BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            log.error("Util.execShell exception", e);
        }
        return result;
    }


    /**
     * 运行shell，适合于脚本中有awk命令
     *
     * @param shStr 需要执行的shell
     * @return
     * @throws IOException 注:如果sh中含有awk,一定要按new String[]{"/bin/sh","-c",shStr}写,才可以获得流.
     */
    public static List<String> execShellAwk(String shStr) throws Exception {
        List<String> strList = new ArrayList<String>();

        Process process;
        process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", shStr}, null, null);
        @Cleanup InputStreamReader ir = new InputStreamReader(process
                .getInputStream(), "UTF-8");
        @Cleanup LineNumberReader input = new LineNumberReader(ir);
        String line;
        process.waitFor();
        while ((line = input.readLine()) != null) {
            strList.add(line);
        }
        return strList;
    }
}