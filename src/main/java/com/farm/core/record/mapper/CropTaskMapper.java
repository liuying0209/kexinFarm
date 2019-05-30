package com.farm.core.record.mapper;

import com.farm.base.record.CropTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 ** @Version 1.0.0
 */
@Mapper
public interface CropTaskMapper {

    int insert(CropTask cropTask);

    int update(CropTask cropTask);

    CropTask findById(Long id);

    /**
     * 查询父节点农事环节
     * @param cropId
     * @return
     */
    List<CropTask> findParentByCropId(Long cropId);

    List<CropTask> findSunByParentId(@Param("farmingId") Long farmingId, @Param("cropId") Long cropId);

    int delete(Long id);
}
