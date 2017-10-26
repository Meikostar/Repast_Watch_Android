package com.canplay.repast_wear.mvp.model;

/**
 * Created by qi_fu on 2017/10/26.
 */

public class ApkUrl {


    /**
     * uploadUrl : http://oud4e96lb.bkt.clouddn.com/AA
     * version : 1.1
     */

    private String uploadUrl;
    private String version;

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ApkUrl{" +
                "uploadUrl='" + uploadUrl + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
