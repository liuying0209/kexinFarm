package com.farm.core.user.mapper;

import com.farm.base.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 ** @Date: 2019-04-20 19:42
 */
@Mapper
public interface UserMapper {

    int insert(User user);

    int update(User user);

    @Update("update user set `default_farm_id`=null where id=#{userId}")
    int setFarmIdNull(String userId);

    User findById(String id);

    List<User> findByMobileNumber(String username);

    int delete(String id);

}
