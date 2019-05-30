package com.farm.core.farm.mapper;

import com.farm.base.farm.Plot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlotMapper {

    int delete(Long id);

    int deleteByFarmId(Long farmId);

    int insert(Plot record);


    Plot findById(Long id);

    int update(Plot record);

    List<Plot> listByFarmIdAndType(@Param("farmId") Long farmId , @Param("type") Integer type);



}