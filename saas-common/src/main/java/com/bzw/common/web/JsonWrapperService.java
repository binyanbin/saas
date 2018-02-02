package com.bzw.common.web;

import com.bzw.common.exception.ApplicationErrorCode;
import com.bzw.common.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.Arrays;

@Service
public class JsonWrapperService {

    @Value("${exception.showStackTrace}")
    private boolean showStackTrace;

    public JsonExceptionWrapper getJsonExceptionWrapper(Throwable ex) {
        JsonExceptionWrapper result = new JsonExceptionWrapper();
        if (ex instanceof ApplicationException) {
            ApplicationException gex = (ApplicationException) ex;
            result.setCode(gex.getCode());
            result.setMsg(gex.getReasoning());
        } else if (ex instanceof IllegalArgumentException
                || ex instanceof MissingServletRequestParameterException
                || ex instanceof HttpMessageNotReadableException
                || ex instanceof HttpRequestMethodNotSupportedException
                || ex instanceof HttpMediaTypeNotSupportedException
                || ex instanceof MethodArgumentTypeMismatchException) {
            result.setCode(ApplicationErrorCode.ArgumentsIncorrect.getCode());
            result.setMsg(ex.getMessage());
        } else if (ex instanceof IOException) {
            result.setCode(ApplicationErrorCode.IOException.getCode());
            result.setMsg(ex.getMessage());
        } else if (ex instanceof NoHandlerFoundException) {
            result.setCode(ApplicationErrorCode.NoHandlerFound.getCode());
            result.setMsg(ApplicationErrorCode.NoHandlerFound.getReasoning());
        } else {
            result.setCode(ApplicationErrorCode.UnKnowException.getCode());
            result.setMsg(ApplicationErrorCode.UnKnowException.getReasoning() + ":" + ex.getMessage());
        }
        if (showStackTrace) {
            result.setStackTrace(Arrays.toString(ex.getStackTrace()));
        } else {
            result.setStackTrace("");
        }
        return result;
    }
}
