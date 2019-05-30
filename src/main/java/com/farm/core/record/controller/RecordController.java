package com.farm.core.record.controller;

import com.alibaba.fastjson.JSONObject;
import com.farm.base.BaseException;
import com.farm.base.common.JsonResult;
import com.farm.base.farm.FarmWork;
import com.farm.core.config.LoginCheck;
import com.farm.core.record.AppendedVO;
import com.farm.core.record.FarmingRecordVO;
import com.farm.core.record.dto.AllParentRecordDTO;
import com.farm.core.record.dto.RecordParamDTO;
import com.farm.core.record.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 ** @Version 1.0.0
 */
@RequestMapping("api/record")
@RestController
public class RecordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordController.class);

    @Autowired
    private RecordService recordService;

    @PostMapping("start/{plotCropId}/{farmId}")
    @LoginCheck
    public JsonResult startPlant(@PathVariable("plotCropId") Long plotCropId,
                                 @PathVariable("farmId") Long farmId) throws BaseException {
        LOGGER.info("请求开始报告: 开始种植接口 参数 plotCropId:{},farmId:{}", plotCropId, farmId);
        this.recordService.startPlant(plotCropId, farmId);
        return JsonResult.ok();
    }


    @GetMapping("addFarmWork")
    @LoginCheck
    public JsonResult listAddFarmWork() {
        LOGGER.info("请求开始报告: 查询追加农事集合");
        List<FarmWork> farmWorks = this.recordService.listAddFarmWork();
        return JsonResult.ok(farmWorks);
    }


    @GetMapping("listRecordMenu/{plotCropId}")
    @LoginCheck
    public JsonResult ListAllParentAndAddRecord(@PathVariable("plotCropId") Long plotCropId) {
        LOGGER.info("请求开始报告: 查询所有父类农事环节接口 参数 plotCropId : {}", plotCropId);
        List<AllParentRecordDTO> list = this.recordService.ListAllParentAndAddRecord(plotCropId);
        return JsonResult.ok(list);
    }

    @PostMapping("appended")
    @LoginCheck
    public JsonResult appended(AppendedVO params) throws BaseException {
        LOGGER.info("请求开始报告: 追加农事环节接口 参数 params:{}", params);
        Long recordId = this.recordService.addAppendFarming(params);
        Map<String, Object> map = new HashMap<>();
        map.put("recordId", recordId);
        return JsonResult.ok(map);
    }

    @GetMapping("getParams")
    @LoginCheck
    public JsonResult getParamsByCategory(@RequestParam("recordId") Long recordId,
                                          @RequestParam("category") Integer category) throws BaseException {
        LOGGER.info("请求开始报告: 获取农事环节页面参数 参数 recordId:{}，category:{}", recordId, category);
        List<RecordParamDTO> resultList = this.recordService.getParamsByCategory(category, recordId);
        return JsonResult.ok(resultList);
    }

    @PostMapping("add")
    @LoginCheck
    public JsonResult saveFarmingRecord(FarmingRecordVO farmingRecordVO) throws BaseException, ParseException {
        LOGGER.info("请求开始报告: 保存农事环节记录 参数:{}", JSONObject.toJSONString(farmingRecordVO));
        this.recordService.saveFarmingParams(farmingRecordVO);
        return JsonResult.ok();
    }

}
