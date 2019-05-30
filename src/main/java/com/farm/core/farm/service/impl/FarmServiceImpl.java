package com.farm.core.farm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.farm.base.common.enums.PlotTypeEnum;
import com.farm.base.farm.*;
import com.farm.base.user.User;
import com.farm.core.farm.CreateCropVarietyVO;
import com.farm.core.farm.CreateFarmVO;
import com.farm.core.farm.PlotVO;
import com.farm.base.exception.FarmException;
import com.farm.core.farm.mapper.*;
import com.farm.core.user.UserInfoDTO;
import com.farm.core.farm.service.FarmService;
import com.farm.core.farm.service.PlotService;
import com.farm.core.user.exception.UserException;
import com.farm.core.user.mapper.UserMapper;
import com.farm.core.util.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 ** @Date: 2019-04-25 21:13
 */
@Service
public class FarmServiceImpl implements FarmService {
    private final static Logger LOGGER = LoggerFactory.getLogger(FarmServiceImpl.class);

    private UserMapper userMapper;
    private FarmMapper farmMapper;
    private PlotService plotService;
    private PlotMapper plotMapper;
    private PlotCropMapper plotCropMapper;
    private CropMapper cropMapper;
    private FarmAssessMapper farmAssessMapper;

    @Autowired
    @SuppressWarnings("all")
    public FarmServiceImpl(UserMapper userMapper, FarmMapper farmMapper, PlotService plotService,
                           PlotMapper plotMapper, PlotCropMapper plotCropMapper, CropMapper cropMapper,
                           FarmAssessMapper farmAssessMapper) {
        this.userMapper = userMapper;
        this.farmMapper = farmMapper;
        this.plotService = plotService;
        this.plotMapper = plotMapper;
        this.plotCropMapper = plotCropMapper;
        this.cropMapper = cropMapper;
        this.farmAssessMapper = farmAssessMapper;
    }

    @Override
    @Transactional
    public Long save(CreateFarmVO params) throws FarmException {
        String farmName = params.getFarmName();
        Integer area = params.getArea();
        Integer brooderCount = params.getBrooderCount();
        Integer coolCount = params.getCoolCount();
        Integer answerScore = params.getAnswerScore();
        if (StringUtils.isBlank(farmName) || area == null) {
            LOGGER.error("必要参数为空  farmName : {}  ,area :{}  ", farmName, area);
            throw new FarmException(FarmException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }

        //获取但前用户
        User current = UserUtils.getCurrent();
        String userId = current.getId();
        //判断是否需要更新分数
        if (answerScore != null) {
            current.setAnswerScore(answerScore);
            userMapper.update(current);
        }

        //创建农场
        Farm farm = new Farm();
        farm.setName(farmName);
        farm.setArea(area);
        farm.setCreatorId(userId);
        farm.setBrooderCount(params.getBrooderCount());
        farm.setCoolCount(params.getCoolCount());
        farmMapper.insert(farm);

        Long farmId = farm.getId();

        //是否创建暖棚
        if (brooderCount != null && brooderCount > 0) {
            plotService.savePlot(farmId, brooderCount, PlotTypeEnum.BROODER);
        }
        if (coolCount != null && coolCount > 0) {
            plotService.savePlot(farmId, coolCount, PlotTypeEnum.COOL);
        }
        return farmId;
    }

    @Override
    public void updateFarm(CreateFarmVO params) throws FarmException {
        Long farmId = params.getFarmId();
        String farmName = params.getFarmName();
        Integer area = params.getArea();

        if (farmId == 0) {
            LOGGER.error("必要参数farmId为空");
            throw new FarmException(FarmException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }


        Farm farm = new Farm();
        farm.setId(farmId);
        farm.setName(farmName);
        farm.setArea(area);

        this.farmMapper.update(farm);

    }

    @Override
    public void deleteFarm(Long farmId) throws FarmException {

        if (farmId == null) {
            LOGGER.error("必要参数farmId为null");
            throw new FarmException(FarmException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }

        //删除品种作物信息
        List<Long> listId = this.plotCropMapper.findPlotCropIdByFarmId(farmId);
        if (CollectionUtils.isNotEmpty(listId)) {
            for (Long id : listId) {
                this.plotCropMapper.delete(id);
            }
        }
        this.plotMapper.deleteByFarmId(farmId);
        this.farmMapper.delete(farmId);
        User current = UserUtils.getCurrent();
        Farm farm = this.farmMapper.findOneByUserId(current.getId());
        LOGGER.info("设置默认农场:farm:{}", JSONObject.toJSONString(farm));
        if (farm != null) {
            current.setDefaultFarmId(farm.getId());
            this.userMapper.update(current);
        } else {
            this.userMapper.setFarmIdNull(current.getId());
        }


    }


    @Override
    public List<Map<String, Object>> getLinkageMenu(Long farmId, Integer plotType, String switchFlag) throws FarmException {

        LOGGER.info("参数 farmId:{} ,plotType:{},switchFlag:{}", farmId, plotType, switchFlag);

        if (farmId == null || plotType == null) {
            LOGGER.error("必要参数为空 farmId {} , plotType : {}", farmId, plotType);
            throw new FarmException(FarmException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }

        //TODO 待优化
        //根据地块类型查询 所有地块
        List<Plot> plots = plotMapper.listByFarmIdAndType(farmId, plotType);
        List<Map<String, Object>> resultList = new ArrayList<>();

        if (StringUtils.isBlank(switchFlag)) {
            return normalProcess(resultList, plots);
        }

        return switchCrops(resultList, plots, farmId, plotType);

    }

    /**
     * 正常展示联动列表流程
     *
     * @return
     * @throws FarmException
     */

    private List<Map<String, Object>> normalProcess(List<Map<String, Object>> resultList, List<Plot> plots) {
        if (CollectionUtils.isNotEmpty(plots)) {
            plots.stream().forEach(plot -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", plot.getId());
                map.put("name", plot.getName());
                List<Map<String, Object>> cropVe = this.plotCropMapper.findByPlotId(plot.getId());
                if (CollectionUtils.isNotEmpty(cropVe)) {
                    cropVe = cropVe.stream().map(variety -> {

                        Long cropId = (Long) variety.get("id");
                        List<PlotCrop> plotCropList = this.plotCropMapper.findByPlotIdAndCropId(plot.getId(), cropId);

                        List<Map<String, Object>> cropVarietyFinalList = plotCropList.stream().map(plotCrop -> {
                            String cropVariety = plotCrop.getCropVariety();
                            Long id = plotCrop.getId();
                            Map<String, Object> cropVarietyMap = new HashMap<>();
                            cropVarietyMap.put("id", id);
                            cropVarietyMap.put("name", cropVariety);
                            return cropVarietyMap;
                        }).collect(Collectors.toList());
                        variety.put("list", cropVarietyFinalList);
                        return variety;
                    }).collect(Collectors.toList());
                    map.put("list", cropVe);

                } else {
                    List<Crop> crops = this.cropMapper.listAll();
                    List<Map<String, Object>> cropFinalList = crops.stream().map(crop -> {
                        Map<String, Object> cropMap = new HashMap<>();
                        cropMap.put("id", crop.getId());
                        cropMap.put("name", crop.getName());
                        cropMap.put("list", null);
                        return cropMap;
                    }).collect(Collectors.toList());
                    map.put("list", cropFinalList);
                }

                resultList.add(map);

            });
        }

        return resultList;

    }

    /**
     * 切换品种作物流程
     *
     * @param farmId   农场id
     * @param plotType 地块类型
     * @return
     * @throws FarmException
     */

    private List<Map<String, Object>> switchCrops(List<Map<String, Object>> resultList, List<Plot> plots, Long farmId, Integer plotType) {

        int plotCropCount = this.plotCropMapper.countByFarmAndPlotType(farmId, plotType);
        if (plotCropCount > 0) {

            return getListWithCrop(resultList, plots);
        }
        return resultList;
    }


    /**
     * 没有品种作物三级联动
     *
     * @throws FarmException
     */
    private List<Map<String, Object>> getListNoCrop(List<Map<String, Object>> resultList, List<Plot> plots) {


        if (CollectionUtils.isNotEmpty(plots)) {
            for (Plot plot : plots) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", plot.getId());
                map.put("name", plot.getName());
                List<Crop> crops = this.cropMapper.listAll();
                List<Map<String, Object>> cropFinalList = crops.stream().map(crop -> {
                    Map<String, Object> cropMap = new HashMap<>();
                    cropMap.put("id", crop.getId());
                    cropMap.put("name", crop.getName());
                    cropMap.put("list", null);
                    return cropMap;
                }).collect(Collectors.toList());
                map.put("list", cropFinalList);
                resultList.add(map);

            }
        }
        return resultList;
    }


    /**
     * 没有品种作物三级联动
     *
     * @throws FarmException
     */
    private List<Map<String, Object>> getListWithCrop(List<Map<String, Object>> resultList, List<Plot> plots) {
        for (Plot plot : plots) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", plot.getId());
            map.put("name", plot.getName());
            List<Map<String, Object>> cropVe = this.plotCropMapper.findByPlotId(plot.getId());
            if (CollectionUtils.isNotEmpty(cropVe)) {
                cropVe = cropVe.stream().map(variety -> {

                    Long cropId = (Long) variety.get("id");
                    List<PlotCrop> plotCropList = this.plotCropMapper.findByPlotIdAndCropId(plot.getId(), cropId);

                    List<Map<String, Object>> cropVarietyFinalList = plotCropList.stream().map(plotCrop -> {
                        String cropVariety = plotCrop.getCropVariety();
                        Long id = plotCrop.getId();
                        Map<String, Object> cropVarietyMap = new HashMap<>();
                        cropVarietyMap.put("id", id);
                        cropVarietyMap.put("name", cropVariety);
                        return cropVarietyMap;
                    }).collect(Collectors.toList());
                    variety.put("list", cropVarietyFinalList);
                    return variety;
                }).collect(Collectors.toList());
                map.put("list", cropVe);
            } else {
                continue;
            }

            resultList.add(map);

        }
        return resultList;
    }


    @Override
    public void updatePlot(PlotVO params) throws FarmException {
        String name = params.getName();
        Long plotId = params.getPlotId();

        if (plotId == null || StringUtils.isBlank(name)) {
            LOGGER.error("必要参数为空 plotId : {},name :{} ", plotId, name);
            throw new FarmException(FarmException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }

        Plot plot = new Plot();
        plot.setId(plotId);
        plot.setName(name);
        this.plotMapper.update(plot);
    }

    @Override
    public Long addCropVariety(CreateCropVarietyVO params) throws FarmException {
        Integer area = params.getArea();
        String batchTime = params.getBatchTime();
        Long cropId = params.getCropId();
        String cropVariety = params.getCropVariety();
        Long plotId = params.getPlotId();

        if (area == null || StringUtils.isBlank(batchTime) || cropId == null ||
                StringUtils.isBlank(cropVariety) || plotId == null) {
            LOGGER.error("必要参数为空 {}", JSONObject.toJSONString(params));
            throw new FarmException(FarmException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }
        Plot plot = this.plotMapper.findById(plotId);
        if (plot == null) {
            LOGGER.error("获取地块信息有误");
            throw new FarmException(FarmException.PLOT_INFO_ERROR);
        }
        Crop crop = this.cropMapper.findById(cropId);
        if (crop == null) {
            LOGGER.error("获取作物信息有误");
            throw new FarmException(FarmException.CROP_INFO_ERROR);
        }
        User user = UserUtils.getCurrent();

        PlotCrop plotCrop = new PlotCrop();
        plotCrop.setArea(area);
        plotCrop.setBatchTime(batchTime);
        String substring = StringUtils.substring(batchTime, 5);
        plotCrop.setCropVariety(substring + cropVariety);
        plotCrop.setCropId(cropId);
        plotCrop.setCropName(crop.getName());
        plotCrop.setPlotId(plotId);
        plotCrop.setPlotName(plot.getName());
        plotCrop.setUserId(user.getId());

        this.plotCropMapper.insert(plotCrop);

        return plotCrop.getId();


    }

    @Override
    public void addFarmAssess() {
        //获取所有农场信息
        List<Farm> farms = this.farmMapper.listAll();

        //判断与前一次农场评估数据是否有变动
        farms.stream().forEach(farm -> {
            Long farmId = farm.getId();
            Integer score = farm.getScore();
            String farmName = farm.getName();


            FarmAssess farmAssess = this.farmAssessMapper.findByFarmId(farmId);

            FarmAssess assess = new FarmAssess();
            assess.setFarmId(farmId);
            assess.setFarmName(farmName);
            assess.setScore(score);

            if (farmAssess == null) {
                //表示第一次添加
                assess.setRemark("首次评估 农场分数为 :" + score + "分");

            } else {
                //表示更新评估报告
                Integer assessScore = farmAssess.getScore();
                Integer currentScore = score - assessScore;
                assess.setRemark("农场分数为 :" + score + "分 增加了 :" + currentScore + "分");
            }
            this.farmAssessMapper.insert(assess);
        });

    }


}
