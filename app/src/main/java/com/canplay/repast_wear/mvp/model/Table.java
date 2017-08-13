package com.canplay.repast_wear.mvp.model;


public class Table {
    /**
     *  {
     "layoutId": 1,
     "tableNo": "1",
     "bound": 0}
     */
    private long layoutId;
    private String tableNo;
    private int bound;


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

    public long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(long layoutId) {
        this.layoutId = layoutId;
    }

}
