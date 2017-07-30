package com.mykar.framework.commonlogic.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mykar.framework.ApplicationHolder;
import com.mykar.framework.KLog.KLog;
import com.mykar.framework.commonlogic.protocol.STATUS;
import com.mykar.framework.network.androidquery.callback.AjaxCallback;
import com.mykar.framework.network.androidquery.callback.AjaxStatus;
import com.mykar.framework.utils.CookieUtils;
import com.mykar.framework.utils.LoginUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linquandong on 15/6/1.
 */
public class BaseSingleModel {
    protected BeeQuery aq;
    protected STATUS responStatus;
    protected JSONObject dataJson;
    protected Gson mGson;
    private int index;
    private boolean hasNext;

    protected BaseSingleModel() {
        aq = new BeeQuery(ApplicationHolder.getmApplication());
        initGson();
    }
    private void initGson() {
        GsonBuilder gsonBulder = new GsonBuilder();
        //对数字类型做兼容
        gsonBulder.registerTypeAdapter(int.class, INTEGER);
        //通过反射获取instanceCreators属性
        try {
            Class builder = (Class) gsonBulder.getClass();
            Field f = builder.getDeclaredField("instanceCreators");
            f.setAccessible(true);
            Map<Type, InstanceCreator<?>> val = (Map<Type, InstanceCreator<?>>) f.get(gsonBulder);//得到此属性的值
            //注册数组的处理器
            gsonBulder.registerTypeAdapterFactory(new CollectionTypeAdapterFactory(new ConstructorConstructor(val)));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mGson = gsonBulder.create();

    }
    /**
     * 自定义adapter，解决由于数据类型为Int,实际传过来的值为Float，或者是""导致解析出错的问题
     * 目前的解决方案为将所有Int类型当成Double解析，再强制转换为Int
     */
    public static final TypeAdapter<Integer> INTEGER = new TypeAdapter<Integer>() {
        @Override
        public Integer read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return 0;
            }
            try {
                return Integer.parseInt(in.nextString());
            }
            catch (Exception e) {
                return 0;
            }

        }

        @Override
        public void write(JsonWriter out, Integer value) throws IOException {
            out.value(value);
        }
    };

    public void callback(String url, JSONObject jo, BaseDelegate delegate) {
        try {
            responStatus = STATUS.fromJson(jo);
            if (responStatus.ret != 0) {
                dataJson = null;
                checkLoginState(responStatus.ret);
                delegate.onMessageResponseFail(url, responStatus.msg, responStatus.ret);
            } else {
                delegate.onMessageResponseSuccess(url, jo);
                dataJson = jo.optJSONObject("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       printJson(url, jo);
    }

    private void printJson(String url, JSONObject jo) {
        AjaxCallback callback = aq.getCallback();
        String paramString = "";
        if (callback != null) {
            HashMap<String, Object> params = (HashMap<String, Object>) callback.getParams();
            paramString = getParamString(params);
        }
        //输出json日志
        KLog.i("op_json", url + "========>");
        KLog.json("op_json==>param", paramString);
        if (jo == null) {
            KLog.i("op_json", "null");
        } else {
            KLog.i("op_json", jo.toString());
        }
    }

    private String getParamString(HashMap<String, Object> params) {
        if (params == null) {
            return "";
        }
        JSONObject json = new JSONObject();
        try {
            for (Map.Entry entry : params.entrySet()) {
                json.put(entry.getKey().toString(), entry.getValue());
            }
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    //if登录不成功则情况SharePreferent里的userInfo
    public void checkLoginState(int resultCode) {
        //session过期 与 ErrorCodeConst.NOLOGIN 的错误码一一对应
        if (resultCode == 101110) {
            CookieUtils.clearCookie();
            LoginUtils.exitLogin();
        }
    }    //加载更多

    public void setIndex(JSONObject dataJson) {
        index = dataJson.optInt("index");
        int sum = dataJson.optInt("sum");
        int count = dataJson.optInt("count");
        hasNext = sum > index ? true : false;
    }

    public void isLoadMore(boolean isLoadMore) {
        index = isLoadMore ? index : 0;
    }

    //判断是否是加载更多
    public boolean isLoadMore() {
        return index != 0;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    /**
     * 用于分页的页数：param("index",getIndex())
     * @return
     */
    public int getIndex() {
        return index;
    }


    public void cancelRquest() {
        aq.ajaxCancel();
    }

    /**
     * editby Eagle 添加吧图片文件转换成url地址的方法
     */
    public interface OnTranFileCallback{

        void onTranResult(Map<String, Object> params, STATUS code);
    }

    public void tranFile2Url(HashMap<String, Object> params,String uploadDomain, OnTranFileCallback delegate) {
        String url = uploadDomain;

        //获取文件类型的参数
        ArrayList<String> keys = new ArrayList<>();
        for (String key :params.keySet()) {
            Object item = params.get(key);
            if(item instanceof File && ((File) item).exists()){
                keys.add(key);
            }
        }
        upLoadFile(keys,params,uploadDomain,delegate);


    }

    private void upLoadFile(final ArrayList<String> keys, final HashMap<String, Object> paramsMap, final String domain, final OnTranFileCallback delegate) {
        HashMap<String,File> fileParams = new HashMap<>();
        if(keys.size()>0){
            final String key = keys.get(0);
            File file = (File) paramsMap.get(key);
            fileParams.put("img",file);
            aq.ajax(domain,fileParams,JSONObject.class,new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject jo, AjaxStatus status) {
                    try {
                        responStatus = STATUS.fromJson(jo);
                        if (responStatus.ret != 0) {
                            dataJson = null;
                            delegate.onTranResult(params,responStatus);
                            return;
                        } else {
                            dataJson = jo.optJSONObject("data");
                            String imgUrl = dataJson.optString("url");
                            paramsMap.put(key,imgUrl);
                            keys.remove(0);
                            upLoadFile(keys,paramsMap,domain,delegate);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        delegate.onTranResult(params,responStatus);
                        return;
                    }
                }
            });
        }else{
            responStatus.ret = 0;
            responStatus.msg = "";
            delegate.onTranResult(paramsMap,responStatus);
            return;

        }
    }
}
