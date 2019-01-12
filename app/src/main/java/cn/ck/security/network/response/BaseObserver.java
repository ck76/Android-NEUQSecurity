package cn.ck.security.network.response;

import cn.ck.security.App;
import cn.ck.security.common.Config;
import cn.ck.security.network.exception.ExceptionEngine;
import cn.ck.security.utils.ToastUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author chengkun
 * @since 2018/11/24 03:16
 */
public abstract class BaseObserver<T> implements Observer<ApiResponse<T>> {

    protected abstract void onDataBack(ApiResponse<T> responseBody);

    protected abstract void onError(int code);

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(ApiResponse<T> response) {
        if (response.getCode()==Config.NET_CORRECT_CODE){
            onDataBack(response);
        }else {
            onError(response.getCode());
        }
    }

    @Override
    public void onError(Throwable e) {
        ToastUtil.show(App.getmContext(), ExceptionEngine.handleException(e).getMsg());
    }

    @Override
    public void onComplete() {

    }
}
