package com.canplay.repast_wear.util;


import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 获取时间戳
     *
     * @return 获取时间戳
     */
    public static Long getTimeLong() {
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        return time.getTime();

    }

    public static Date getSystemDate() {
        Date date = new Date();

        return date;
    }

    public static String getSystemTime() {
        return getDateLongToString(getTimeLong(), 4);
    }

    /**
     * 将时间转化为年月日
     *
     * @param time
     * @param type 默认年月日时分
     *             1：年月日
     *             2：月日
     *             3：月日时分
     *             4：时分
     * @return
     */
    public static String getDateLongToString(long time, int type) {
        Date d = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;//tmd.因为老外计算只有0-11月
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        String minutesStr = minutes < 10 ? "0" + minutes : minutes + "";
        String hoursStr = hours == 0 ? hours + "0" : hours + "";
        if (type == 1) {
            return year + "年" + month + "月" + day + "日";
        }
        if (type == 2) {
            return month + "月" + day + "日";
        }
        if (type == 3) {
            return month + "月" + day + "日" + " " + hoursStr + ":" + minutesStr;
        }
        if (type == 4) {
            return hoursStr + ":" + minutesStr;
        }
        return year + "年" + month + "月" + day + "日" + " " + hoursStr + ":" + minutesStr;
    }
}
