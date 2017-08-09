package com.example.soda.soda20.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.example.soda.soda20.globle.User;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import java.util.Iterator;
import java.util.List;


/**
 * Created by soda on 2017/7/26.
 */

public class AppInit extends Application{
    private final String TAG = "AppInit";

    private int lifeCount = 0;
    public User userGloble;
    @Override
    public void onCreate() {
        super.onCreate();
        initEMClient();
        instance = this;
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
//                Log.d(TAG,"onActivityCreated" + activity.getLocalClassName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
//                Log.d(TAG,"onActivityStarted" + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
//                Log.d(TAG,"onActivityResumed" + activity.getLocalClassName() + "life count = " + lifeCount);
            }

            @Override
            public void onActivityPaused(Activity activity) {
//                Log.d(TAG,"onActivityPaused" + activity.getLocalClassName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
//                Log.d(TAG,"onActivityStopped" + activity.getLocalClassName() + "life count =" + lifeCount);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
//                Log.d(TAG,"onActivitySaveInstanceState" + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
//                Log.d(TAG,"onActivityDestroyed" + activity.getLocalClassName());
            }
        });
    }


    /**
     * 外界调用进行初始化全局变量的方法
     */
    public void initGloble() {
        userGloble = User.getInstance(getApplicationContext());
    }

    private static AppInit instance;
    public static AppInit getInstance(){
        if (instance == null){
            synchronized (AppInit.class){
                if (instance == null)
                    instance = new AppInit();
            }
        }
        return instance;
    }

    /**
     * 环信的初始化方法，需要在启动程序时进行初始化，且不能重复初始化
     */
    private void initEMClient() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMOptions options = new EMOptions();
        options.setAutoLogin(true);


        //添加好友时需要验证
        options.setAcceptInvitationAlways(false);
        EMClient.getInstance().init(this,options);
        EMClient.getInstance().setDebugMode(true);
    }

    /**
     * 获取线程名的方法
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
