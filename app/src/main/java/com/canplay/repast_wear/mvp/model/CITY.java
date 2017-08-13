package com.canplay.repast_wear.mvp.model;

import java.util.List;

/**
 * Created by mykar on 17/4/26.
 */
public class CITY {
//    "id": "535",
//            "name": "爱尔巴桑",
//            "parent_id": "534",
//            "type": "city"
    public int cityCode;
    public String cityName;
    public List<AREA> areaList;
//    "cityCode": 340100,
    //            "cityName": "合肥市",
//            "areaList":

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<AREA> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<AREA> areaList) {
        this.areaList = areaList;
    }
}
