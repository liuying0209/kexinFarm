package com.farm.core.record.dto;

import java.io.Serializable;

/**
 * 追加农事的DTO
 *
 ** @Version 1.0.0
 */
public class AppendedDTO implements Serializable {

    /**
     * 记录id
     */
    private Long recordId;

    /**
     * 类别 0-农药 1-化肥
     */
    private Integer appendedType = 0;

    /**
     * 名称
     */
    private String farmingName;

    /**
     * 归类 0-直接拍摄  1-前置任务  2-细分子任务
     */

    private Integer category;


    /**
     *  完成状态 0-未开始 1-未完成，2-已完成
     */

    private Integer status;


    public AppendedDTO() {
    }

    public Long getRecordId() {

        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Integer getAppendedType() {
        return appendedType;
    }

    public void setAppendedType(Integer appendedType) {
        this.appendedType = appendedType;
    }

    public String getFarmingName() {
        return farmingName;
    }

    public void setFarmingName(String farmingName) {
        this.farmingName = farmingName;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AppendedDTO{" +
                "recordId=" + recordId +
                ", appendedType=" + appendedType +
                ", farmingName='" + farmingName + '\'' +
                ", category=" + category +
                ", status=" + status +
                '}';
    }
}
