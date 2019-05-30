package com.farm.core.record.mapper;

import com.farm.base.record.FarmingParamTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 ** @Version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FarmingParamTemplateMapperTest {

    @Autowired
    FarmingParamTemplateMapper farmingParamTemplateMapper;

    @Test
    public void testFind(){

        FarmingParamTemplate byFarmingIdAndCropId = this.farmingParamTemplateMapper.findByFarmingIdAndCropId(24L, 1L);

        System.out.println(byFarmingIdAndCropId);

    }

}