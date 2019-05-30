package com.farm.core.record;

import java.io.Serializable;

/**
 * 保存农事环节记录参数类
 *
 ** @Version 1.0.0
 */
public class FarmingRecordVO implements Serializable {

    private static final long serialVersionUID = 2967563954498520336L;
    /**
     * 记录id
     */
    private Long recordId;
    /**
     * 内容
     */
    private String content;

    /**
     * 时间
     */
    private String date;


    public FarmingRecordVO() {
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "FarmingRecordVO{" +
                "recordId=" + recordId +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
