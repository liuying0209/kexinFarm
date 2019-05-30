package com.farm.core.user;

import java.io.Serializable;

/**
 * 登入参数
 *
 ** @Date: 2019-04-20 23:37
 */
public class LoginVO implements Serializable {

    private static final long serialVersionUID = -3214341055303069027L;
    /**
     * 账号 手机号
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 来源
     */
    private String source;
    /**
     * 验证码
     */
    private String identifyCode;

    public LoginVO() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIdentifyCode() {
        return identifyCode;
    }

    public void setIdentifyCode(String identifyCode) {
        this.identifyCode = identifyCode;
    }

    @Override
    public String toString() {
        return "LoginVO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", source='" + source + '\'' +
                ", identifyCode='" + identifyCode + '\'' +
                '}';
    }
}
