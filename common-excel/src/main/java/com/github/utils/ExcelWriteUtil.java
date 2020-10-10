package com.github.utils;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author Created by liuao on 2019/6/27.
 * @desc
 */
public class ExcelWriteUtil {


    /**
     * 把Excel写出客户端
     * @param response 相应体
     * @param workbook 文件
     * @param name 表名
     * @throws IOException io异常
     */
    public static void write(HttpServletResponse response, Workbook workbook, String name) throws IOException {
        OutputStream outstream = response.getOutputStream();
        /**这段代码可以实现Excel以文件流的形式到浏览器中,浏览器左下角出现Excel的下载提示，
         *new String(fileName.getBytes(),"iso-8859-1")可是实现文件名为中文，不是乱码  */
        response.reset();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(name , "UTF-8") + ".xls");
        workbook.write(outstream);
        outstream.close();

    }

}
