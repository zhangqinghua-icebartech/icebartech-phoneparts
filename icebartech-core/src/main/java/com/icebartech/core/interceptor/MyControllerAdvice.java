package com.icebartech.core.interceptor;

import com.icebartech.core.constants.IcebartechConstants;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.vo.BaseData;
import com.icebartech.core.vo.RespJson;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class MyControllerAdvice {
//
//    /**
//     * 全局异常捕捉处理
//     */
//    @ResponseBody
//    @ExceptionHandler(value = Exception.class)
//    public RespJson errorHandler(Exception ex) {
//        int statusCode;
//        StringBuilder msg = new StringBuilder();
//        if (ex instanceof HttpRequestMethodNotSupportedException) {
//            statusCode = 405;
//            msg = new StringBuilder("不支持当前请求方法！请使用 " + Arrays.toString(((HttpRequestMethodNotSupportedException) ex).getSupportedMethods()) + " 方式请求");
//        } else if (ex instanceof MissingServletRequestParameterException) {
//            statusCode = 400;
//            msg = new StringBuilder("缺少请求参数！ [" + ((MissingServletRequestParameterException) ex).getParameterType() + "]" + ((MissingServletRequestParameterException) ex).getParameterName());
//        } else if (ex instanceof MethodArgumentNotValidException) {
//            statusCode = 400;
//            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
//            List<ObjectError> allErrors = bindingResult.getAllErrors();
//            for (ObjectError error : allErrors) {
//                msg.append(((FieldError) error).getField() + " " + error.getDefaultMessage()).append("\n");
//            }
//        } else if (ex instanceof ServiceException) {
//            statusCode = 500;
//            msg = new StringBuilder(((ServiceException) ex).getErrorMessage());
//        } else {
//            statusCode = 500;
//            msg = new StringBuilder("未知错误！" + ex.getMessage() + "!!!");
//        }
//        RespJson respJson = new RespJson();
//        respJson.setStatus(statusCode + "");
////        logger.info("发生错误，errorMsg：" + ex.getMessage(), ex);
//        return respJson;
//    }

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public RespJson errorHandler(Exception ex) {
        ex.printStackTrace();

        int resultCode;
        StringBuilder errMsg = new StringBuilder();
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            resultCode = CommonResultCodeEnum.MISSING_SUPPORTED_METHODS.getCode();
            errMsg = new StringBuilder("不支持当前请求方式！请使用 " + Arrays.toString(((HttpRequestMethodNotSupportedException) ex).getSupportedMethods()) + " 方式请求");
        } else if (ex instanceof MissingServletRequestParameterException) {
            resultCode = CommonResultCodeEnum.MISSING_REQUIRED_ARGUMENTS.getCode();
            errMsg = new StringBuilder("缺少请求参数！ [" + ((MissingServletRequestParameterException) ex).getParameterType() + "]" + ((MissingServletRequestParameterException) ex).getParameterName());
            ex.printStackTrace();
        } else if (ex instanceof MethodArgumentNotValidException) {
            resultCode = CommonResultCodeEnum.INVALID_ARGUMENTS.getCode();
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError error : allErrors) {
                //noinspection StringConcatenationInsideStringBufferAppend
                errMsg.append(((FieldError) error).getField() + " " + error.getDefaultMessage()).append("、");
            }
            if (errMsg.length() > 0) {
                errMsg.deleteCharAt(errMsg.length() - 1);
            }
        } else if (ex instanceof ServiceException) {
            resultCode = ((ServiceException) ex).getErrorCode().getCode();
            errMsg = new StringBuilder(((ServiceException) ex).getErrorMessage());
        } else if (ex instanceof IllegalArgumentException) {
            // 断言抛出的异常，一般是函数入参有问题 抛出异常的地方必须在函数调用前进行参数校验
            resultCode = 500;
            errMsg = new StringBuilder("系统错误，请联系开发人员修改！" + ex.getMessage() + "!!!");
            ex.printStackTrace();
        } else {
            resultCode = CommonResultCodeEnum.UNKNOWN_ERROR.getCode();
            errMsg = new StringBuilder("未知错误！" + ex.getMessage() + "!!!");
        }
        RespJson respJson = new RespJson();
        respJson.setStatus(IcebartechConstants.RESULT_STATUS_FAILED);
        respJson.setCode(IcebartechConstants.SERVICE_ERROR);
        BaseData baseData = new BaseData();
        baseData.setResultCode(resultCode);
        String errMsgStr = errMsg.toString();
        errMsgStr = errMsgStr.endsWith("\n") ? errMsgStr.substring(0, errMsgStr.length() - 2) : errMsgStr;
        baseData.setErrMsg(errMsgStr);
        respJson.setData(baseData);
//        logger.info("发生错误，errorMsg：" + ex.getMessage(), ex);
        return respJson;
    }

}