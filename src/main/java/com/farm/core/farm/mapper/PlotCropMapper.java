package com.farm.core.farm.mapper;

import com.farm.base.farm.PlotCrop;
import com.farm.core.record.PlotCropParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PlotCropMapper {

    int delete(Long id);

    int insert(PlotCrop record);

    PlotCrop findById(Long id);

    Map<String, Object> findByUserId(String userId);

    Map<String, Object> findByUserIdAndFarmId(@Param("farmId") Long farmId,@Param("userId") String userId);

    List<Map<String, Object>> findByFarmIdAndUserId(PlotCropParams params);

    List<Map<String, Object>> findByPlotId(Long plotId);

    List<Long> findPlotCropIdByFarmId(Long farmId);

    /**
     * 根据 地块id和 作物id
     *
     * @return
     */
    List<PlotCrop> findByPlotIdAndCropId(@Param("plotId") Long plotId, @Param("cropId") Long cropId);

    int update(PlotCrop record);

    //获取农场id
    long getFarmId(Long plotCropId);

    /**
     * 计算农场所有品种作物得分
     */
    int countFarmScore(Long farmId);

    /**
     * 查询总面积
     *
     * @param farmId
     * @return
     */
    int countArea(Long farmId);


    /**
     * 计算农场作物个数
     */
    int countPlotCropByFarmId(Long farmId);


    /**
     * 查询当前农场地块类型下 品种作物个数
     */
    int countByFarmAndPlotType(@Param("farmId") Long farmId, @Param("plotType") Integer plotType);

}