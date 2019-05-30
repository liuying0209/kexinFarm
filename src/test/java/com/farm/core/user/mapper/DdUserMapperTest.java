package com.farm.core.user.mapper;

import com.farm.core.user.DdUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 ** @Version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DdUserMapperTest {

    @Autowired
    DdUserMapper ddUserMapper;

    @Test
    public void testUser(){

        DdUser ddUser = this.ddUserMapper.findByDdId("manager9087");
        System.out.println(ddUser.toString());


    }


}