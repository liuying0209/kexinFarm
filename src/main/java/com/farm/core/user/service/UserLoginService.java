package com.farm.core.user.service;

import com.dingtalk.api.response.OapiUserGetResponse;
import com.farm.core.user.LoginVO;
import com.farm.core.user.exception.UserException;

/**
 ** @Date: 2019-04-20 23:32
 */
public interface UserLoginService {

    /**
     * 密码登入
     * @return
     */
    String loginByPassword(LoginVO loginParams) throws UserException;


    /**
     * 钉钉用户登入
     */

    String ddLogin(OapiUserGetResponse oapiUserGetResponse) throws UserException;



}
