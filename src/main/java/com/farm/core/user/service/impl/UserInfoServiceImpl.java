package com.farm.core.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.farm.base.BaseException;
import com.farm.base.common.enums.SexEnum;
import com.farm.base.common.enums.UserStatusEnum;
import com.farm.base.exception.FarmException;
import com.farm.base.exception.RecordException;
import com.farm.base.exception.UploadException;
import com.farm.base.farm.*;
import com.farm.base.record.FarmingParam;
import com.farm.base.record.FarmingRecord;
import com.farm.base.user.User;
import com.farm.core.farm.mapper.*;
import com.farm.base.record.ParamContent;
import com.farm.core.record.PlotCropParams;
import com.farm.core.record.mapper.FarmingParamMapper;
import com.farm.core.record.mapper.FarmingRecordMapper;
import com.farm.core.upload.service.UploadService;
import com.farm.core.user.exception.UserException;
import com.farm.core.user.mapper.UserMapper;
import com.farm.core.user.service.UserInfoService;
import com.farm.core.util.RedisUtil;
import com.farm.core.util.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 ** @Version 1.0.0
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    private FarmMapper farmMapper;
    private UserMapper userMapper;
    private PlotCropMapper plotCropMapper;
    private FarmingRecordMapper farmingRecordMapper;
    private FarmAssessMapper farmAssessMapper;
    private FarmingParamMapper farmingParamMapper;
    private UploadService uploadService;
    private PlotMapper plotMapper;
    private CropMapper cropMapper;
    private RedisUtil redisUtil;


    @Autowired
    @SuppressWarnings("all")
    public UserInfoServiceImpl(FarmMapper farmMapper, UserMapper userMapper, PlotCropMapper plotCropMapper,
                               FarmingRecordMapper farmingRecordMapper, FarmAssessMapper farmAssessMapper,
                               FarmingParamMapper farmingParamMapper, UploadService uploadService, PlotMapper plotMapper,
                               CropMapper cropMapper, RedisUtil redisUtil) {
        this.farmMapper = farmMapper;
        this.userMapper = userMapper;
        this.plotCropMapper = plotCropMapper;
        this.farmingRecordMapper = farmingRecordMapper;
        this.farmAssessMapper = farmAssessMapper;
        this.farmingParamMapper = farmingParamMapper;
        this.uploadService = uploadService;
        this.plotMapper = plotMapper;
        this.cropMapper = cropMapper;
        this.redisUtil = redisUtil;
    }

    @Override
    public Map<String, Object> getCurrentUser(String plotCropId) throws UserException {
        User user = UserUtils.getCurrent();
        if (user == null) {
            LOGGER.error("用户登入信息有误");
            throw new UserException(UserException.LOGIN_USWENAME_ERROE);
        }
        String idCardImage = user.getIdCardImage();
        String farmImage = user.getFarmImage();
        String businessLicense = user.getBusinessLicense();
        String qualification = user.getQualification();

        Integer num = 0;

        if (StringUtils.isNotBlank(idCardImage)) {
            num += 1;
        }
        if (StringUtils.isNotBlank(farmImage)) {
            num += 1;
        }

        if (StringUtils.isNotBlank(businessLicense)) {
            num += 1;
        }
        if (StringUtils.isNotBlank(qualification)) {
            num += 1;
        }


        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("mobileNumber", user.getMobileNumber());
        map.put("realName", user.getRealName());
        map.put("email", StringUtils.isBlank(user.getEmail()) ? "" : user.getEmail());
        map.put("idCardImage", StringUtils.isBlank(idCardImage) ? "" : idCardImage);
        map.put("farmImage", StringUtils.isBlank(farmImage) ? "" : farmImage);
        map.put("businessLicense", StringUtils.isBlank(businessLicense) ? "" : businessLicense);
        map.put("qualification", StringUtils.isBlank(qualification) ? "" : qualification);
        map.put("answerScore", user.getAnswerScore());
        map.put("photo", user.getPhoto());

        Long defaultFarmId = user.getDefaultFarmId();
        Farm farm = null;
        if (defaultFarmId != null) {
            farm = this.farmMapper.findById(defaultFarmId);
        } else {
            farm = this.farmMapper.findOneByUserId(user.getId());
        }

        //添加redis中保留的作物id
        if (StringUtils.isBlank(plotCropId) && farm != null) {
            Object obj = redisUtil.get(farm.getId().toString() + user.getId());
            if (obj != null) {
                plotCropId = obj.toString();
            }
        }

        map.put("farm", farm);
        Map<String, Object> plotCropMap = new HashMap<>();
        if (StringUtils.isNotBlank(plotCropId)) {
            PlotCrop plotCrop = this.plotCropMapper.findById(Long.parseLong(plotCropId));

            if (plotCrop != null) {
                plotCropMap.put("id", plotCrop.getId());
                plotCropMap.put("cropVariety", plotCrop.getCropVariety());
                plotCropMap.put("cropName", plotCrop.getCropName());
                plotCropMap.put("plotName", plotCrop.getPlotName());
                plotCropMap.put("userId", plotCrop.getUserId());
                plotCropMap.put("status", plotCrop.getStatus());
                this.redisUtil.set(farm.getId().toString() + user.getId(), plotCropId);
            }
        } else {
            if (farm != null) {
                plotCropMap = this.plotCropMapper.findByUserIdAndFarmId(farm.getId(), user.getId());
            }
        }

        Integer dataIntegrity = (num * 100) / 4;
        map.put("plotCrop", plotCropMap);
        //资料完善度
        map.put("dataIntegrity", dataIntegrity.toString() + "%");

        return map;
    }

    @Override
    public void updateUser(User user) throws UserException {
        String userId = user.getId();
        if (StringUtils.isBlank(userId)) {
            LOGGER.error("必要参数 userId 为空 ");
            throw new UserException(UserException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }
        this.userMapper.update(user);

    }

    @Override
    public void updateUser(Long farmId) throws RecordException {
        if (farmId == null) {
            LOGGER.error("必要参数 farmId :{}", farmId);
            throw new RecordException(RecordException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }

        User current = UserUtils.getCurrent();
        current.setDefaultFarmId(farmId);
        this.userMapper.update(current);


    }

    @Override
    public List<Farm> listFarmByUser() throws UserException {
        User user = UserUtils.getCurrent();
        if (user == null) {
            LOGGER.error("用户登入信息有误");
            throw new UserException(UserException.LOGIN_USWENAME_ERROE);
        }
        List<Farm> list = this.farmMapper.findByUserId(user.getId());

        return list;
    }

    @Override
    public Map<String, Object> getCurrentFarm(Long farmId) throws UserException, FarmException {

        if (farmId == null) {
            LOGGER.error("必要参数farmId 为空 ");
            throw new UserException(UserException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }
        User user = UserUtils.getCurrent();
        String userId = user.getId();
        Farm farm = this.farmMapper.findByIdAndUserId(farmId, userId);
        if (farm == null) {
            LOGGER.error("农场信息有误 farmId:{}", farmId);
            throw new FarmException(FarmException.FARM_INFO_ERROR);
        }

        Integer area = farm.getArea();
        Integer currentArea = this.plotCropMapper.countArea(farmId);
        Integer coverArea = 0;
        if (area != null && area > 0) {
            coverArea = (currentArea * 100) / area;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", farm.getId());
        map.put("name", farm.getName());
        map.put("score", farm.getScore());
        map.put("updateTime", farm.getUpdateTime());
        map.put("coverArea", coverArea.toString() + "%");
        int assessCount = farmAssessMapper.countByFarmId(farmId);
        map.put("assessCount", assessCount);

        return map;
    }

    @Override
    public List<Map<String, Object>> listPlotCropByFarm(PlotCropParams param) throws UserException {
        String farmId = param.getFarmId();
        if (StringUtils.isBlank(farmId)) {
            LOGGER.error("必要参数 farmId 为空 ");
            throw new UserException(UserException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }
        List<Map<String, Object>> resultList = this.plotCropMapper.findByFarmIdAndUserId(param);
        return resultList;
    }

    @Override
    public Map<String, Object> getCropVarietyDetail(Long plotCropId) throws UserException {

        if (plotCropId == null) {
            LOGGER.error("必要参数 farmCropId 为空 ");
            throw new UserException(UserException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }
        PlotCrop plotCrop = this.plotCropMapper.findById(plotCropId);
        String plotName = plotCrop.getPlotName();
        String cropVariety = plotCrop.getCropVariety();
        Long cropId = plotCrop.getCropId();
        Crop crop = this.cropMapper.findById(cropId);
        String image = crop.getImage();
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("id", plotCrop.getId());
        tempMap.put("plotId", plotCrop.getPlotId());
        tempMap.put("cropVariety", plotCrop.getCropVariety());
        tempMap.put("plotName", plotCrop.getPlotName());
        tempMap.put("batchTime", plotCrop.getBatchTime());
        tempMap.put("score", plotCrop.getScore());
        tempMap.put("image", image);


        //获取品种作物追加的农事环节
        List<FarmingRecord> list = this.farmingRecordMapper.getPlotCropTimerShaft(plotCropId);

        List<Map<String, Object>> resultList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(list)) {

            for (FarmingRecord record : list) {
                Map<String, Object> map = new HashMap<>();

                Long recordId = record.getId();
                String farmWorkName = record.getFarmWorkName();
                Date createTime = record.getCreateTime();

                map.put("farmWorkName", farmWorkName);
                map.put("createTime", createTime);
                map.put("plotName", plotName);
                map.put("cropVariety", cropVariety);

                FarmingParam param = this.farmingParamMapper.findByRecordId(recordId);
                String content = param.getContent();
                List contentList = JSONObject.parseObject(content, List.class);

                List<ParamContent> arrayList = new ArrayList<>();

                for (Object obj : contentList) {
                    JSONObject jsonObject = (JSONObject) obj;
                    ParamContent paramContent = jsonObject.toJavaObject(ParamContent.class);
                    String flag = paramContent.getFlag();
                    String type = paramContent.getType();
                    if ("0".equals(flag) || "2".equals(type)) {
                        continue;
                    }

                    if ("0".equals(type)) {
                        arrayList.add(paramContent);
                        continue;
                    }
                    String key = paramContent.getName();
                    if ("肥料用量".equals(key)) {
                        map.put("weight", paramContent.getValue() + paramContent.getUnit());

                    }
                }

                map.put("images", arrayList);

                resultList.add(map);
            }

        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("cropInfo", tempMap);
        resultMap.put("timerShaft", resultList);

        return resultMap;
    }


    @Override
    public List<FarmAssess> listFarmAssessByFarmId(Long farmId) throws UserException {
        if (farmId == null) {
            LOGGER.error("必要参数 farmId 为空 ");
            throw new UserException(UserException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }
        List<FarmAssess> list = this.farmAssessMapper.listByFarmId(farmId);

        return list;
    }

    @Override
    public List<Plot> getPlotByFarmIdAndType(Long farmId, Integer plotType) throws BaseException {
        if (farmId == null || plotType == null) {
            LOGGER.error("必要参数为空");
            throw new BaseException(BaseException.ERROR_CODE_ILLEGAL_ARGUMENTS);

        }
        List<Plot> plots = this.plotMapper.listByFarmIdAndType(farmId, plotType);
        return plots;
    }

}
