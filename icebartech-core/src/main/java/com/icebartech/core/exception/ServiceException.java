package com.icebartech.core.exception;

import com.icebartech.core.enums.ResultEnum;
import lombok.Data;

/**
 * <p>
 * 表示服务返回的错误消息。
 * </p>
 *
 * <p>
 * {@link ServiceException}用于处理服务返回的错误消息。比如，没有权限进行某项操作
 * </p>
 *
 * <p>
 * 通常来讲，调用者只需要处理{@link ServiceException}。因为该异常表明请求被服务处理，但处理的结果表明
 * 存在错误。异常中包含了细节的信息，特别是错误代码，可以帮助调用者进行处理。
 * </p>
 */
@Data
public class ServiceException extends RuntimeException {

    private ResultEnum<Integer> errorCode;
    private String errorMessage;

    /**
     * 构造新实例。
     */
    public ServiceException() {
        super();
    }

    /**
     * 用给定的异常信息构造新实例。
     *
     * @param errorMessage 异常信息。
     */
    // public ServiceException(String errorMessage) {
    //     super((String) null);
    //     this.errorMessage = errorMessage;
    // }

    /**
     * 用表示异常原因的对象构造新实例。
     *
     * @param cause 异常原因。
     */
    public ServiceException(Throwable cause) {
        super(null, cause);
    }

    /**
     * 用异常消息和表示异常原因的对象构造新实例。
     *
     * @param errorMessage 异常信息。
     * @param cause        异常原因。
     */
    public ServiceException(String errorMessage, Throwable cause) {
        super(null, cause);
        this.errorMessage = errorMessage;
    }

    /**
     * 用异常消息和表示异常原因及其他信息的对象构造新实例。
     *
     * @param errorMessage 异常信息。
     * @param errorCode    错误代码。
     */
    public ServiceException(ResultEnum<Integer> errorCode, String errorMessage) {
        this(errorCode, errorMessage, null);
    }


    /**
     * 用异常消息和表示异常原因及其他信息的对象构造新实例。
     *
     * @param errorMessage 异常信息。
     * @param errorCode    错误代码。
     * @param cause        异常原因。
     */
    public ServiceException(ResultEnum<Integer> errorCode, String errorMessage, Throwable cause) {
        this(errorMessage, cause);
        this.errorCode = errorCode;
    }

}