package com.farm.core;

import com.farm.core.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 ** @Date: 2019-04-21 10:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {



    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("qw","1234");
        Object qw = redisTemplate.opsForValue().get("qw");
        System.out.println(qw.toString());
    }

    @Test
    public void testRedisUtil(){
        redisUtil.set("abc","你好");
        Object abc = redisUtil.get("abc");
        System.out.println(abc.toString());
    }

}