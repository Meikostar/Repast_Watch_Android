package com.canplay.repast_wear.mvp.model;

import java.io.Serializable;

/**
 * Created by qi_fu on 2017/7/27.
 */

public class Message implements Serializable{
    private String tableFrom;
    private String icon;
    private String content;
    private Long time;

    public String getTableFrom() {
        return tableFrom;
    }

    public void setTableFrom(String tableFrom) {
        this.tableFrom = tableFrom;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
