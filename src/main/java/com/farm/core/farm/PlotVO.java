package com.farm.core.farm;

import java.io.Serializable;

/**
 * 地块页面参数接口对象
 ** @Version 1.0.0
 */
public class PlotVO  implements Serializable {
    private static final long serialVersionUID = 6429228532180451476L;

    private Long plotId;
    private String name;

    public PlotVO() {
    }

    public Long getPlotId() {
        return plotId;
    }

    public void setPlotId(Long plotId) {
        this.plotId = plotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PlotVO{" +
                "plotId=" + plotId +
                ", name='" + name + '\'' +
                '}';
    }
}
