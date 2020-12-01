package com.icebartech.excel;

import com.icebartech.core.utils.BeanMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
class Property {

    /**
     * 单元格类型
     */
    private Type type;

    /**
     * 单元格名称
     */
    private String name;

    /**
     * 单元格内容
     */
    private Object value;

    /**
     * 单元格长度，1个长度表示1个数字/字母，或半个汉字。没有则按照标题文字计算长度
     */
    private Integer columnWidth;

    /**
     * 父单元格
     */
    private Property parent;

    /**
     * 子单元格列表
     */
    private List<Property> children;

    /**
     * 第几行
     */
    private Integer row;

    public Property() {
    }

    public Property(String value) {
        this.value = value;
    }

    public static Property buildRoot(Integer row) {
        Property property = new Property();
        property.setRow(row - 1);
        return property;
    }

    public void setValue(Object value) {
        if (value == null) return;

        this.value = String.valueOf(value);
        this.type = Type.STRING;

        if ("java.util.Date".equals(value.getClass().getName())) {
            // this.type = Type.DATE;
            this.value = BeanMapper.map(value, String.class);
        }
        if ("java.time.LocalDateTime".equals(value.getClass().getName())) {
            // todo 转换
            this.value = BeanMapper.map(value, String.class);
        }

        if (Arrays.asList(
                "int", "double", "long", "short", "boolean", "float",
                "java.lang.Integer",
                "java.lang.Double",
                "java.lang.Float",
                "java.lang.Long",
                "java.lang.Short",
                "java.math.BigDecimal").contains(value.getClass().getName())) {
            this.type = Type.DOUBLE;
            this.value = BeanMapper.map(value, Double.class);
        }

        if ("java.lang.Boolean".equals(value.getClass().getName())) {
            this.type = Type.BOOLEAN;
        }

        // todo richtext
    }

    public void setValue(Object value, Type type) {
        this.setValue(value);
        this.type = type;
    }

    /**
     * 获取当前单元格所在行数
     * 父单元格的行数 + 1
     */
    public Integer getRow() {
        if (this.parent == null) return this.row == null ? 0 : this.row;
        return this.parent.getRow() + 1;
    }

    /**
     * 获取当前单元格所在列数
     * <p>
     * 1. 如果它是父节点的第一个节点，则取父节点的列数
     * 1. 否则取前兄弟节点的列数 + 前兄弟节点的宽度
     */
    public Integer getCol() {
        if (this.getParent() == null) return 0;
        int index = this.getParent().getChildren().indexOf(this);

        if (index == 0) {
            return this.parent.getCol();
        } else {
            Property pre = this.getParent().getChildren().get(index - 1);
            return pre.getCol() + pre.getWidth();
        }
    }

    /**
     * 获取此节点的宽度
     * 1. 没有子节点就是1
     * 1. 有子节点就就算子节点宽度和
     */
    public Integer getWidth() {
        if (this.getChildren() == null) return 1;
        return this.getChildren().stream().mapToInt(Property::getWidth).sum();
    }

    /**
     * 获取此节点的高度
     * 1. 取兄弟节点的最大深度
     */
    public Integer getHeight() {
        if (this.getParent() == null) return 1;
        if (this.getChildren() != null) return 1;
        return this.getParent().getChildren().stream().mapToInt(Property::getDepth).max().orElse(1);
    }

    /**
     * 获取此节点的深度
     * 1. 没有子节点则1
     * 2. 有子节点则取子节点的深度
     */
    public Integer getDepth() {
        if (this.getChildren() == null) return 1;
        return this.getChildren().stream().mapToInt(Property::getDepth).max().orElse(0) + 1;
    }

    public void addChildren(List<Property> children) {
        if (children == null) return;
        for (Property child : children) {
            this.addChild(child);
        }
    }

    public void addChild(Property child) {
        if (child == null) return;

        // 子单元格的行数是父单元格的行数+1
        child.setParent(this);

        if (this.getChildren() == null) this.setChildren(new ArrayList<>());
        this.getChildren().add(child);
    }

    public void addChild(String name) {
        // 子单元格的行数是父单元格的行数+1
        Property child = new Property();
        child.setValue(name);
        this.addChild(child);
    }

    public Property getChild(String name) {
        if (this.getChildren() == null) return null;
        return this.getChildren().stream().filter(s -> s.getValue().equals(name)).findFirst().orElse(null);
    }

    public void print() {
        if (this.getParent() != null) {
            System.out.println(String.format("%s 行：%s   列：%s    宽：%s    高：%s", this.getValue(), this.getRow(), this.getCol(), this.getWidth(), this.getHeight()));
        }

        if (this.getChildren() != null) {
            System.out.println();
            this.getChildren().forEach(Property::print);
            System.out.println();
        }
    }

    /**
     * 将此节点以及子节点平铺开来
     */
    public List<Property> findFlatProperties() {
        List<Property> list = new ArrayList<>();
        if (this.getParent() != null) list.add(this);
        if (this.getChildren() != null) {
            for (Property child : this.getChildren()) {
                list.addAll(child.findFlatProperties());
            }
        }
        return list;
    }

    /**
     * 获取底层节点
     */
    public List<Property> findBottomProperties() {
        return this.findFlatProperties().stream().filter(s -> s.getChildren() == null || s.getChildren().size() == 0).collect(Collectors.toList());
    }

    /**
     * 获取指定列的节点。
     */
    public Property findBottomProperty(Integer col) {
        return this.findBottomProperties().stream().filter(s -> s.getCol().equals(col)).findFirst().orElse(null);
    }

    /**
     * 获取指定列的节点。
     */
    public List<Property> findBottomProperties(Integer col) {
        return this.findBottomProperties().stream().filter(s -> s.getCol().equals(col)).collect(Collectors.toList());
    }

    /**
     * 获取此节点的完整路径
     * cargos,weight
     */
    public List<String> findRootPath() {
        List<String> rootPath = new ArrayList<>();

        if (this.parent != null) {
            rootPath.addAll(this.parent.findRootPath());
        }
        if (this.name != null && this.name.length() > 0) {
            rootPath.add(this.name);
        }

        return rootPath;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    /**
     * 节点类型
     */
    public enum Type {
        DATE,
        STRING,
        DOUBLE,
        BOOLEAN,
        RICHTEXT,

        OPTIONS,
    }

    public static void main(String[] args) {
        Property header = Property.buildRoot(0);
        header.addChild("订单编号");
        header.addChild("订单名称");
        header.addChild("货物列表");

        header.getChild("货物列表").addChild("名称");
        header.getChild("货物列表").addChild("重量");

        header.addChild("订单金额");

        header.print();

        header.findBottomProperty(2).print();
    }
}
