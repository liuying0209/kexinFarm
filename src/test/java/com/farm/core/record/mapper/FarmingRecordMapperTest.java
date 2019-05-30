package com.farm.core.record.mapper;

import com.farm.base.record.FarmingRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 ** @Version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FarmingRecordMapperTest {

    @Autowired
    private FarmingRecordMapper farmingRecordMapper;

    @Test
    public void test(){

        List<FarmingRecord> one = this.farmingRecordMapper.findByParentRecordId(14L,false);


        List<FarmingRecord> two = this.farmingRecordMapper.findByParentRecordId(14L, true);

        System.out.println("a");

    }


    @Test
    public void testCount(){

        int i = this.farmingRecordMapper.countScoreByPlotCropId(5L);
        System.out.println(i);

    }


}