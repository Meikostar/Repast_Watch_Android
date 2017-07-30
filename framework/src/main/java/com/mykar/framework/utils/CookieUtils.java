package com.mykar.framework.utils;

import android.content.SharedPreferences;

import com.mykar.framework.ApplicationHolder;
import com.mykar.framework.network.androidquery.callback.AjaxStatus;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by linquandong on 15/7/7.
 */
public class CookieUtils {

    private static SharedPreferences shared;
    private static SharedPreferences.Editor editor;
    private static final String COOKIEFLAGS = "cookie";
    private static final String COOKIE_STARTTIME = "cookie_startime";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final long COOKIE_LIFE = 24 * 7 * 5;//有效时间5个星期（单位为小时）
    private static HashMap<String, String> CookieContiner;

    static {
        shared = ApplicationHolder.getmApplication().getSharedPreferences(COOKIEFLAGS, 0);
        editor = shared.edit();
        //初始化cookie
        CookieContiner = new HashMap<>();
        decodeCookie(makeCookie());
    }

    public static void saveCookies(AjaxStatus status) {
        String cookier = status.getCookieString(SET_COOKIE);

        decodeCookie(cookier);
        String cookieString = encodeCookie();
        if (cookieString != null) {
            editor.putString(SET_COOKIE, cookieString);
            editor.commit();
        }
    }

    public static void decodeCookie(String cookieStr) {
        if (cookieStr == null) {
            return;
        }
        String[] cookievalues = cookieStr.split(";");
        for (int i = 0; i < cookievalues.length; i++) {
            String[] keyPair = cookievalues[i].split("=");
            String key = keyPair[0].trim();
            String value = keyPair.length > 1 ? keyPair[1].trim() : "";
            CookieContiner.put(key, value);
        }
    }

    public static String encodeCookie() {
        StringBuilder cookieString = new StringBuilder();
        Iterator iter = CookieContiner.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = entry.getKey().toString();
            String val = entry.getValue().toString();
            cookieString.append(key);
            cookieString.append("=");
            cookieString.append(val);
            cookieString.append(";");
        }
        return cookieString.toString();
    }

    private static long getPresentime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime().getTime();
    }

    public static String makeCookie() {
        if (shared == null || shared.getAll().size() == 0) {
            return null;
        }
        return shared.getString(SET_COOKIE, null);

    }

    /***
     * cookie是否过了有效时间
     * 如果过期则清空cookie
     */
    public static boolean isOutTime() {
        long cookie_startime = shared.getLong(COOKIE_STARTTIME, 0);
        long duration = getPresentime() - cookie_startime;
        long hours = duration / (1000 * 60 * 60);
        if (hours >= COOKIE_LIFE) {
            editor.putString(SET_COOKIE, null);
            editor.commit();
            return true;
        }
        return false;
    }

    //清空cookie
    public static void clearCookie() {
        editor.putString(SET_COOKIE, null);
        editor.commit();
    }
}