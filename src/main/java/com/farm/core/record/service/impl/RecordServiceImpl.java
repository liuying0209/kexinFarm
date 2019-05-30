package com.farm.core.record.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farm.base.BaseException;
import com.farm.base.common.Constant;
import com.farm.base.common.enums.CropStatusEnum;
import com.farm.base.common.enums.FarmingRecordStatusEnum;
import com.farm.base.exception.FarmException;
import com.farm.base.exception.RecordException;
import com.farm.base.farm.Farm;
import com.farm.base.farm.FarmWork;
import com.farm.base.farm.PlotCrop;
import com.farm.base.record.CropTask;
import com.farm.base.record.FarmingParam;
import com.farm.base.record.FarmingRecord;
import com.farm.base.record.ParamContent;
import com.farm.base.user.User;
import com.farm.core.farm.mapper.FarmMapper;
import com.farm.core.farm.mapper.FarmWorkMapper;
import com.farm.core.farm.mapper.PlotCropMapper;
import com.farm.core.record.AppendedVO;
import com.farm.core.record.FarmingRecordVO;
import com.farm.core.record.dto.AllParentRecordDTO;
import com.farm.core.record.dto.AppendedDTO;
import com.farm.core.record.dto.RecordParamDTO;
import com.farm.core.record.mapper.CropTaskMapper;
import com.farm.core.record.mapper.FarmingParamMapper;
import com.farm.core.record.mapper.FarmingRecordMapper;
import com.farm.core.record.mapper.RecordAssessMapper;
import com.farm.core.record.service.FarmingParamService;
import com.farm.core.record.service.RecordService;
import com.farm.core.user.mapper.UserMapper;
import com.farm.core.util.RedisUtil;
import com.farm.core.util.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 ** @Version 1.0.0
 */
@Service
public class RecordServiceImpl implements RecordService {
    private final static Logger LOGGER = LoggerFactory.getLogger(RecordServiceImpl.class);
    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    private PlotCropMapper plotCropMapper;
    private CropTaskMapper cropTaskMapper;
    private FarmingRecordMapper farmingRecordMapper;
    private FarmingParamService farmingParamService;
    private FarmWorkMapper farmWorkMapper;
    private FarmingParamMapper farmingParamMapper;
    private FarmMapper farmMapper;
    private RecordAssessMapper recordAssessMapper;
    private RedisUtil redisUtil;
    private UserMapper userMapper;

    @Autowired
    @SuppressWarnings("all")
    public RecordServiceImpl(PlotCropMapper plotCropMapper, CropTaskMapper cropTaskMapper,
                             FarmingRecordMapper farmingRecordMapper, FarmingParamService farmingParamService,
                             FarmWorkMapper farmWorkMapper, FarmingParamMapper farmingParamMapper,
                             FarmMapper farmMapper, RecordAssessMapper recordAssessMapper,
                             RedisUtil redisUtil, UserMapper userMapper) {
        this.plotCropMapper = plotCropMapper;
        this.cropTaskMapper = cropTaskMapper;
        this.farmingRecordMapper = farmingRecordMapper;
        this.farmingParamService = farmingParamService;
        this.farmWorkMapper = farmWorkMapper;
        this.farmingParamMapper = farmingParamMapper;
        this.farmMapper = farmMapper;
        this.recordAssessMapper = recordAssessMapper;
        this.redisUtil = redisUtil;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public void startPlant(Long plotCropId, Long farmId) throws BaseException {
        if (plotCropId == null || farmId == null) {
            LOGGER.error("必要参数为空  plotCropId:{},farmId:{}", plotCropId, farmId);
            throw new BaseException(BaseException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }

        PlotCrop plotCrop = this.plotCropMapper.findById(plotCropId);
        if (plotCrop == null) {
            LOGGER.error("查询不到品种作物信息 plotCropId:{}", plotCropId);
            throw new RecordException(RecordException.PLOT_CROP_INFO_ERROR);
        }
        CropStatusEnum status = plotCrop.getStatus();
        if (1 != status.getCode()) {
            LOGGER.info("作物已经添加农事环节 plotCropId :{}", plotCropId);
            return;
        }
        Long cropId = plotCrop.getCropId();
        //查询任务表中的农事环节 copy到记录表中
        List<CropTask> parentCropTaskList = cropTaskMapper.findParentByCropId(cropId);

        if (CollectionUtils.isEmpty(parentCropTaskList)) {
            LOGGER.error("作物任务中查询不到农事环节信息  cropId:{}", cropId);
            throw new FarmException(FarmException.CROP_TASK_INFO_ERROR);
        }
        User user = UserUtils.getCurrent();

        //添加redis中保留的作物id
        this.redisUtil.set(farmId.toString() + user.getId(), plotCropId);
        //设置默认农场
        user.setDefaultFarmId(farmId);
        this.userMapper.update(user);
        LOGGER.info("更新用户默认农场 farmId:{}", farmId);

        if (1 == status.getCode()) {
            //修改作物状态为0
            plotCrop.setStatus(CropStatusEnum.DOING);
            this.plotCropMapper.update(plotCrop);
        }

        for (CropTask cropTask : parentCropTaskList) {
            Long farmingId = cropTask.getFarmingId();
            Integer category = cropTask.getCategory();
            Long parentRecordId = handleFarmingRecord(cropTask, plotCropId, null);
            if (category != 0) {
                //根据父节点农事环节查询子节点
                List<CropTask> suns = this.cropTaskMapper.findSunByParentId(farmingId, cropId);

                if (CollectionUtils.isNotEmpty(suns)) {
                    for (CropTask sun : suns) {
                        Long farmingIdSun = sun.getFarmingId();
                        Long recordSunId = handleFarmingRecord(sun, plotCropId, parentRecordId);
                        this.farmingParamService.copyTemplateToParam(recordSunId, cropId, farmingIdSun);
                    }
                }
            } else {
                this.farmingParamService.copyTemplateToParam(parentRecordId, cropId, farmingId);
            }


        }

    }

    /**
     * 封装农事记录对象
     *
     * @return
     */

    private Long handleFarmingRecord(CropTask cropTask, Long plotCropId, Long parentRecordId) {
        Integer category = cropTask.getCategory();
        Long farmingId = cropTask.getFarmingId();
        String farmingName = cropTask.getFarmingName();
        Integer number = cropTask.getNumber();
        Integer type = cropTask.getType();
        Long parentId = cropTask.getParentId();
        Integer score = cropTask.getScore();


        FarmingRecord farmingRecord = new FarmingRecord();
        farmingRecord.setCategory(category);
        farmingRecord.setPlotCropId(plotCropId);
        farmingRecord.setFarmWorkId(farmingId);
        farmingRecord.setFarmWorkName(farmingName);
        farmingRecord.setIndexNumber(number);
        farmingRecord.setType(type);
        farmingRecord.setParentId(parentId);
        farmingRecord.setScore(score);
        if (parentId != 0) {
            farmingRecord.setParentRecordId(parentRecordId);
        }

        farmingRecordMapper.insert(farmingRecord);
        Long recordId = farmingRecord.getId();
        return recordId;
    }


    @Override
    public List<FarmWork> listAddFarmWork() {
        List<FarmWork> farmWorks = this.farmWorkMapper.listAddFarmWork();
        return farmWorks;
    }

    @Override
    public List<AllParentRecordDTO> ListAllParentAndAddRecord(Long plotCropId) {

        List<AllParentRecordDTO> resultList = new ArrayList<>();

        //查询父类农事环节
        List<FarmingRecord> farmingRecords = this.farmingRecordMapper.findByPlotCropId(plotCropId);

        if (CollectionUtils.isNotEmpty(farmingRecords)) {

            farmingRecords.stream().forEach(farmingRecord -> {

                Integer addFlag = farmingRecord.getAddFlag();
                Long recordId = farmingRecord.getId();
                Long farmingId = farmingRecord.getFarmWorkId();
                String farmWorkName = farmingRecord.getFarmWorkName();
                Integer category = farmingRecord.getCategory();
                FarmingRecordStatusEnum status = farmingRecord.getStatus();

                AllParentRecordDTO allParentRecordDTO = new AllParentRecordDTO();
                allParentRecordDTO.setAddFlag(addFlag);
                allParentRecordDTO.setCategory(category);
                allParentRecordDTO.setFarmingId(farmingId);
                allParentRecordDTO.setRecordId(recordId);
                allParentRecordDTO.setFarmingName(farmWorkName);
                allParentRecordDTO.setStatus(status.getCode());

                List<AppendedDTO> list = new ArrayList<>();

                //有追加
                if (addFlag == 1) {

                    List<FarmingRecord> appendeds = this.farmingRecordMapper.findAppendedById(recordId);

                    appendeds.stream().forEach(append -> {
                        Long appendRecordId = append.getId();
                        Long farmWorkId = append.getFarmWorkId();
                        String farmingName = append.getFarmWorkName();
                        Integer appendCategory = append.getCategory();
                        FarmingRecordStatusEnum appendStatus = append.getStatus();

                        AppendedDTO appendedDTO = new AppendedDTO();

                        appendedDTO.setRecordId(appendRecordId);
                        appendedDTO.setCategory(appendCategory);
                        appendedDTO.setFarmingName(farmingName);
                        appendedDTO.setStatus(appendStatus.getCode());
                        if (farmWorkId == 11) {
                            appendedDTO.setAppendedType(1);
                        }
                        list.add(appendedDTO);
                    });
                    allParentRecordDTO.setList(list);

                }
                resultList.add(allParentRecordDTO);
            });

        }

        return resultList;
    }

    @Override
    @Transactional
    public Long addAppendFarming(AppendedVO params) throws RecordException, FarmException {
        Long farmingId = params.getFarmingId();
        Long recordId = params.getRecordId();
        if (recordId == null || farmingId == null) {
            LOGGER.error("必要的参数为空 recordId :{} ,farmingId :{}", recordId, farmingId);
            throw new RecordException(RecordException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }
        FarmingRecord record = this.farmingRecordMapper.findById(recordId);
        if (record == null) {
            LOGGER.error("获取农事记录有误 recordId:{}", recordId);
            throw new FarmException(FarmException.FARMING_RECORD_INFO_ERROR);
        }
        Long plotCropId = record.getPlotCropId();
        PlotCrop plotCrop = this.plotCropMapper.findById(plotCropId);
        if (plotCrop == null) {
            LOGGER.error("获取品种作物信息有误 plotCropId:{}", plotCropId);
            throw new FarmException(FarmException.PLOT_CROP_INFO_ERROR);
        }
        Long cropId = plotCrop.getCropId();
        FarmWork farmWork = this.farmWorkMapper.findById(farmingId);
        if (farmWork == null) {
            LOGGER.error("获取农事环节信息有误 farmingId:{}", farmingId);
            throw new FarmException(FarmException.FARM_WORK_INFO_ERROR);
        }

        //修改原记录 是否追加标识
        FarmingRecord original = new FarmingRecord();
        original.setId(recordId);
        original.setAddFlag(1);
        this.farmingRecordMapper.update(original);

        Long parentId = farmWork.getParentId();
        String farmWorkName = farmWork.getName();
        Integer type = farmWork.getType();
        Integer category = farmWork.getCategory();

        Long appendRecordId = null;

        //添加农事环节记录
        FarmingRecord farmingRecord = new FarmingRecord();
        farmingRecord.setPlotCropId(plotCropId);
        farmingRecord.setFarmWorkId(farmingId);
        farmingRecord.setParentId(parentId);
        farmingRecord.setFarmWorkName(farmWorkName);
        farmingRecord.setType(type);
        farmingRecord.setCategory(category);
        farmingRecord.setAppendedId(recordId);
        this.farmingRecordMapper.insert(farmingRecord);
        appendRecordId = farmingRecord.getId();
        //获取参数集合拷贝到参数
        this.farmingParamService.copyTemplateToParam(appendRecordId, cropId, farmingId);

        if (category == 1) {
            //表示追肥  需要把追肥子类也添加
            List<FarmWork> sunFarmWork = this.farmWorkMapper.findByParentId(farmingId);
            for (FarmWork sun : sunFarmWork) {
                //添加农事环节记录
                FarmingRecord sunFarmingRecord = new FarmingRecord();
                sunFarmingRecord.setPlotCropId(plotCropId);
                sunFarmingRecord.setFarmWorkId(sun.getId());
                sunFarmingRecord.setParentId(sun.getParentId());
                sunFarmingRecord.setFarmWorkName(sun.getName());
                sunFarmingRecord.setType(sun.getType());
                sunFarmingRecord.setCategory(sun.getCategory());
                sunFarmingRecord.setAppendedId(recordId);
                sunFarmingRecord.setParentRecordId(appendRecordId);
                this.farmingRecordMapper.insert(sunFarmingRecord);
                Long farmingRecordId = sunFarmingRecord.getId();
                //获取参数集合拷贝到参数
                this.farmingParamService.copyTemplateToParam(farmingRecordId, cropId, sun.getId());

            }

        }
        return appendRecordId;
    }


    @Override
    public List<RecordParamDTO> getParamsByCategory(Integer category, Long recordId) throws RecordException {

        if (category == null || recordId == null) {
            LOGGER.error("必要的参数为空 recordId :{} ,category :{}", recordId, category);
            throw new RecordException(RecordException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }

        List<RecordParamDTO> resultList = new ArrayList<>();

        switch (category) {
            case 0:
                FarmingRecord record = this.farmingRecordMapper.findById(recordId);
                RecordParamDTO recordParam = getRecordParam(record);
                resultList.add(recordParam);
                break;
            //前置任务
            case 1:
                //细分子任务

                // 区分  ，未开始 展示所有
                FarmingRecord recordOne = this.farmingRecordMapper.findById(recordId);
                FarmingRecordStatusEnum status = recordOne.getStatus();
                List<FarmingRecord> records = null;
                if (status.getCode() == 0) {
                    records = this.farmingRecordMapper.findByParentRecordId(recordId, false);
                } else {
                    //  返回之前完成的子项
                    records = this.farmingRecordMapper.findByParentRecordId(recordId, true);
                }
                if (CollectionUtils.isNotEmpty(records)) {
                    records.stream().forEach(recordTwo -> {
                        RecordParamDTO recordParamTwo = getRecordParam(recordTwo);
                        resultList.add(recordParamTwo);
                    });
                }
                break;

            case 2:

                List<FarmingRecord> recordList = this.farmingRecordMapper.findByParentRecordId(recordId, false);
                if (CollectionUtils.isNotEmpty(recordList)) {
                    recordList.stream().forEach(recordTwo -> {
                        RecordParamDTO recordParamTwo = getRecordParam(recordTwo);
                        resultList.add(recordParamTwo);
                    });
                }
                break;
            default:
                break;
        }

        return resultList;
    }


    /**
     * 处理 参数
     */

    private RecordParamDTO getRecordParam(FarmingRecord record) {
        Long recordId = record.getId();
        FarmingParam param = this.farmingParamMapper.findByRecordId(recordId);

        String farmWorkName = record.getFarmWorkName();
        FarmingRecordStatusEnum status = record.getStatus();
        RecordParamDTO paramDTO = new RecordParamDTO();
        paramDTO.setName(farmWorkName);
        if (param != null) {
            String content = param.getContent();
            if (StringUtils.isNotBlank(content)) {
                JSONArray objects = JSONArray.parseArray(content);
                paramDTO.setContent(objects);
            }
        }
        paramDTO.setTime(record.getTime());
        paramDTO.setRecordId(recordId);
        paramDTO.setStatus(status.getCode());
        return paramDTO;

    }


    @Override
    @Transactional
    public void saveFarmingParams(FarmingRecordVO farmingRecordVO) throws FarmException, ParseException {
        String content = farmingRecordVO.getContent();
        Long recordId = farmingRecordVO.getRecordId();
        String time = farmingRecordVO.getDate();

        if (recordId == null || StringUtils.isBlank(time)) {
            LOGGER.error("必要请求参数为空 recordId:{}, time:{}", recordId, content, time);
            throw new FarmException(FarmException.ERROR_CODE_ILLEGAL_ARGUMENTS);
        }
        LOGGER.info("content :{}", content);

        //对于分数的处理

        List list = JSONObject.parseObject(content, List.class);

//TODO 计算 参数中每一项的得分

//        Map<String, Object> map = this.countScore(list);
//        Integer score = (Integer) map.get("score");
//        String remark = (String) map.get("remark");
        Boolean flag = this.finished(list);

        FarmingRecord record = new FarmingRecord();
        record.setId(recordId);
//        record.setScore(score);
//        record.setRemark(remark);
        Date date = DATE_FORMAT.parse(time);
        record.setTime(date);
        if (flag) {
            record.setStatus(FarmingRecordStatusEnum.FINISHED);
        } else {
            record.setStatus(FarmingRecordStatusEnum.UNFINISHED);
        }
        //修改记录分值
        this.farmingRecordMapper.update(record);

        FarmingRecord farmingRecord = this.farmingRecordMapper.findById(recordId);

        Long farmWorkId = farmingRecord.getFarmWorkId();

        // 处理有父类的记录状态
        Long parentRecordId = farmingRecord.getParentRecordId();
        if (parentRecordId != null) {

            FarmingRecord parentRecord = this.farmingRecordMapper.findById(parentRecordId);
            Integer category = parentRecord.getCategory();
            if (category == 1) {
                //直接修改父类状态
                parentRecord.setStatus(record.getStatus());
            } else {
                //类型为 2
                //根据父类记录编号 查询子类是否有未完成的
                int resultCount = this.farmingRecordMapper.countSunUnfinished(parentRecordId);
                if (resultCount == 0) {
                    //表示都完成
                    parentRecord.setStatus(FarmingRecordStatusEnum.FINISHED);
                } else {
                    parentRecord.setStatus(FarmingRecordStatusEnum.UNFINISHED);
                }
            }

            parentRecord.setTime(date);
            this.farmingRecordMapper.update(parentRecord);
        }


        Long plotCropId = farmingRecord.getPlotCropId();

        updateScore(plotCropId, farmWorkId, recordId, content);

    }


    /**
     * 更新分值
     */
    public void updateScore(Long plotCropId, Long farmWorkId, Long recordId, String content) {

        LOGGER.info("请求参数是 plotCropId:{} ,farmWorkId:{},recordId:{}", plotCropId, farmWorkId, recordId);
        //查询作物农事环节得分
        int plotCropId1Count = this.farmingRecordMapper.countScoreByPlotCropId(plotCropId);

        //修改作物分值
        PlotCrop plotCrop = new PlotCrop();
        plotCrop.setId(plotCropId);

//        if(plotCropId1Count==null){
//            LOGGER.info("计算分数结果为null plotCropId:{} ",plotCropId);
//            plotCrop.setScore(0);
//
//        }else {
//            plotCrop.setScore(plotCropId1Count);
//        }
        plotCrop.setScore(plotCropId1Count);
        //针对采收农事  需要修改作物状态为完成
        plotCrop.setStatus(CropStatusEnum.DOING);
        if (farmWorkId == Constant.RECOVERY) {
            plotCrop.setStatus(CropStatusEnum.FINISHED);
        }
        this.plotCropMapper.update(plotCrop);
        //查询农场所有品种作物得分
        //获取农场id
        long farmId = this.plotCropMapper.getFarmId(plotCropId);


        int countPlotCrop = this.plotCropMapper.countPlotCropByFarmId(farmId);
        if (countPlotCrop > 0) {
            //查询总的分
            int farmScore = this.plotCropMapper.countFarmScore(farmId);

            Integer resultScore = farmScore / countPlotCrop;
            //修改农场分值
            Farm farm = new Farm();
            farm.setId(farmId);
            farm.setScore(resultScore);
            this.farmMapper.update(farm);

        }


        FarmingParam farmingParam = new FarmingParam();
        farmingParam.setRecordId(recordId);
        farmingParam.setContent(content);
        this.farmingParamMapper.updateByRecordId(farmingParam);

    }


    /**
     * 递归处理分数和扣分说明
     */

    private Map<String, Object> countScore(List list) {

        Map<String, Object> result = new HashMap<>();
        Integer count = 0;
        String remark = "";

        if (CollectionUtils.isEmpty(list)) {
            result.put("score", count);
            result.put("remark", remark);
            return result;
        }

        for (Object obj : list) {

            JSONObject jsonObject = (JSONObject) obj;
            ParamContent paramContent = jsonObject.toJavaObject(ParamContent.class);
            if ("0".equals(paramContent.getFlag()) && !("2".equals(paramContent.getType()))) {
                if (paramContent.getScore() != 0) {
                    String content = paramContent.getName() + " 未完成 扣 " + paramContent.getScore() + "分 ,";
                    remark += content;
                }
                continue;
            }

            if ("2".equals(paramContent.getType())) {
                //递归
                JSONArray value = (JSONArray) paramContent.getValue();
                List sunList = value.toJavaObject(List.class);
                Map<String, Object> map = countScore(sunList);
                Integer score = (Integer) map.get("score");
                String remarkStr = (String) map.get("remark");
                Boolean flagSun = (Boolean) map.get("flag");

                count += score;
                remark += remarkStr;

            } else {
                count += paramContent.getScore();
            }

        }

        result.put("score", count);
        result.put("remark", remark);
        return result;
    }


    /**
     * 判断农事环节是否全部完成
     */

    public Boolean finished(List list) {
        Boolean flag = true;

        for (Object obj : list) {
            JSONObject jsonObject = (JSONObject) obj;
            ParamContent paramContent = jsonObject.toJavaObject(ParamContent.class);

            if ("2".equals(paramContent.getType())) {

                if ("0".equals(paramContent.getFlag())) {
                    continue;
                }

                if ("2".equals(paramContent.getFlag())) {
                    flag = false;
                    break;
                }
            } else {

                if ("0".equals(paramContent.getFlag())) {
                    flag = false;
                    break;
                }
            }


        }
        return flag;
    }


}
