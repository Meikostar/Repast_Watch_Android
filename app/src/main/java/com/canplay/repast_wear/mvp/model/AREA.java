package com.canplay.repast_wear.mvp.model;

/**
 * Created by mykar on 17/4/26.
 */
public class AREA {

    public int areaCode;
    public String areaName;

//    "areaCode": 340102,
//    //                "areaName": "瑶海区"

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    /*
    * "businessName": "三及第",
    * "businessId": 2
    * */
    private int businessId;
    private String businessName;

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
