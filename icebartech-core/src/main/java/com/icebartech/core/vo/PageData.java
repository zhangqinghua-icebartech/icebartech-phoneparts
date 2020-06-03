package com.icebartech.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * 分页类型的响应数据
 *
 * @author wenhsh
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "PageData", description = "分页类型的响应数据")
public class PageData<T> extends BaseData<List<T>> {

    public PageData() {
    }

    public PageData(int count, int pageCount, List<T> bussData) {
        super(bussData);
        this.count = count;
        this.pageCount = pageCount;
    }

    /**
     * 总数据条数
     */
    @ApiModelProperty(value = "总数据条数")
    private int count = 0;

    /**
     * 总分页数
     */
    @ApiModelProperty(value = "总分页数")
    private int pageCount = 0;

    /**
     * 扩展信息，比如可以存储像合计、统计这一类的数据
     */
    @ApiModelProperty(value = "扩展信息，比如可以存储像合计、统计这一类的数据")
    private Map<String, Object> ext;

}