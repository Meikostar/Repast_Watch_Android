package com.canplay.repast_wear.mvp.model;

import java.io.Serializable;



public class Message implements Serializable{
    /* "pushId": "2",long
           "menuName": "加水",
           "tableNo": "5"
   * */
    private Long pushId;
    private String tableNo;
    private String menuName;
    private String tableFrom;
    private int type;

    public Long getPushId() {
        return pushId;
    }

    public void setPushId(Long pushId) {
        this.pushId = pushId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTableFrom() {
        return tableFrom;
    }

    public void setTableFrom(String tableFrom) {
        this.tableFrom = tableFrom;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

}
