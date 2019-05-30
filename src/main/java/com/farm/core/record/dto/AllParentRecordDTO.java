package com.farm.core.record.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 所有父类节点农事环节 和追加农事环节DTO
 *
 ** @Version 1.0.0
 */
public class AllParentRecordDTO implements Serializable {

    /**
     * 记录编号
     */
    private Long recordId;

    /**
     * 农事环节id
     */

    private Long farmingId;

    /**
     * 农事环节名称
     */
    private String farmingName;

    /**
     * 是否有追加 0-否 1-是
     */

    private Integer addFlag;

    /**
     * 归类 0-直接拍摄  1-前置任务  2-细分子任务
     */

    private Integer category;


    /**
     *  完成状态 0-未开始 1-未完成，2-已完成
     */

    private Integer status;

    /**
     * 追加农事记录
     */
    List<AppendedDTO> list;


    public AllParentRecordDTO() {
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getFarmingId() {
        return farmingId;
    }

    public void setFarmingId(Long farmingId) {
        this.farmingId = farmingId;
    }

    public String getFarmingName() {
        return farmingName;
    }

    public void setFarmingName(String farmingName) {
        this.farmingName = farmingName;
    }

    public Integer getAddFlag() {
        return addFlag;
    }

    public void setAddFlag(Integer addFlag) {
        this.addFlag = addFlag;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public List<AppendedDTO> getList() {
        return list;
    }

    public void setList(List<AppendedDTO> list) {
        this.list = list;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AllParentRecordDTO{" +
                "recordId=" + recordId +
                ", farmingId=" + farmingId +
                ", farmingName='" + farmingName + '\'' +
                ", addFlag=" + addFlag +
                ", category=" + category +
                ", status=" + status +
                ", list=" + list +
                '}';
    }
}
