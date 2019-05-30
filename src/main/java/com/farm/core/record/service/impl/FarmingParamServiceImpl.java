package com.farm.core.record.service.impl;

import com.farm.base.record.FarmingParam;
import com.farm.base.record.FarmingParamTemplate;
import com.farm.core.record.mapper.FarmingParamMapper;
import com.farm.core.record.mapper.FarmingParamTemplateMapper;
import com.farm.core.record.service.FarmingParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 ** @Version 1.0.0
 */
@Service
public class FarmingParamServiceImpl implements FarmingParamService {
    private final static Logger LOGGER = LoggerFactory.getLogger(FarmingParamServiceImpl.class);


    private FarmingParamTemplateMapper farmingParamTemplateMapper;
    private FarmingParamMapper farmingParamMapper;

    @Autowired
    @SuppressWarnings("all")
    public FarmingParamServiceImpl(FarmingParamTemplateMapper farmingParamTemplateMapper, FarmingParamMapper farmingParamMapper) {
        this.farmingParamTemplateMapper = farmingParamTemplateMapper;
        this.farmingParamMapper = farmingParamMapper;
    }

    @Override
    public void copyTemplateToParam(Long recordId, Long cropId, Long farmingId) {

        FarmingParamTemplate template = this.farmingParamTemplateMapper.findByFarmingIdAndCropId(farmingId, cropId);
        if (template == null) {
            LOGGER.info("没有模板信息 cropId :{},farmingId:{}",cropId,farmingId);
            return;

        }
        FarmingParam farmingParam = new FarmingParam();
        farmingParam.setRecordId(recordId);
        farmingParam.setContent(template.getContent());
        this.farmingParamMapper.insert(farmingParam);


    }
}
