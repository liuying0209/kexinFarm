package com.farm.core.record.service;

/**
 ** @Version 1.0.0
 */
public interface FarmingParamService {

    /**
     * 根据记录id,作物id,农事环节id
     */
    void copyTemplateToParam(Long recordId,Long cropId,Long farmingId) ;

}
