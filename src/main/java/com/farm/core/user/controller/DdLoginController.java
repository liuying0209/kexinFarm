package com.farm.core.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.DingTalkSignatureUtil;
import com.dingtalk.api.request.OapiServiceGetCorpTokenRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiServiceGetCorpTokenResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.farm.base.common.JsonResult;
import com.farm.core.config.ApiUrlConstant;
import com.farm.core.config.DdConfig;
import com.farm.core.constant.Constants;
import com.farm.core.user.DdLoginVO;
import com.farm.core.user.exception.DdException;
import com.farm.core.user.exception.UserException;
import com.farm.core.user.service.UserLoginService;
import com.farm.core.util.RedisUtil;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 ** @Version 1.0.0
 */
@RestController
@RequestMapping("api/dd")
public class DdLoginController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    UserLoginService userLoginService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    DdConfig ddConfig;

    @PostMapping("login")
    public JsonResult ddLogin(DdLoginVO params) throws UserException, DdException {
        LOGGER.info("请求开始报告: 钉钉登入接口 参数:{}", JSONObject.toJSONString(params));
        String corpId = params.getCorpId();
        String requestAuthCode = params.getRequestAuthCode();

        if(StringUtils.isBlank(corpId)||StringUtils.isBlank(requestAuthCode)){
            LOGGER.error("必要参数为空");
            throw  new DdException(DdException.ERROR_CODE_ILLEGAL_ARGUMENTS);

        }

        //获取accessToken,注意正是代码要有异常流处理
        OapiServiceGetCorpTokenResponse oapiServiceGetCorpTokenResponse = getOapiServiceGetCorpToken(corpId);

        if(oapiServiceGetCorpTokenResponse==null){
            LOGGER.error("获取accessToken异常");
            throw new DdException(DdException.DD_ASSESS_TOKEN_ERROR);
        }

        String accessToken = oapiServiceGetCorpTokenResponse.getAccessToken();

        //获取用户信息
        OapiUserGetuserinfoResponse oapiUserGetuserinfoResponse = getOapiUserGetuserinfo(accessToken, requestAuthCode);

        if(oapiUserGetuserinfoResponse==null){
            LOGGER.error("获取钉钉用户id 异常");
            throw new DdException(DdException.DD_USER_ID_ERROR);
        }

        //3.查询得到当前用户的userId
        // 获得到userId之后应用应该处理应用自身的登录会话管理（session）,避免后续的业务交互（前端到应用服务端）每次都要重新获取用户身份，提升用户体验
        String userId = oapiUserGetuserinfoResponse.getUserid();

        OapiUserGetResponse oapiUserGetResponse = getOapiUserGetResponse(accessToken, userId);
        if(oapiUserGetResponse==null){
            LOGGER.error("获取钉钉用户详情信息异常");
            throw new DdException(DdException.DD_USER_INFO_ERROR);
        }

        String token = this.userLoginService.ddLogin(oapiUserGetResponse);
        LOGGER.info("用户token:{}",token);
        //返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("corpId", corpId);
        resultMap.put("token", token);

        return JsonResult.ok(resultMap);
    }

    /**
     * ISV获取企业访问凭证
     *
     * @param corpId 授权企业的corpId
     */
    private OapiServiceGetCorpTokenResponse getOapiServiceGetCorpToken(String corpId) throws DdException {
        if (corpId == null || corpId.isEmpty()) {
            return null;
        }
        long timestamp = System.currentTimeMillis();
        //正式应用应该由钉钉通过开发者的回调地址动态获取到
        String suiteTicket = getSuiteTicket(ddConfig.getSuiteKey());
        String signature = DingTalkSignatureUtil.computeSignature(ddConfig.getSuiteSecret(), DingTalkSignatureUtil.getCanonicalStringForIsv(timestamp, suiteTicket));
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("timestamp", String.valueOf(timestamp));
        params.put("suiteTicket", suiteTicket);
        params.put("accessKey", ddConfig.getSuiteKey());
        params.put("signature", signature);
        String queryString = DingTalkSignatureUtil.paramToQueryString(params, "utf-8");
        DingTalkClient client = new DefaultDingTalkClient(ApiUrlConstant.URL_GET_CORP_TOKEN + "?" + queryString);
        OapiServiceGetCorpTokenRequest request = new OapiServiceGetCorpTokenRequest();
        request.setAuthCorpid(corpId);
        OapiServiceGetCorpTokenResponse response;
        try {
            response = client.execute(request);
        } catch (ApiException e) {
            LOGGER.info(e.toString(), e);
            return null;
        }
        if (response == null || !response.isSuccess()) {
            return null;
        }
        return response;
    }


    /**
     * 通过钉钉服务端API获取用户在当前企业的userId
     *
     * @param accessToken 企业访问凭证Token
     * @param code        免登code
     * @
     */
    private OapiUserGetuserinfoResponse getOapiUserGetuserinfo(String accessToken, String code) {
        DingTalkClient client = new DefaultDingTalkClient(ApiUrlConstant.URL_GET_USER_INFO);
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(code);
        request.setHttpMethod("GET");

        OapiUserGetuserinfoResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
        if (response == null || !response.isSuccess()) {
            return null;
        }
        return response;
    }


    /**
     * 获取dd用户的详细信息
     */

    private OapiUserGetResponse getOapiUserGetResponse(String accessToken, String userId) {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
        OapiUserGetRequest request = new OapiUserGetRequest();
        request.setUserid(userId);
        request.setHttpMethod("GET");
        OapiUserGetResponse response;
        try {
            response = client.execute(request, accessToken);
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
        if (response == null || !response.isSuccess()) {
            return null;
        }

        return response;
    }


    /**
     * suiteTicket是一个定时变化的票据，主要目的是为了开发者的应用与钉钉之间访问时的安全加固。
     * 测试应用：可随意设置，钉钉只做签名不做安全加固限制。
     * 正式应用：开发者应该从自己的db中读取suiteTicket,suiteTicket是由开发者在开发者平台设置的应用回调地址，由钉钉定时推送给应用，
     * 由开发者在回调地址所在代码解密和验证签名完成后获取到的.正式应用钉钉会在开发者代码访问时做严格检查。
     *
     * @return suiteTicket
     */
    private String getSuiteTicket(String suiteKey) throws DdException {

        /**
         * 生产环境直接从redis中获取
         */
        Object obj = redisUtil.get(Constants.SUITE_TICKET);
        if(obj==null){
            LOGGER.error("redis中获取suiteTicket 异常");
            throw new DdException(DdException.SUITE_TICKET_IS_ERROR);
        }

        return obj.toString();

    }


}
