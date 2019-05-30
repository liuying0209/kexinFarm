package com.farm.core.farm.service;

import com.farm.base.common.enums.PlotTypeEnum;

/**
 ** @Date: 2019-04-25 21:38
 */
public interface PlotService {

    /**
     * 创建地块
     *
     * @param farmId 农场id
     * @param count  数量
     * @param type   地块类型
     */
    void savePlot(Long farmId, Integer count, PlotTypeEnum type);



}
