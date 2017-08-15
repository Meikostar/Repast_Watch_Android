package com.canplay.repast_wear.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.canplay.repast_wear.base.manager.AppManager;
import com.canplay.repast_wear.util.ExceptionHandler;

import cn.jpush.android.api.JPushInterface;

/**
 * App基类
 * Created by peter on 2016/9/11.
 */

public class BaseApplication extends Application{
    //全局单例
    AppComponent mAppComponent;
    public static  BaseApplication cplayApplication;
    public static BaseApplication getInstance() {
        if (cplayApplication == null) {
            cplayApplication = new BaseApplication();
        }

        return (BaseApplication) cplayApplication;
    }
    @Override
    public void onCreate(){
        // TODO Auto-generated method stub
        super.onCreate();
        //生成全局单例
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        mAppComponent.inject(this);
        ApplicationConfig.setAppInfo(this);
        //全局异常处理
        new ExceptionHandler().init(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    /**
     * 退出应用
     */
    public void exit(){
        AppManager.getInstance(this).exitAPP(this);
    }
    //    /**
    //     * 退出应用
    //     *
    //     * @param exitAppListener 应用退出时监听，在应用完全退出前可进行额外操作。
    //     */
    //    public void exit(OnExitAppListener exitAppListener){
    //        //应用退出监听
    //        if(exitAppListener != null)
    //            exitAppListener.onExit();
    //        //杀进程，关闭应用
    //        android.os.Process.killProcess(android.os.Process.myPid());
    //        android.app.ActivityManager activityMgr = (android.app.ActivityManager) getSystemService(ACTIVITY_SERVICE);
    //        activityMgr.restartPackage(getPackageName());
    //        System.exit(0);
    //        System.gc();
    //    }
    //
    //    /**
    //     * 应用退出时监听
    //     */
    //    public interface OnExitAppListener{
    //        public void onExit();
    //    }

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }
}
