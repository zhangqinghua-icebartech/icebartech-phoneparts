package com.icebartech.phoneparts.util;

import com.icebartech.core.components.CodeGenerate;
import com.icebartech.core.po.BasePo;
import com.icebartech.phoneparts.agent.po.AddUseRecord;
import com.icebartech.phoneparts.company.po.CoverInfo;
import com.icebartech.phoneparts.system.po.SysSerialClass;
import org.hibernate.annotations.Table;

import java.util.Arrays;
import java.util.List;

/**
 * @author Anler
 * @Date 2018/12/28 16:58
 * @Description
 */
public class CodeGenerateMain {

    public static void main(String[] args) {
        String outPath = "icebartech-starter/src/main/java/com/icebartech/phoneparts/company";
        String packageName = "com.icebartech.phoneparts.company.po";

        // 自定义要创建的类
        List<Class<? extends BasePo>> classes = Arrays.asList(CoverInfo.class);

        // 根据包下所有的PO类创建
        // List<Class<?>> classes = PackageUtil.getClass(packageName, false);
        for (Class clazz : classes) {
            System.out.println(clazz.getSimpleName());
            Table annotation = (Table) clazz.getAnnotation(Table.class);
            String description = null == annotation ? "" : annotation.comment();
            new CodeGenerate().generate(outPath, packageName, clazz.getSimpleName(), description);
        }
    }
}
