package cn.ck.security.network.response;

import cn.ck.security.App;
import cn.ck.security.network.exception.ExceptionEngine;
import cn.ck.security.utils.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author chengkun
 * @since 2018/11/24 03:16
 */
public abstract class ApiCallBack<T> implements Callback<ApiResponse<T>> {

    protected abstract void onDataBack(ApiResponse<T> response);

    protected abstract void onError(int code);

    @Override
    public void onResponse(Call<ApiResponse<T>> call, Response<ApiResponse<T>> response) {
        if (response != null && response.body() != null) {
            onDataBack(response.body());
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        ToastUtil.show(App.getAppContext(), ExceptionEngine.handleException(t).getMsg());
        onError(ExceptionEngine.handleException(t).getCode());
    }
}
