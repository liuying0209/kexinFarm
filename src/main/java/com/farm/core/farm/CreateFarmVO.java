package com.farm.core.farm;

import java.io.Serializable;

/**
 * 创建农场VO
 ** @Date: 2019-04-25 21:06
 */
public class CreateFarmVO implements Serializable {
    private static final long serialVersionUID = 8384963341537287786L;

    /**
     * 农场id
     */
    private Long farmId;
    /**
     * 农场名称
     */
    private String farmName;

    /**
     * 农场面积
     */
    private Integer area;

    /**
     * 暖棚数量
     */
    private Integer  brooderCount;

    /**
     * 冷棚数量
     */
    private Integer coolCount;

    /**
     * 答题分数
     */
    private Integer answerScore;


    public CreateFarmVO() {
    }

    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getBrooderCount() {
        return brooderCount;
    }

    public void setBrooderCount(Integer brooderCount) {
        this.brooderCount = brooderCount;
    }

    public Integer getCoolCount() {
        return coolCount;
    }

    public void setCoolCount(Integer coolCount) {
        this.coolCount = coolCount;
    }

    public Integer getAnswerScore() {
        return answerScore;
    }

    public void setAnswerScore(Integer answerScore) {
        this.answerScore = answerScore;
    }


    @Override
    public String toString() {
        return "CreateFarmVO{" +
                "farmId=" + farmId +
                ", farmName='" + farmName + '\'' +
                ", area=" + area +
                ", brooderCount=" + brooderCount +
                ", coolCount=" + coolCount +
                ", answerScore=" + answerScore +
                '}';
    }
}
