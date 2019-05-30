package com.farm.core.user.mapper;

import com.farm.base.common.enums.SexEnum;
import com.farm.base.common.enums.UserStatusEnum;
import com.farm.base.user.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;

import java.util.Date;


/**
 ** @Date: 2019-04-20 21:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    UserMapper userMapper;

    @Test
    @Commit
    public void testInsert() {

        Date date = new Date();
        User user = new User();
        user.setId("h17611013091");
        user.setMobileNumber("17611013091");
        user.setRealName("邱玮");
        user.setSex(SexEnum.MAN);
        user.setBirthday(date);
        user.setEmail("Wcbc0310@126.com");
        user.setPassword("123456");
        user.setStatus(UserStatusEnum.USABLE);
        user.setIdCard("352227199303100556");
        user.setIdCardImage("front.jpg");
        user.setFarmImage("reverse.jpg");
        user.setBusinessLicense("a.jpg,b.jpg");
        user.setQualification("c.jpg,d.jpg");
        user.setPhoto("photo.jpg");
        user.setRegisterTime(date);
        user.setSource("DD");

        int row = this.userMapper.insert(user);
        Assert.assertEquals(row,1);


        user.setSource("app");
        int updateRow = this.userMapper.update(user);
        Assert.assertEquals(updateRow,1);

        User result = this.userMapper.findById("h17611013091");


        Assert.assertEquals(result.getMobileNumber(),"17611013091");


        int delete = this.userMapper.delete("h17611013091");
        Assert.assertEquals(delete,1);


    }


    @AfterTransaction
    public void clear() {
        deleteFromTables("admin");
    }
}