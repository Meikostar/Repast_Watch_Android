package com.canplay.repast_wear.mvp.model;

import java.util.List;


public class provinceCityList {

    /**
     * provinceCode : 340000
     * provinceName : 安徽省
     * cityList : [{"cityCode":340100,"cityName":"合肥市","areaList":[{"areaCode":340102,"areaName":"瑶海区"},{"areaCode":340104,"areaName":"蜀山区"},{"areaCode":340123,"areaName":"肥西县"}]}]
     */

    private int provinceCode;
    private String provinceName;
    private List<CityListBean> cityList;

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<CityListBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityListBean> cityList) {
        this.cityList = cityList;
    }

    public static class CityListBean {
        /**
         * cityCode : 340100
         * cityName : 合肥市
         * areaList : [{"areaCode":340102,"areaName":"瑶海区"},{"areaCode":340104,"areaName":"蜀山区"},{"areaCode":340123,"areaName":"肥西县"}]
         */

        private int cityCode;
        private String cityName;
        private List<AreaListBean> areaList;

        public int getCityCode() {
            return cityCode;
        }

        public void setCityCode(int cityCode) {
            this.cityCode = cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public List<AreaListBean> getAreaList() {
            return areaList;
        }

        public void setAreaList(List<AreaListBean> areaList) {
            this.areaList = areaList;
        }

        public static class AreaListBean {
            /**
             * areaCode : 340102
             * areaName : 瑶海区
             */

            private int areaCode;
            private String areaName;

            public int getAreaCode() {
                return areaCode;
            }

            public void setAreaCode(int areaCode) {
                this.areaCode = areaCode;
            }

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }
        }
    }

    @Override
    public String toString() {
        return "provinceCityList{" +
                "provinceCode=" + provinceCode +
                ", provinceName='" + provinceName + '\'' +
                ", cityList=" + cityList +
                '}';
    }
}
