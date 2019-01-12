package cn.ck.security.base.mvp.impl;

/**
 * @author fhyPayaso
 * @since 2018/5/3 on 上午1:55
 * fhyPayaso@qq.com
 */
public interface IBaseContract {

    interface IBaseView {
        void showLoading();

        void closeLoading();

        void showMessage(String msg);
    }

    interface IBasePresenter {


        /**
         * p层与v层解绑
         */
        void unBindView();

        /**
         * p层是否绑定了v层
         *
         * @return
         */
        boolean isBindView();
    }

}
