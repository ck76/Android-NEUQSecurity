package cn.ck.security;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.ArrayList;
import java.util.List;

import cn.ck.security.utils.CacheUtil;
import cn.ck.security.utils.ToastUtil;

/**
 * @author FanHongyu.
 * @since 18/4/23 17:48.
 * email fanhongyu@hrsoft.net.
 */

public class App extends Application {


    private static final long EXIT_TIME = 2000;

    private static long sTimeMillis;

    private static App sInstance;

    private static Context mContext;

    private static List<Activity> sActivityList = new ArrayList<>();


    public static App getInstance() {
        return sInstance;
    }

    public static Context getAppContext(){
        return mContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mContext = getApplicationContext();
        registerActivityLifecycleCallbacks(getActivityLifecycleCallbacks());

        //qrcode
        ZXingLibrary.initDisplayOpinion(this);

        //内存泄漏检测工具
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }


    /**
     * Activity生命周期
     */
    private ActivityLifecycleCallbacks getActivityLifecycleCallbacks() {

        return new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                sActivityList.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                sActivityList.remove(activity);
            }
        };
    }


    /**
     * 移除Activity
     *
     * @param activity act
     */
    public static void removeActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    /**
     * 清除所有Activity
     */
    public void removeAllActivity() {
        for (Activity activity : sActivityList) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        ToastUtil.cancelToast();
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        removeAllActivity();
    }

    /**
     * 双击退出应用
     */
    public void exitAppWithTwiceClick() {
        //获取当前时间戳
        long currentTime = System.currentTimeMillis();
        if ((currentTime - sTimeMillis) > EXIT_TIME) {
            ToastUtil.show(mContext, R.string.toast_twice_click_exit);
            //更新当前时间戳
            sTimeMillis = currentTime;
        } else {
            removeAllActivity();
        }
    }

    /**
     * 切换账户
     */
    public void exitAccount() {
        removeAllActivity();
        CacheUtil.clear();
    }
}