package com.farm.core.user.exception;

import com.farm.base.BaseException;

/**
 ** @Version 1.0.0
 */
public class DdException extends BaseException {

    public static final int  SUITE_TICKET_IS_ERROR = 1000;
    public static final int  DD_USER_INFO_ERROR = 1001;
    public static final int  DD_USER_ID_ERROR = 1002;
    public static final int  DD_ASSESS_TOKEN_ERROR = 1003;

    public DdException(int code) {
        super(code);
    }

    public DdException(int code, Throwable throwable) {
        super(code, throwable);
    }

    public DdException(int code, String errorMessage) {
        super(code, errorMessage);
    }
}
