package com.farm.core.user.mapper;

import com.farm.core.user.DdUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DdUserMapper {

    int delete(Long id);

    int update(DdUser record);

    int insert(DdUser record);

    DdUser findById(Long id);

    DdUser findByDdId(String ddUserId);
}