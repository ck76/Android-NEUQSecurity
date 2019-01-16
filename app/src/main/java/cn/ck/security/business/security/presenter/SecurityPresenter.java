package cn.ck.security.business.security.presenter;

import cn.ck.security.base.mvp.presenter.BasePresenter;
import cn.ck.security.business.security.contract.SecurityContract;

/**
 * @author chengkun
 * @since 2019/1/16 18:53
 */
public class SecurityPresenter extends BasePresenter<SecurityContract.SecurityView> implements
        SecurityContract.SecurityPresenter {
    public SecurityPresenter(SecurityContract.SecurityView view) {
        super(view);
    }
}
