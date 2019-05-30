package com.farm.core.scheduled;

import com.farm.core.farm.service.FarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 农场评估定时任务
 *
 ** @Date: 2019-1-22 14:25
 */
@Component
public class FarmAssessTask {
    private final static Logger LOGGER = LoggerFactory.getLogger(FarmAssessTask.class);

    @Autowired
    private FarmService farmService;
    /**
     * 定时执行评估 每星期一 凌晨 1点
     */
//    @Scheduled(cron = "0 0 1 ? * MON")
    private void createTaskForCallCenter() {

        LOGGER.info("开始执行定时任务");
        try{
            this.farmService.addFarmAssess();
        }catch (Exception e){

        }


    }


}
