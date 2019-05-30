package com.farm.core.farm.service;

import com.alibaba.fastjson.JSONObject;
import com.farm.base.exception.FarmException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 ** @Date: 2019-04-26 09:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FarmServiceTest {

    @Autowired
    FarmService farmService;

    @Test
    public void testGetLinkageMenu() throws FarmException {

        List<Map<String, Object>> linkageMenu = this.farmService.getLinkageMenu(1L, 0,null);


        String json = JSONObject.toJSONString(linkageMenu);
        System.out.println(json);


    }


}