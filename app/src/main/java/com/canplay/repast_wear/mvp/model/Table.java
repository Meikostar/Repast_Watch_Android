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

    private String name;

    private int bound;

    private String watchCode;

    public String getWatchCode() {
        return watchCode;
    }

    public void setWatchCode(String watchCode) {
        this.watchCode = watchCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
                ", watchCode='" + watchCode + '\'' +
                ", name='" + name + '\'' +
                ", bound=" + bound +
                '}';
    }
}
