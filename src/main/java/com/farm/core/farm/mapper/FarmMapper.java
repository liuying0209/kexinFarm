package com.farm.core.farm.mapper;

import com.farm.base.farm.Farm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FarmMapper {


    int delete(Long id);

    int insert(Farm record);


    Farm findById(Long id);

    Farm findByIdAndUserId(@Param("id") Long id, @Param("userId") String userId);

    List<Farm> findByUserId(String userId);

    Farm findOneByUserId(String userId);


    int update(Farm record);

    //获取所有农场信息

    List<Farm> listAll();
}