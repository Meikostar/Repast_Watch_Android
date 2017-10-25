package com.canplay.repast_wear.mvp.model;

import java.util.List;


public class RespsTable {
    private int hasNext;
    private List<Table> list;

    public int getHasNext() {
        return hasNext;
    }

    public void setHasNext(int hasNext) {
        this.hasNext = hasNext;
    }

    public List<Table> getList() {
        return list;
    }

    public void setList(List<Table> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "RespsTable{" +
                "hasNext=" + hasNext +
                ", list=" + list +
                '}';
    }
}
