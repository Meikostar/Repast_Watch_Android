package com.canplay.repast_wear.mvp.model;



public class Version {

    /**
     * version : 2000
     * sleepTime : 0
     * type : 0
     * userId : 0
     * thirdSupport : 0
     * sensitiveVersion : 1.0
     * sensitiveUrl : http://oud4e96lb.bkt.clouddn.com/words.dict
     * apkVersion : 1.1
     */

    private int version;
    private int sleepTime;
    private int type;
    private int userId;
    private int thirdSupport;
    private String sensitiveVersion;
    private String sensitiveUrl;
    private String apkVersion;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getThirdSupport() {
        return thirdSupport;
    }

    public void setThirdSupport(int thirdSupport) {
        this.thirdSupport = thirdSupport;
    }

    public String getSensitiveVersion() {
        return sensitiveVersion;
    }

    public void setSensitiveVersion(String sensitiveVersion) {
        this.sensitiveVersion = sensitiveVersion;
    }

    public String getSensitiveUrl() {
        return sensitiveUrl;
    }

    public void setSensitiveUrl(String sensitiveUrl) {
        this.sensitiveUrl = sensitiveUrl;
    }

    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }

    @Override
    public String toString() {
        return "Version{" +
                "version=" + version +
                ", sleepTime=" + sleepTime +
                ", type=" + type +
                ", userId=" + userId +
                ", thirdSupport=" + thirdSupport +
                ", sensitiveVersion='" + sensitiveVersion + '\'' +
                ", sensitiveUrl='" + sensitiveUrl + '\'' +
                ", apkVersion='" + apkVersion + '\'' +
                '}';
    }
}
