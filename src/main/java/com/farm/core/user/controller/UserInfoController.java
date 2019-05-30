package com.farm.core.user.controller;

import com.farm.base.BaseException;
import com.farm.base.common.JsonResult;
import com.farm.base.exception.FarmException;
import com.farm.base.exception.UploadException;
import com.farm.base.farm.Farm;
import com.farm.base.farm.FarmAssess;
import com.farm.base.farm.Plot;
import com.farm.base.farm.PlotCrop;
import com.farm.base.user.User;
import com.farm.core.config.LoginCheck;
import com.farm.core.record.PlotCropParams;
import com.farm.core.user.exception.UserException;
import com.farm.core.user.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 ** @Version 1.0.0
 */
@RequestMapping("api/user")
@RestController
public class UserInfoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    private UserInfoService userInfoService;


    @GetMapping("info")
    @LoginCheck
    public JsonResult getCurrentUser(@RequestParam(value = "plotCropId", required = false) String plotCropId) throws UserException {
        LOGGER.info("请求开始报告: 获取个人首页信息接口 参数 plotCropId:{}", plotCropId);
        Map<String, Object> map = this.userInfoService.getCurrentUser(plotCropId);
        return JsonResult.ok(map);
    }

    @PostMapping("update")
    @LoginCheck
    public JsonResult updateUserInfo(User user) throws UserException {
        LOGGER.info("请求开始报告: 修改用户信息接口 参数:{}", user);
        this.userInfoService.updateUser(user);
        return JsonResult.ok();
    }

    @GetMapping("farm")
    @LoginCheck
    public JsonResult listFarmByUser() throws UserException {
        LOGGER.info("请求开始报告: 获取当前用户农场信息");
        List<Farm> farmList = this.userInfoService.listFarmByUser();
        return JsonResult.ok(farmList);
    }

    @GetMapping("farm/{farmId}")
    @LoginCheck
    public JsonResult getCurrentFarm(@PathVariable("farmId") Long farmId) throws UserException, FarmException {
        LOGGER.info("请求开始报告: 获取单个农场详情 参数 farmId:{}", farmId);
        Map<String, Object> map = this.userInfoService.getCurrentFarm(farmId);
        return JsonResult.ok(map);
    }


    @GetMapping("cropVariety")
    @LoginCheck
    public JsonResult listPlotCropByFarm(PlotCropParams params) throws UserException {
        LOGGER.info("请求开始报告: 获取当前农场下所有品种作物信息 参数:{}", params);
        List<Map<String, Object>> resultList = this.userInfoService.listPlotCropByFarm(params);
        return JsonResult.ok(resultList);
    }


    @GetMapping("farmAssess")
    @LoginCheck
    public JsonResult listByFarmId(@RequestParam("farmId") Long farmId) throws UserException {
        LOGGER.info("请求开始报告: 获取当前农场；评估报告 参数:{}", farmId);
        List<FarmAssess> resultList = this.userInfoService.listFarmAssessByFarmId(farmId);
        return JsonResult.ok(resultList);
    }

    @GetMapping("varietyDetail")
    @LoginCheck
    public JsonResult getCropVarietyDetail(@RequestParam("plotCropId") Long plotCropId) throws UploadException, ParseException, UserException {
        LOGGER.info("请求开始报告: 作物品种时间轴 参数 plotCropId :{}", plotCropId);
        Map<String, Object> map = this.userInfoService.getCropVarietyDetail(plotCropId);
        return JsonResult.ok(map);
    }

    @GetMapping("plot")
    @LoginCheck
    public JsonResult listPlotByFarmIdAndType(@RequestParam("farmId") Long farmId,
                                              @RequestParam("plotType") Integer plotType) throws BaseException {
        LOGGER.info("请求开始报告: 获取地块信息 参数 farmId:{},plotType:{}", farmId, plotType);
        List<Plot> plotByFarmIdAndType = this.userInfoService.getPlotByFarmIdAndType(farmId, plotType);
        return JsonResult.ok(plotByFarmIdAndType);
    }


    @PostMapping("defaultFarm")
    @LoginCheck
    public JsonResult defaultFarm(Long farmId) throws BaseException {
        LOGGER.info("请求开始报告: 更新用户默认农场id farmId:{}", farmId);
        this.userInfoService.updateUser(farmId);
        return JsonResult.ok();
    }


}
