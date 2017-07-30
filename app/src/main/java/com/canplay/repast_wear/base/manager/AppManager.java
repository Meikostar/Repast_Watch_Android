package com.canplay.repast_wear.base.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.canplay.repast_wear.util.CanplayUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Stack;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * app管理类
 * Created by peter on 2016/9/17.
 */
public class AppManager{

    public static Context context;

    private TelephonyManager tm;
    //获得imei编号
    public static String imei = "";
    //获得包信息
    public static PackageInfo info;

    private static final String TAG = AppManager.class.getSimpleName();

    private static AppManager instance = null;

    private static Stack<Activity> activityStack = null;

    private AppManager(Context context){
        this.context = context;
        CanplayUtils.getDisplayMetrics(context);
    }

    public static AppManager getInstance(Context context){
        if(null == instance)
            synchronized(AppManager.class){
                if(null == instance){
                    instance = new AppManager(context);
                    getPackageInfo();
                }
            }
        return instance;
    }

    /**
     * 把当前Activity添加到栈顶
     *
     * @param at
     */
    public void addActivity(Activity at){
        if(at != null){
            if(activityStack == null){
                activityStack = new Stack<Activity>();
            }
            activityStack.add(at);
        }
    }

    /**
     * 结束当前栈顶的Activity
     */
    public void finishActivity(){
        if(activityStack != null){
            Activity at = activityStack.lastElement();
            finishActivity(at);
        }
    }

    /**
     * 从堆栈中结束指定Activity
     *
     * @param at
     */
    public void finishActivity(Activity at){
        if(activityStack != null && at != null){
            activityStack.remove(at);
            at.finish();
            at = null;
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public void finishActvity(Class<?> cls){
        if(activityStack != null && cls != null){
            for(Activity at : activityStack){
                if(at.getClass().equals(cls)){
                    finishActivity(at);
                }
            }
        }
    }

    /**
     * 结束所有的Activity
     */
    public void finishAllActivity(){
        if(activityStack != null){
            int size = activityStack.size();
            for(int i = 0; i < size; i++){
                Activity activity = activityStack.get(i);
                Logger.e("activity:" + activity.getLocalClassName());
                activity.finish();
            }
            activityStack.clear();
        }
    }


    /**
     * 退出应用程序
     */
    public void exitAPP(Context context){
        finishAllActivity();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(context.getPackageName());
        System.exit(0);
        System.gc();
    }

    /**
     * 获取当前栈里面activity的数量
     *
     * @return
     */
    public int getCount(){
        if(activityStack != null && !activityStack.isEmpty()){
            return activityStack.size();
        }else{
            return 0;
        }
    }

    /*
    获得手机管理器
     */
    public void getTelephonyManager(){
        tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        if(TextUtils.isEmpty(imei)){
            imei = android.os.Build.SERIAL;
        }
        Logger.e("imei" + imei);
    }

    public static void getPackageInfo(){
        try{
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }

    /*
    获得缓存目录
     */
    public static File getCacheDir(){
        Log.i("getCacheDir", "cache sdcard state: " + Environment.getExternalStorageState());
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File cacheDir = context.getExternalCacheDir();
            if(cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())){
                Log.i("getCacheDir", "cache dir: " + cacheDir.getAbsolutePath());
                return cacheDir;
            }
        }
        File cacheDir = context.getCacheDir();
        Log.i("getCacheDir", "cache dir: " + cacheDir.getAbsolutePath());
        return cacheDir;
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public static Activity topActivity(){
        if(activityStack == null){
            throw new NullPointerException("Activity stack is Null,your Activity must extend BaseActivity");
        }
        if(activityStack.isEmpty()){
            return null;
        }
        return activityStack.lastElement();
    }
}
