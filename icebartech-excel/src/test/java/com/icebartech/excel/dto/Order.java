package com.icebartech.excel.dto;

import com.icebartech.excel.annotation.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class Order {

    @ExcelField("订单编号")
    private Long orderId;

    @ExcelField("订单名称")
    private String orderName;

    @ExcelField(value = "货物列表")
    private List<Cargo> cargos;

    @ExcelField(value = "订单金额")
    private Integer price;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cargo {

        @ExcelField(value = "名称")
        private String name;

        @ExcelField(value = "重量（kg）")
        private Integer weight;
    }

    public static List<Order> createExample() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(100101L, null, null, 305));
        orders.add(new Order(100102L, "订单二", Arrays.asList(new Cargo("平板", 20), new Cargo("书本", 15)), 500));
        orders.add(new Order(100102L, "订单三", Arrays.asList(new Cargo("平板", 20), new Cargo("书本", 15), new Cargo("显示器", 100)), 1500));
        return orders;
    }
}
