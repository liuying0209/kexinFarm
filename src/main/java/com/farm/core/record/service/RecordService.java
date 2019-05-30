package com.farm.core.record.service;

import com.farm.base.BaseException;
import com.farm.base.exception.FarmException;
import com.farm.base.exception.RecordException;
import com.farm.base.farm.FarmWork;
import com.farm.core.record.AppendedVO;
import com.farm.core.record.FarmingRecordVO;
import com.farm.core.record.dto.AllParentRecordDTO;
import com.farm.core.record.dto.RecordParamDTO;
import org.omg.PortableInterceptor.INACTIVE;

import java.text.ParseException;
import java.util.List;

/**
 ** @Version 1.0.0
 */
public interface RecordService {

    /**
     * 开始种植
     *
     * @param plotCropId 品种作物id
     * @throws BaseException
     */
    void startPlant(Long plotCropId, Long farmId) throws BaseException;


    /**
     * 查询所有追加农事环节
     */
    List<FarmWork> listAddFarmWork();

    /**
     * 查询品种下所有父节点农事环节 和相应追加项列表
     *
     * @param plotCropId 品种作物id
     */
    List<AllParentRecordDTO> ListAllParentAndAddRecord(Long plotCropId);


    /**
     * 追加农事环节
     *
     * @return
     */
    Long addAppendFarming(AppendedVO params) throws RecordException, FarmException;


    /**
     * 获取农事环节页面参数
     * 0-直接拍摄  1-前置任务  2-细分子任务
     */
    List<RecordParamDTO> getParamsByCategory(Integer category, Long recordId) throws RecordException;


    /**
     * 完成农事后点击提交
     */

    void saveFarmingParams(FarmingRecordVO farmingRecordVO) throws FarmException, ParseException;


}
