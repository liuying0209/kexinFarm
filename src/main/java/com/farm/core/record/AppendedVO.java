package com.farm.core.record;

import java.io.Serializable;

/**
 * 追加农事环节页面参数对象
 *
 ** @Version 1.0.0
 */
public class AppendedVO implements Serializable {
    private static final long serialVersionUID = 6496359730765628226L;

    /**
     * 当前农事环节记录id 如 播种
     */
    private Long recordId;

    /**
     * 追加的农事环节id 如 化肥 这个农事环节id
     */
    private Long farmingId;


    public AppendedVO() {
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

    @Override
    public String toString() {
        return "AppendedVO{" +
                "recordId=" + recordId +
                ", farmingId=" + farmingId +
                '}';
    }
}
