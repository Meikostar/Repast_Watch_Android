package com.canplay.repast_wear.mvp.model;



public class DEVICE {
    /*
    * tableId	long	桌子id
businessId	long	商家id
tableNo	string	绑定桌号
bound	int	绑定设备情况 0:未绑定台桌，1：已绑定台桌（异常情况引导绑定桌子）
easeToken	string	环信密码*/
    private long tableId;
    private long businessId;
    private String tableNo;
    private int bound;
    private String easeToken;

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(long businessId) {
        this.businessId = businessId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public int getBound() {
        return bound;
    }

    public void setBound(int bound) {
        this.bound = bound;
    }

    public String getEaseToken() {
        return easeToken;
    }

    public void setEaseToken(String easeToken) {
        this.easeToken = easeToken;
    }

    @Override
    public String toString() {
        return "DEVICE{" +
                "tableId=" + tableId +
                ", businessId=" + businessId +
                ", tableNo='" + tableNo + '\'' +
                ", bound=" + bound +
                ", easeToken='" + easeToken + '\'' +
                '}';
    }
}
