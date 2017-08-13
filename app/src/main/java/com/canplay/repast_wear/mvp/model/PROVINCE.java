package com.canplay.repast_wear.mvp.model;

import java.util.List;

/**
 * Created by Meiko on 2017/8/8.
 */
public class PROVINCE {
//    "provinceCode": 340000,
//            "provinceName": "安徽省",
//            "cityList": [
//    {
//        "cityCode": 340100,
//            "cityName": "合肥市",
//            "areaList": [
//        {
//            "areaCode": 340102,
//                "areaName": "瑶海区"
    public List<PROVINCE> provinceCityList;
    public List<CITY> cityList;
    public int provinceCode;
    public String provinceName;

    public List<CITY> getCityList() {
        return cityList;
    }

    public void setCityList(List<CITY> cityList) {
        this.cityList = cityList;
    }

    public List<PROVINCE> getProvinceCityList() {
        return provinceCityList;
    }

    public void setProvinceCityList(List<PROVINCE> provinceCityList) {
        this.provinceCityList = provinceCityList;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String businessName;
    public int businessId;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }
}
