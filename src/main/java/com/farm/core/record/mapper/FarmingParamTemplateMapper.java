package com.farm.core.record.mapper;

import com.farm.base.record.FarmingParamTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface FarmingParamTemplateMapper {

    FarmingParamTemplate findByFarmingIdAndCropId(@Param("farmingId") Long farmingId,@Param("cropId") Long cropId);

}