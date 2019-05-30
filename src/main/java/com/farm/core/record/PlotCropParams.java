package com.farm.core.record;

import java.io.Serializable;

/**
 * 查询品种作物页面参数
 *
 ** @Version 1.0.0
 */
public class PlotCropParams implements Serializable {
    private static final long serialVersionUID = -7383859642879150793L;


    private String farmId;
    private String plotId;
    private String CropId;
    private String Status;

    public PlotCropParams() {
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getPlotId() {
        return plotId;
    }

    public void setPlotId(String plotId) {
        this.plotId = plotId;
    }

    public String getCropId() {
        return CropId;
    }

    public void setCropId(String cropId) {
        CropId = cropId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public String toString() {
        return "PlotCropParams{" +
                "farmId='" + farmId + '\'' +
                ", plotId='" + plotId + '\'' +
                ", CropId='" + CropId + '\'' +
                ", Status='" + Status + '\'' +
                '}';
    }
}
