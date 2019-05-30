/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with htouhui.com.
 */
package com.farm.core.config;

import com.alibaba.fastjson.JSONObject;
import com.farm.base.BaseException;
import com.farm.base.common.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Locale;

/**
 * API接口异常处理的Handler
 *
 */
@ControllerAdvice
public class APIExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(APIExceptionHandler.class);

    private final MessageSource messageSource;

    @Autowired
    public APIExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = Exception.class)
    public void defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e, Locale locale) throws Exception {

        JsonResult result = new JsonResult();
        int code = 500;
        String errorMessage = null;
        Throwable exception = e;
        if (e instanceof UndeclaredThrowableException) {
            exception = ((UndeclaredThrowableException) e).getUndeclaredThrowable();
        }
        if (exception instanceof BaseException) {
            code = ((BaseException) exception).getCode();
            errorMessage = ((BaseException) exception).getErrorMessage();
        } else if (exception instanceof MissingServletRequestParameterException) {
            code = BaseException.ERROR_CODE_ILLEGAL_ARGUMENTS;
        }

        if (StringUtils.isBlank(errorMessage)) { // 如果异常里已经包含了错误信息，则不会再通过错误码获取预先定义的错误信息
            String prefix = exception.getClass().getSimpleName();
            errorMessage = getMessage(prefix + "." + code, locale);
            if (errorMessage == null) {
                errorMessage = getMessage(Integer.toString(code), locale);
            }
        }
        result.setStatus(code);
        result.setMessage(errorMessage);
        output(response, result);

        if (!(exception instanceof BaseException || exception instanceof MissingServletRequestParameterException)) {
            LOGGER.error("Unknown Exception, URI = " + request.getRequestURI(), e);
        } else {
            LOGGER.error("URI = {}, errorCode = {}, errorMessage = {}", request.getRequestURI(), code, errorMessage);
        }
    }

    private void output(HttpServletResponse response, JsonResult result) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = JSONObject.toJSONString(result);
        response.getWriter().write(json);
        response.flushBuffer();
    }

    private String getMessage(String key, Locale locale) {
        String errorMessage = null;
        try {
            errorMessage = messageSource.getMessage(key, null, locale);
        } catch (NoSuchMessageException exception) {
            LOGGER.debug("ErrorMessage|NotFound|{}", key);
        }
        return errorMessage;
    }

}
