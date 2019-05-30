/*
 * Copyright (c) 2018 htouhui.com. All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with htouhui.com.
 */
package com.farm.core.constant;

/**
 * 通用常量
 *
 */
public final class Constants {

    public static final String ZOOKEEPER_ROOT_PATH = "/farm";

    /**
     * jwt
     */
    public static final String JWT_ID = "farmJwt";
    public static final String JWT_SECRET = "farm";
    public static final long WEB_JWT_TTL = 2*60 * 60 * 1000;
    public static final long APP_JWT_TTL = 15 * 24 * 60 * 60 * 1000;
    /**
     * token过期时间，1h
     */
    public static final long WE_CHAT_JWT_TTL = 60 * 60 * 1000;
    public static final long WEB_NEED_REFRESH_TOKEN_TIME = 7 * 60 * 1000;
    public static final long APP_NEED_REFRESH_TOKEN_TIME = 5 * 24 * 60 * 60 * 1000;


    public static final String USER_PREFIX = "f";

    /**
     * 保存在redis中 suite_ticket
     */
    public static final String SUITE_TICKET = "suite_ticket";



    /**
     * 创建套件后，验证回调URL创建有效事件（第一次保存回调URL之前）
     */
    public static final String EVENT_CHECK_CREATE_SUITE_URL = "check_create_suite_url";
    /**
     * 创建套件后，验证回调URL变更有效事件（第一次保存回调URL之后）
     */
    public static final String EVENT_CHECK_UPADTE_SUITE_URL = "check_update_suite_url";

    /**
     * suite_ticket推送事件
     */
    public static final String EVENT_SUITE_TICKET = "suite_ticket";
    /**
     * 企业授权开通应用事件
     */
    public static final String EVENT_TMP_AUTH_CODE = "tmp_auth_code";
    /**
     * 相应钉钉回调时的值
     */
    public static final String CALLBACK_RESPONSE_SUCCESS = "success";

}
