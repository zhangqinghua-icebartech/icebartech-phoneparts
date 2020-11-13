package com.icebartech.core.enums;


/**
 * ClassName: ResultEnum
 *
 * @author guocp
 * @date 2017年5月2日
 * <p>
 * =================================================================================================
 * Task ID			  Date			     Author		      Description
 * ----------------+----------------+-------------------+-------------------------------------------
 */

public interface ResultEnum<T> {

    T getCode();

    String getDesc();
}
