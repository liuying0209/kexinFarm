package com.farm.core.farm.mapper;

import com.farm.base.farm.FarmWork;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FarmWorkMapper {

    /**
     * 查询所有类型是追加农事环节
     * @return
     */
    List<FarmWork> listAddFarmWork();

    FarmWork findById(Long id);

    List<FarmWork> findByParentId(Long id);

}