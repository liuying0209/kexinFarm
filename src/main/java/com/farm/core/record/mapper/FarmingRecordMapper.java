package com.farm.core.record.mapper;

import com.farm.base.record.FarmingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FarmingRecordMapper {

    int delete(Long id);

    int insert(FarmingRecord record);

    FarmingRecord findById(Long id);


    /**
     * 查询所有父类农事环节 节点
     * @param plotCropId 品种作物id
     * @return
     */
    List<FarmingRecord> findByPlotCropId(Long plotCropId);

    /**
     * 获取完成记录时间轴
     * @param plotCropId
     * @return
     */
    List<FarmingRecord> getPlotCropTimerShaft(Long plotCropId);

    /**
     * 查询追加农事环节记录
     * @param recordId
     * @return
     */

    List<FarmingRecord> findAppendedById(Long recordId);


    /**
     * 根据父节点记录id 查询子任务记录数据
     * @return
     */

    List<FarmingRecord> findByParentRecordId(@Param("recordId") Long recordId,@Param("flag") Boolean flag);

    /**
     * 针对 归类为2 的 父类记录id 查询是否有子类未完成
     * @return
     */
    int countSunUnfinished(Long recordId);


    int update(FarmingRecord record);

    /**
     * 计算当前品种作物农事环节得分 扣除评估
     * @param plotCropId
     * @return
     */
    int countScoreByPlotCropId(Long plotCropId);
}