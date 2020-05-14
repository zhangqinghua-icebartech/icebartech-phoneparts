package com.icebartech.phoneparts.system.service;

import com.icebartech.core.modules.BaseService;
import com.icebartech.phoneparts.system.dto.SysSerialDto;
import com.icebartech.phoneparts.system.param.SysSerialCreateParam;
import com.icebartech.phoneparts.system.param.SysSerialInsertParam;
import com.icebartech.phoneparts.system.po.SysSerial;

import java.util.List;

/**
 * @author pc
 * @Date 2019-06-18T11:09:42.138
 * @Description 序列号表
 */

public interface SysSerialService extends BaseService
        <SysSerialDto, SysSerial> {

    /**
     * 批量导入
     * @param sysSerialInsertParams 数据列表
     * @return Boolean
     */
    Boolean excelInput(List<SysSerialInsertParam> sysSerialInsertParams);

    /**
     * 获取通过序列号
     * @param serialNum 序列号
     * @return SysSerialDTO
     */
    SysSerialDto findBySerialNum(String serialNum);

    /**
     * 增长使用次数
     * @param serialDTO 数据
     * @return Boolean
     */
    Boolean useNumAdd(SysSerialDto serialDTO);

    /**
     * 检测序列号
     * @param serialNum 序列号
     * @return
     */
    Boolean isValid(String serialNum);

    /**
     * 批量生成序列号
     * @param param
     * @return
     */
    String create(SysSerialCreateParam param);

    Boolean init(Long id);

    /**
     * 获取二级分类下的一个
     * @param id 二级分类id
     * @return
     */
    SysSerialDto findBySerialClassId(Long id);

    Integer unUseNum(Long userId);

    Boolean allocation(Long secondAgentId, List<Long> serialIds,Long serialClassId);
}
