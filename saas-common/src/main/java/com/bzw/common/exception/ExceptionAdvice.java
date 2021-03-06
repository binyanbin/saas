package com.bzw.common.exception;

import com.bzw.common.log.LogServiceImpl;
import com.bzw.common.system.StatusType;
import com.bzw.common.web.JsonExceptionWrapper;
import com.bzw.common.web.JsonWrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 *
 * @author yanbin
 * @date 2017/7/1
 */
@ControllerAdvice
@ResponseBody
@CrossOrigin
public class ExceptionAdvice {

    private LogServiceImpl logServiceImpl;
    private JsonWrapperService jsonWrapperService;

    @Autowired
    public ExceptionAdvice(LogServiceImpl logServiceImpl, JsonWrapperService jsonWrapperService){
        this.logServiceImpl = logServiceImpl;
        this.jsonWrapperService = jsonWrapperService;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public JsonExceptionWrapper handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        return getJsonExceptionWrapper(e);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JsonExceptionWrapper handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return getJsonExceptionWrapper(e);
    }


    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public JsonExceptionWrapper handHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        return getJsonExceptionWrapper(e);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public JsonExceptionWrapper handleHttpMediaTypeNotSupportedException(
            Exception e) {
        return getJsonExceptionWrapper(e);
    }

    private JsonExceptionWrapper getJsonExceptionWrapper(Exception e) {
        JsonExceptionWrapper exceptionWrapper = new JsonExceptionWrapper();
        exceptionWrapper.setCode(String.valueOf(StatusType.ApplicationException.getStatusCode()));
        exceptionWrapper.setStackTrace(Arrays.toString(e.getStackTrace()));
        exceptionWrapper.setMsg(StatusType.ApplicationException.getDescription());
        return exceptionWrapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Throwable.class)
    public JsonExceptionWrapper handleException(Throwable e) {
        return wrapperException(e);
    }

    private JsonExceptionWrapper wrapperException(Throwable e) {
        JsonExceptionWrapper jsonExceptionWrapper = jsonWrapperService.getJsonExceptionWrapper(e);
        logServiceImpl.insertExcept(jsonExceptionWrapper,e);
        jsonExceptionWrapper.setCode(String.valueOf(StatusType.ApplicationBug));
        return jsonExceptionWrapper;
    }
}
