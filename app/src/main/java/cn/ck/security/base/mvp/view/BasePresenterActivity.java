package cn.ck.security.base.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.ck.security.base.activity.BaseActivity;
import cn.ck.security.base.mvp.impl.IBaseContract;
import cn.ck.security.utils.ToastUtil;


/**
 * @author FanHongyu.
 * @since 18/4/23 19:17.
 * email fanhongyu@hrsoft.net.
 */

public abstract class BasePresenterActivity<P extends IBaseContract.IBasePresenter> extends
        BaseActivity implements IBaseContract.IBaseView {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initPresenter();
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取Presenter实例
     *
     * @return
     */
    protected abstract P getPresenter();

    /**
     * 初始化绑定状态
     */
    @SuppressWarnings("unchecked")
    private void initPresenter() {
        mPresenter = getPresenter();
    }


    @Override
    protected void onDestroy() {

        if (mPresenter != null) {
            mPresenter.unBindView();
            mPresenter = null;
        }
        if (mLoadingDialog != null) {
            closeLoading();
        }
        super.onDestroy();
    }

    @Override
    public void showLoading() {
        mLoadingDialog.show();
    }

    @Override
    public void closeLoading() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showMessage(String msg) {
        ToastUtil.show(mContext,msg);
    }
}
