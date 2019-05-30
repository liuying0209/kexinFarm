package com.farm.core.user.exception;

import com.farm.base.BaseException;

/**
 ** @Date: 2019-04-21 12:13
 */
public class UserException extends BaseException {


    public static final int ERROR_CODE_USER_DELETED = 700;
    public static final int LOGIN_COMMON_ERROR = 701;
    public static final int LOGIN_USWENAME_ERROE = 720;
    public static final int ERROR_CODE_LOGIN_OUT_FAILED = 702;
    public static final int ERROR_CODE_LOGIN_DISABLE = 703;
    public static final int ERROR_CODE_LOGIN_NOT_RESET_PASSWORD = 704;
    public static final int ERROR_CODE_NOT_BE_NULL = 710;
    public static final int ERROR_CODE_NOT_UPDATE_SUPERIOR = 711;
    public static final int ERROR_CODE_DEPT_ID_OR_NAME_EXIST = 712;
    public static final int ERROR_CODE_MOBILE_EXIST = 713;
    public static final int ERROR_CODE_EMAIL_EXIST = 714;
    public static final int ERROR_CODE_IS_OLD_PASSWORD = 715;
    public static final int ERROR_CODE_EMAIL_FORMAT = 716;
    public static final int ERROR_CODE_MOBILE_FORMAT = 717;
    public static final int ERROR_CODE_NOT_EXIST_DEPT = 719;

    public static final int ERROR_CODE_NOT_OLD_PASSWORD = 721;
    public static final int ERROR_CODE_NOT_PARENT = 722;
    public static final int ERROR_CODE_DEPT_ID_FORMAT_ERROR = 730;

    public UserException(int code) {
        super(code);
    }

    public UserException(int code, Throwable throwable) {
        super(code, throwable);
    }

    public UserException(int code, String errorMessage) {
        super(code, errorMessage);
    }
}
