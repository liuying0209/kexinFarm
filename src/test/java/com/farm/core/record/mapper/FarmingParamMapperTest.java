package com.farm.core.record.mapper;

import com.farm.base.record.FarmingParam;
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
public class FarmingParamMapperTest {

    @Autowired
    private FarmingParamMapper farmingParamMapper;

    @Test
    public void test(){
        FarmingParam param = new FarmingParam();
        param.setRecordId(1L);
        param.setContent("你是我的文档中心");
        this.farmingParamMapper.insert(param);

    }

    @Test
    public void testFind(){

        FarmingParam byId = this.farmingParamMapper.findById(1L);

    }


}