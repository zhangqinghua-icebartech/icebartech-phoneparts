package com.icebartech.excel.dto;

import com.icebartech.excel.annotation.ExcelField;
import lombok.Data;

import java.util.List;

@Data
class Order2 {

    @ExcelField("订单编号")
    private Long orderId;

    @ExcelField("订单名称")
    private String orderName;

    @ExcelField(value = "货物")
    private List<Cargo> cargos;

    @ExcelField(value = "订单金额")
    private Integer price;

    @Data
    public static class Cargo {

        @ExcelField(value = "名称")
        private String name;

        @ExcelField(value = "重量（kg）")
        private Integer weight;
    }
}
