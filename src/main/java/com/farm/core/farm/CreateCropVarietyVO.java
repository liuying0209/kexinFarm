package com.farm.core.farm;

import java.io.Serializable;

/**
 * 创建作物品种参数类
 *
 ** @Date: 2019-04-26 11:24
 */
public class CreateCropVarietyVO implements Serializable {

    private static final long serialVersionUID = 2897856757637249657L;
    /**
     * 品种名称
     */

    private String cropVariety;
    /**
     * 批次时间 2018-01-01
     */
    private String batchTime;
    /**
     * 面积
     */
    private Integer area;

    /**
     * 地块编号
     */

    private Long plotId;

    /**
     * 作物编号
     */
    private Long cropId;

    public CreateCropVarietyVO() {
    }

    public String getCropVariety() {
        return cropVariety;
    }

    public void setCropVariety(String cropVariety) {
        this.cropVariety = cropVariety;
    }

    public String getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(String batchTime) {
        this.batchTime = batchTime;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Long getPlotId() {
        return plotId;
    }

    public void setPlotId(Long plotId) {
        this.plotId = plotId;
    }

    public Long getCropId() {
        return cropId;
    }

    public void setCropId(Long cropId) {
        this.cropId = cropId;
    }

    @Override
    public String toString() {
        return "CreateCropVarietyVO{" +
                "cropVariety='" + cropVariety + '\'' +
                ", batchTime='" + batchTime + '\'' +
                ", area=" + area +
                ", plotId=" + plotId +
                ", cropId=" + cropId +
                '}';
    }
}
