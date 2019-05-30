package com.farm.core.farm.controller;

import com.alibaba.fastjson.JSONObject;
import com.farm.base.common.JsonResult;
import com.farm.core.config.LoginCheck;
import com.farm.core.farm.CreateCropVarietyVO;
import com.farm.core.farm.CreateFarmVO;
import com.farm.core.farm.PlotVO;
import com.farm.base.exception.FarmException;
import com.farm.core.farm.service.FarmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 ** @Date: 2019-04-26 10:27
 */
@RequestMapping("api/farm")
@RestController
public class FarmController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FarmController.class);

    @Autowired

    @SuppressWarnings("all")
    FarmService farmService;

    @PostMapping("add")
    @LoginCheck
    public JsonResult saveFarm(CreateFarmVO params) throws FarmException {
        LOGGER.info("请求开始报告 : 创建农场 参数:{}", JSONObject.toJSONString(params));
        Long farmId = this.farmService.save(params);
        HashMap<String, Object> map = new HashMap<>();
        map.put("farmId", farmId);
        return JsonResult.ok(map);
    }


    @PostMapping("delete/{farmId}")
    @LoginCheck
    public JsonResult deleteFarm(@PathVariable("farmId") Long farmId) throws FarmException {
        LOGGER.info("请求开始报告 : 删除农场 farmId :{}", farmId);
        this.farmService.deleteFarm(farmId);
        return JsonResult.ok();
    }

    @PostMapping("update")
    @LoginCheck
    public JsonResult updateFarm(CreateFarmVO params) throws FarmException {
        LOGGER.info("请求开始报告 : 更新农场接口 参数 params:{}",params);
        this.farmService.updateFarm(params);
        return JsonResult.ok();
    }

    @GetMapping("menu")
    @LoginCheck
    public JsonResult getLinkageMenu(@RequestParam(value = "farmId") Long farmId,
                                     @RequestParam(value = "plotType") Integer plotType,
                                     @RequestParam(value = "switchFlag", required = false) String switchFlag) throws FarmException {
        LOGGER.info("请求开始报告 : 获取三级联动菜单 参数:farmId:{},plotType:{},switchFlag:{}",farmId,plotType,switchFlag);
        List<Map<String, Object>> list = this.farmService.getLinkageMenu(farmId, plotType, switchFlag);
        return JsonResult.ok(list);
    }


    @PostMapping("updatePlot")
    @LoginCheck
    public JsonResult updatePlot(PlotVO params) throws FarmException {
        LOGGER.info("请求开始报告 : 更新地块名称 参数 params: {}",params);
        this.farmService.updatePlot(params);
        return JsonResult.ok();
    }

    @PostMapping("addCropVariety")
    @LoginCheck
    public JsonResult addCropVariety(CreateCropVarietyVO params) throws FarmException {
        LOGGER.info("请求开始报告 : 地块添加农作物 参数 params:{}",params);
        Long plotCropId = this.farmService.addCropVariety(params);
        HashMap<String, Object> map = new HashMap<>();
        map.put("plotCropId", plotCropId);
        return JsonResult.ok(map);
    }


}
