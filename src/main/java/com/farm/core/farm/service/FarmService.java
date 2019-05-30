package com.farm.core.farm.service;

import com.farm.core.farm.CreateCropVarietyVO;
import com.farm.core.farm.CreateFarmVO;
import com.farm.core.farm.PlotVO;
import com.farm.base.exception.FarmException;
import com.farm.core.user.UserInfoDTO;
import com.farm.core.user.exception.UserException;

import java.util.List;
import java.util.Map;

/**
 ** @Date: 2019-04-25 20:44
 */
public interface FarmService {

    /**
     * 创建农场
     * @param params
     * @return farmId
     * @throws FarmException
     */
    Long save(CreateFarmVO params) throws FarmException;

    void updateFarm(CreateFarmVO params) throws FarmException;

    /**
     * 删除农场
     */
    void deleteFarm(Long farmId) throws FarmException;

    /**
     * 查询农场关联作物信息
     *
     * @param farmId
     * @param plotType
     * @param switchFlag 开关标识  是否查询是切换标识
     * @return
     * @throws FarmException
     */
    List<Map<String ,Object>> getLinkageMenu(Long farmId, Integer plotType, String switchFlag) throws FarmException;

    /**
     * 根据地块id修改地块名称
     * @throws FarmException
     */
    void updatePlot(PlotVO params) throws FarmException;

    /**
     * 地块添加农作物
     *
     * @param params
     */
    Long addCropVariety(CreateCropVarietyVO params) throws FarmException;


    /**
     * 添加农场评估
     */

    void addFarmAssess();

}
