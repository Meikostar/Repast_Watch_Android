package com.canplay.repast_wear.mvp.model;


public class Table {
    /**
     *  {
     "tableId": 1,
     "tableNo": "1",
     "bound": 0}
     */
    private long tableId;
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

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

}
