package com.farm.core.user;

import com.farm.base.farm.Farm;

import java.io.Serializable;
import java.util.List;

/**
 * 钉钉个人首页数据分装类
 ** @Version 1.0.0
 */
public class UserInfoDTO implements Serializable {


    private static final long serialVersionUID = -167835785501001640L;
    /**
     * 主键
     */
    private String id;
    /**
     * 手机好吗
     */
    private String mobileNumber;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 头像照片
     */
    private String photo;


    /**
     * 农场信息集合
     */
    List<Farm> list;

    public UserInfoDTO() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Farm> getList() {
        return list;
    }

    public void setList(List<Farm> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "id='" + id + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", realName='" + realName + '\'' +
                ", photo='" + photo + '\'' +
                ", list=" + list +
                '}';
    }
}
