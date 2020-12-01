package com.icebartech.excel;

import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.core.utils.ReflectUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class PropertyUtil {

    /**
     * 将单元行转换成为目标对象
     */
    public static <T> T mapping(List<XSSFCell> cells, Property header, Class<T> clz) {
        try {
            T t = clz.newInstance();
            // 设置值
            for (XSSFCell cell : cells) {
                if (cell == null) continue;
                setValue(t, header, cell.getRowIndex() - cells.get(0).getRowIndex(), cell.getColumnIndex(), cell);
            }
            return t;
        } catch (Exception e) {
            throw new ServiceException("转换对象异常：" + e.getMessage(), e);
        }
    }

    private static <T> void setValue(T t, Property header, Integer row, Integer col, XSSFCell value) {
        try {
            Property cell = header.findBottomProperty(col);
            if (cell == null) return;

            List<String> rootPath = cell.findRootPath();

            Field field = null;
            Object parent = t;
            for (String name : rootPath) {
                // 不是最底层
                if (rootPath.lastIndexOf(name) != rootPath.size() - 1) {
                    field = parent.getClass().getDeclaredField(name);
                    field.setAccessible(true);

                    if (field.get(parent) == null) {
                        // 集合
                        if (ReflectUtils.isList(field)) {
                            // 1. 给此集合创建一个实例
                            field.set(parent, new ArrayList<>());
                        } else {
                            // 普通类
                            field.set(parent, field.getType().newInstance());
                        }
                    }
                    parent = field.get(parent);
                }

                // 最低层
                if (rootPath.lastIndexOf(name) == rootPath.size() - 1) {
                    // 判断是否集合类型
                    if (ReflectUtils.isObjectType(parent.getClass())) {
                        field = parent.getClass().getDeclaredField(name);
                        Object cellValue = POIUtils.cellValue(value);
                        field.setAccessible(true);
                        field.set(parent, BeanMapper.map(cellValue, field.getType()));
                    }

                    // 集合类型
                    if (ReflectUtils.isList(parent.getClass())) {
                        List list = (List) parent;
                        Class listType = ReflectUtils.findListType(field);

                        while (list.size() < row + 1) {
                            list.add(listType.newInstance());
                        }

                        // 集合泛型是基本数据类型
                        if (ReflectUtils.isBaseType(listType)) {
                            list.set(row, value);
                        }
                        // 集合泛型是对象类
                        if (ReflectUtils.isObjectType(listType)) {
                            Field ff = list.get(row).getClass().getDeclaredField(name);
                            Object cellValue = POIUtils.cellValue(value);

                            ff.setAccessible(true);
                            field.set(list.get(row), BeanMapper.map(cellValue, ff.getType()));
                        }
                    }
                }
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Excel字段转换异常：" + e.getMessage(), e);
        }
    }
}
