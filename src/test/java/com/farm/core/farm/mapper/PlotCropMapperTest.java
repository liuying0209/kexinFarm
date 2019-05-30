package com.farm.core.farm.mapper;

import com.farm.base.farm.PlotCrop;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;


/**
 ** @Date: 2019-04-26 00:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PlotCropMapperTest {


    @Autowired
    PlotCropMapper plotCropMapper;

    @Test
    public void testFindByPlotId(){

        List<Map<String, Object>> list = this.plotCropMapper.findByPlotId(3L);
        System.out.println(list);

        boolean empty = CollectionUtils.isEmpty(list);
        System.out.println(empty);


    }

    @Test
    public void testFindByPlotIdAndCropId(){
        List<PlotCrop> byPlotIdAndCropId = this.plotCropMapper.findByPlotIdAndCropId(1L, 1L);
        System.out.println(byPlotIdAndCropId.toString());

    }

    @Test
    public void testGetFarmId(){

        long farmId = this.plotCropMapper.getFarmId(1L);
        System.out.println(farmId);
    }


    @Test
    public void testFindById(){

        PlotCrop plotCrop = this.plotCropMapper.findById(15L);

        System.out.println(plotCrop.toString());

    }

}