package com.icebartech.phoneparts.redeem.excel;

import com.icebartech.excel.annotation.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedeemImports {

    @ExcelField(value = "标题", columnWidth = 30)
    private String title;

    @ExcelField(value = "兑换码", columnWidth = 20)
    private String code;

    @ExcelField(value = "兑换次数")
    private Integer useNum;

    @ExcelField("所属代理商（一级分类）")
    private String className;

    public static List<RedeemImports> demo() {
        List<RedeemImports> imports = new ArrayList<>();
        imports.add(new RedeemImports("标题一", "100101", 12, "Cutter"));
        imports.add(new RedeemImports("标题二", "100102", 26, "DEVIA"));
        imports.add(new RedeemImports("标题三", "100103", 55, "Cutter"));
        imports.add(new RedeemImports("标题四", "100104", 33, "Cutter"));
        return imports;
    }

    /**
     * 用于分组
     */
    public String group() {
        return this.title + ":" + this.className;
    }
}
