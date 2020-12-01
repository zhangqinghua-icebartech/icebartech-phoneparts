package com.icebartech.excel.dto;

import com.icebartech.excel.annotation.ExcelField;
import com.icebartech.excel.annotation.Options;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order3 {

    @ExcelField(value = "订单编号")
    private Long id;

    @ExcelField(value = "订单名称")
    private String name;

    @ExcelField(value = "渠道Id", options = ChannelOptions.class)
    private Long channelId;

    @Data
    public static class ChannelOptions implements Options {

        @Override
        public String[] get() {
            return new String[]{"(1)Java", "(2)C", "(3)C++", "(4)PHP"};
        }
    }

    public static List<Order3> createExample() {
        List<Order3> list = new ArrayList<>();

        Order3 order3 = new Order3();
        order3.setId(1L);
        order3.setName("订单一");
        order3.setChannelId(3L);
        list.add(order3);

        order3 = new Order3();
        order3.setId(2L);
        order3.setName("订单二");
        order3.setChannelId(1L);
        list.add(order3);

        return list;
    }
}
