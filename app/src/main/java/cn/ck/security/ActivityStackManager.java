package cn.ck.security;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import java.util.Stack;

import cn.ck.security.business.account.view.LoginActivity;
import cn.ck.security.utils.ToastUtil;

/**
 * Activity栈管理
 */

public class ActivityStackManager {

    private static final long EXIT_TIME = 2000;
    private static long sTimeMillis;

    private volatile static ActivityStackManager instance;
    private static Stack<Activity> activityStack;

    /**
     * 私有构造
     */
    private ActivityStackManager() {
        activityStack = new Stack<>();
    }

    /**
     * 单例
     *
     * @return
     */
    public static ActivityStackManager getManager() {
        if (instance == null) {
            synchronized (ActivityStackManager.class) {
                if (instance == null) {
                    instance = new ActivityStackManager();
                }
            }
        }
        return instance;
    }

    /**
     * 压栈
     *
     * @param activity
     */
    public void push(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 出栈
     *
     * @return
     */
    public Activity pop() {
        if (activityStack.isEmpty()) {
            return null;
        }
        return activityStack.pop();
    }

    /**
     * 获取栈顶元素
     *
     * @return
     */
    public Activity peek() {
        if (activityStack.isEmpty()) {
            return null;
        }
        return activityStack.peek();
    }

    /**
     * 出栈该元素和该元素直上的元素
     *
     * @param activity
     */
    public void remove(Activity activity) {

        if (activityStack.isEmpty()) {
            return;
        }

        if (activity == activityStack.peek()) {
            activityStack.pop();
        } else {
            activityStack.remove(activity);
        }
    }

    /**
     * 判断指定元素是否在栈内
     *
     * @param activity
     * @return
     */
    public boolean contains(Activity activity) {
        return activityStack.contains(activity);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        ToastUtil.cancelToast();
        while (!activityStack.isEmpty()) {
            activityStack.pop().finish();
        }
    }

    /**
     * token缺失重新登录
     */
    public void tokenError() {
        while (!activityStack.isEmpty() && activityStack.size() > 1) {
            activityStack.pop().finish();
        }
        Activity activity = activityStack.pop();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 双击退出应用
     */
    public void exitAppWithTwiceClick() {
        //获取当前时间戳
        long currentTime = System.currentTimeMillis();
        if ((currentTime - sTimeMillis) > EXIT_TIME) {
            ToastUtil.show(App.getAppContext(), R.string.toast_twice_click_exit);
            //更新当前时间戳
            sTimeMillis = currentTime;
        } else {
            exitApp(App.getAppContext());
        }
    }

    /**
     * 退出App
     *
     * @param context
     */
    public void exitApp(Context context) {

        ToastUtil.cancelToast();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            activityManager.killBackgroundProcesses(context.getPackageName());
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }

        finishAllActivity();

        Process.killProcess(Process.myPid());
    }
}
