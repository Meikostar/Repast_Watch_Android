package com.canplay.repast_wear.mvp.model;

import java.io.Serializable;
import java.util.List;



public class Resps implements Serializable{
    private List<Message> pushListResps;
    private int hasNext;

    public Resps() {
    }

    public Resps(List<Message> pushListResps, int hasNext) {
        this.pushListResps = pushListResps;
        this.hasNext = hasNext;
    }

    public List<Message> getPushListResps() {
        return pushListResps;
    }

    public void setPushListResps(List<Message> pushListResps) {
        this.pushListResps = pushListResps;
    }

    public int getHasNext() {
        return hasNext;
    }

    public void setHasNext(int hasNext) {
        this.hasNext = hasNext;
    }

    @Override
    public String toString() {
        return "Resps{" +
                "pushListResps=" + pushListResps +
                ", hasNext=" + hasNext +
                '}';
    }
}
