/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with htouhui.com.
 */
package com.farm.core.constant;

/**
 * 常量类
 *
 */
public final class DigestConstants {

    static final String COOKIE_NAME_SESSION_ID = "htouhui_session_id";
    public static final String PARAMETER_DIGEST = "htouhui_digest";

    public final static class RequestSource {
        public static final String IOS = "iOS";
        public static final String ANDROID = "android";
        public static final String PC = "pc";
        public static final String H5 = "h5";
        public static final String APP_H5 = "app_h5";
        public static final String WECHAT = "wechat";
        public static final String DD = "dd";
    }

    public final static class PlatformAwardType {
        public static final String COUPON = "coupon";
        public static final String EXTRA_INTEREST = "extra_interest";
    }

    public final static class PlatformAwardCouponType {
        public static final String CASH = "cash";
        public static final String PLUS_COUPON = "plus_coupon";
    }

}
