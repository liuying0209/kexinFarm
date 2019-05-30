package com.farm.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 ** @Version 1.0.0
 */
@Component("ddConfig")
public class DdConfig {

    /**
     * 应用的SuiteKey，登录开发者后台，点击应用管理，进入应用详情可见
     */
    @Value("${dd.suite_key}")
    private  String suiteKey;

    /**
     * 应用的SuiteSecret，登录开发者后台，点击应用管理，进入应用详情可见
     */
    @Value("${dd.suite_secret}")
    private  String suiteSecret;

    /**
     * 回调URL加解密用。应用的数据加密密钥，登录开发者后台，点击应用管理，进入应用详情可见
     */
    @Value("${dd.encoding_aes_key}")
    private  String encodingAesKey;

    /**
     * 回调URL签名用。应用的签名Token, 登录开发者后台，点击应用管理，进入应用详情可见
     */
    @Value("${dd.token}")
    private  String token;


    public DdConfig() {
    }

    public String getSuiteKey() {
        return suiteKey;
    }

    public void setSuiteKey(String suiteKey) {
        this.suiteKey = suiteKey;
    }

    public String getSuiteSecret() {
        return suiteSecret;
    }

    public void setSuiteSecret(String suiteSecret) {
        this.suiteSecret = suiteSecret;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
