package com.bzw.common.exception;

import com.bzw.common.log.LogServiceImpl;
import com.bzw.common.web.JsonExceptionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public ExceptionAdvice(LogServiceImpl logServiceImpl){
        this.logServiceImpl = logServiceImpl;
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public JsonExceptionWrapper handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        return wrapperException(e);
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JsonExceptionWrapper handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return wrapperException(e);
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public JsonExceptionWrapper handleHttpMediaTypeNotSupportedException(
            Exception e) {
        return wrapperException(e);
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Throwable.class)
    public JsonExceptionWrapper handleException(Throwable e) {
        return wrapperException(e);
    }

    private JsonExceptionWrapper wrapperException(Throwable e) {
        JsonExceptionWrapper jsonExceptionWrapper = new JsonExceptionWrapper(e);
        logServiceImpl.insertExcept(jsonExceptionWrapper);
        return jsonExceptionWrapper;
    }
}
