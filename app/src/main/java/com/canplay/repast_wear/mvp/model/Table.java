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
    private String state;
    private int bound;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableId=" + tableId +
                ", tableNo='" + tableNo + '\'' +
                ", state='" + state + '\'' +
                ", bound=" + bound +
                '}';
    }
}
