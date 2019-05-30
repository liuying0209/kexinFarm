package com.farm.core.farm.mapper;

import com.farm.base.farm.FarmAssess;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FarmAssessMapper {

    int delete(Long id);

    int insert(FarmAssess record);

    FarmAssess findById(Long id);

    FarmAssess findByFarmId(Long FarmId);

    int countByFarmId(Long FarmId);

    List<FarmAssess> listByFarmId(Long FarmId);

}