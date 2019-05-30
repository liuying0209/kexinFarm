package com.farm.core.user;

import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

/**
 * 钉钉登入页面参数
 ** @Version 1.0.0
 */
public class DdLoginVO implements Serializable {

    private static final long serialVersionUID = -3892751524931854882L;
    /**
     * corpId
     */
  private   String corpId;

  private   String requestAuthCode;

    public DdLoginVO() {
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getRequestAuthCode() {
        return requestAuthCode;
    }

    public void setRequestAuthCode(String requestAuthCode) {
        this.requestAuthCode = requestAuthCode;
    }

    @Override
    public String toString() {
        return "DdLoginVO{" +
                "corpId='" + corpId + '\'' +
                ", requestAuthCode='" + requestAuthCode + '\'' +
                '}';
    }
}
