package com.farm.core.record.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * 集体农事环节参数类
 ** @Version 1.0.0
 */
public class RecordParamDTO implements Serializable {
    private static final long serialVersionUID = -192545939951262378L;


    /**
     * 记录id
     */
    private  Long recordId;

    /**
     * 名称
     */
    private String name;


    /**
     * 内容
     */
    private JSONArray content=new JSONArray();

    /**
     * 状态
     */
    private Integer status;

    /**
     * 时间
     */
    private Date time;

    public RecordParamDTO() {
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONArray getContent() {
        return content;
    }

    public void setContent(JSONArray content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
