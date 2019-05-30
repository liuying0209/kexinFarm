package com.farm.core.farm.service.impl;

import com.farm.base.common.enums.PlotTypeEnum;
import com.farm.base.farm.Plot;
import com.farm.core.farm.mapper.PlotMapper;
import com.farm.core.farm.service.PlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 ** @Date: 2019-04-25 21:39
 */
@Service
public class PlotServiceImpl implements PlotService {
    private final static Logger LOGGER = LoggerFactory.getLogger(PlotServiceImpl.class);

    @Autowired
    @SuppressWarnings("all")
    private PlotMapper plotMapper;


    @Override
    @Transactional
    public void savePlot(Long farmId, Integer count, PlotTypeEnum type) {

        for (int i = 0; i < count; i++) {
            Plot plot = new Plot();
            plot.setFarmId(farmId);
            plot.setType(type);
            String plotName = i + 1 + "号棚";
            plot.setName(plotName);
            this.plotMapper.insert(plot);
        }

    }

}
