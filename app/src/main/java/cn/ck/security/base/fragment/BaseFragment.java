package cn.ck.security.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ck.security.App;
import cn.ck.security.wedget.LoadingDialog;

/**
 * @author FanHongyu.
 * @since 18/4/23 18:08.
 * email fanhongyu@hrsoft.net.
 */

public abstract class BaseFragment extends Fragment {


    /**
     * 当前Fragment RootView
     */
    protected View mView;
    protected LoadingDialog mLoadingDialog;
    protected Context mContext;
    private Activity activity;

    Unbinder unbinder;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
        mLoadingDialog = new LoadingDialog(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {

        mView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mView);
        initFragment();
        return mView;
    }

    @Override
    public Context getContext() {
        if (activity == null) {
            return App.getInstance();
        }
        return activity;
    }

    protected void initFragment() {
        initData();
        initView();
    }

    /**
     * 获取LayoutId.
     *
     * @return LayoutId 布局文件Id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 初始化View.
     */
    protected abstract void initView();

    /**
     * 初始数据
     */
    protected abstract void initData();


    /**
     * 获取当前Fragment的RootView
     *
     * @return RootView
     */
    protected View getRootView() {
        return mView;
    }


    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}