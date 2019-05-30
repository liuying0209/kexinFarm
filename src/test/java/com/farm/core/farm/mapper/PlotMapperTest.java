package com.farm.core.farm.mapper;

import com.farm.base.farm.Plot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 ** @Date: 2019-04-26 00:05
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PlotMapperTest {

    @Autowired
    PlotMapper plotMapper;


    @Test
    public void test(){

        List<Plot> plots = this.plotMapper.listByFarmIdAndType(1L, 1);

    }
}