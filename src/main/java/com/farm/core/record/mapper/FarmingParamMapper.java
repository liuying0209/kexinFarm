package com.farm.core.record.mapper;

import com.farm.base.record.FarmingParam;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FarmingParamMapper {

    int insert(FarmingParam param);

    int update(FarmingParam param);

    int updateByRecordId(FarmingParam param);

    FarmingParam findById(Long id);

    FarmingParam findByRecordId(Long recordId);

    int delete(Long id);

}