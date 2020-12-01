package com.icebartech.excel.dto;

import com.icebartech.excel.annotation.ExcelField;
import lombok.Data;

import java.util.List;

@Data
public class OrderImportsExcel {

    @ExcelField(value = "渠道信息 ")
    private OrderImportsExcelaramOrderChannel orderChannel;

    @Data
    public static class OrderImportsExcelaramOrderChannel {

        @ExcelField(value = "渠道Id")
        private Long channelId;

        @ExcelField(value = "货物Id列表")
        private List<Long> cargoIds;
    }
}
