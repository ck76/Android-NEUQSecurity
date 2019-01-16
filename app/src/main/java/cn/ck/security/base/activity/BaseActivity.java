package cn.ck.security.base.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ck.security.utils.StatusBarUtils;
import cn.ck.security.wedget.LoadingDialog;

/**
 * @author FanHongyu.
 * @since 18/4/23 17:53.
 * email fanhongyu@hrsoft.net.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected Unbinder mUnbinder;
    protected LoadingDialog mLoadingDialog;

    /**
     * 获取日志输出标志
     */
    protected final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        removeFragmentState(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        mContext = this;
        mLoadingDialog = new LoadingDialog(mContext);
        initActivity(savedInstanceState);
    }

    protected void initActivity(Bundle savedInstanceState) {
        initData(savedInstanceState);
        initView();
    }

    /**
     * 加载数据
     */
    protected abstract void initData(Bundle savedInstanceState);


    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 获取布局Id
     *
     * @return 布局Id
     */
    protected abstract int getLayoutId();


    /**
     * 启动新活动
     */
    protected void startActivity(Class<?> newActivity) {
        startActivity(newActivity, null);
    }

    /**
     * 启动新活动(传输简单数据)
     *
     * @param newActivity
     * @param bundle
     */
    protected void startActivity(Class<?> newActivity, Bundle bundle) {
        Intent intent = new Intent(this, newActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 启动新活动
     *
     * @param newActivity
     * @param REQUEST_CODE
     */
    protected void startActivityForResult(Class<?> newActivity, int REQUEST_CODE) {
        Intent intent = new Intent(this, newActivity);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }


    /**
     * 清除fragment状态
     *
     * @param savedInstanceState
     */
    protected void removeFragmentState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");
            savedInstanceState.remove("android:fragments");
        }
    }

    /**
     * 是否设置沉浸状态栏
     *
     * @param isSetStatusBar
     */
    public void setImmersiveStatusBar(boolean isSetStatusBar) {
        if (isSetStatusBar) {
            StatusBarUtils.setImmersiveStatusBar(this.getWindow());
        }
    }

    /**
     * 使布局背景填充状态栏
     */
    public void setLayoutNoLimits(boolean isNoLimits) {
        // 布局背景填充状态栏 与键盘监听冲突
        if (isNoLimits) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }
    }

    /**
     * 设置状态栏字体为深色
     * 注意：如果同时设置了沉浸状态栏，如果开启沉浸状态栏，必须在设置沉浸状态栏之后调用
     *
     * @param isDeepColor
     */
    public void setDeepColorStatusBar(boolean isDeepColor) {
        if (isDeepColor) {
            StatusBarUtils.setDeepColorStatusBar(this.getWindow());
        }
    }

}
