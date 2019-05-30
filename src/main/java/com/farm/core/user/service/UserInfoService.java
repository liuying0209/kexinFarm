package com.farm.core.user.service;

import com.farm.base.BaseException;
import com.farm.base.exception.FarmException;
import com.farm.base.exception.RecordException;
import com.farm.base.exception.UploadException;
import com.farm.base.farm.Farm;
import com.farm.base.farm.FarmAssess;
import com.farm.base.farm.Plot;
import com.farm.base.farm.PlotCrop;
import com.farm.base.user.User;
import com.farm.core.record.PlotCropParams;
import com.farm.core.user.exception.UserException;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 ** @Version 1.0.0
 */
public interface UserInfoService {


    /**
     * 获取个人用户完善资料接口
     */

    Map<String, Object> getCurrentUser(String plotCropId) throws UserException;


    /**
     * 更新用户信息
     *
     * @param user
     * @throws UserException
     */
    void updateUser(User user) throws UserException;

    /**
     * 更新用户默认展示农场
     */
    void updateUser(Long farmId) throws RecordException;

    /**
     * 获取当前用户农场信息列表
     */
    List<Farm> listFarmByUser() throws UserException;

    /**
     * 获取用户农场详情
     */

    Map<String, Object> getCurrentFarm(Long farmId) throws UserException, FarmException;

    /**
     * 当前农场下的品种作物信息
     */

    List<Map<String,Object>> listPlotCropByFarm(PlotCropParams param) throws UserException;

    /**
     * 品种作物信息时间轴
     */
    Map<String, Object> getCropVarietyDetail(Long plotCropId) throws UserException, UploadException, ParseException;

    /**
     * 获取评估报告
     */

    List<FarmAssess> listFarmAssessByFarmId(Long farmId) throws UserException;


    /**
     * 当前农场下的所有地块
     */

    List<Plot> getPlotByFarmIdAndType(Long farmId,Integer plotType) throws BaseException;


}
