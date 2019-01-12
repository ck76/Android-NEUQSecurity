package cn.ck.security.base.mvp.impl;

/**
 * @author FanHongyu.
 * @since 18/4/23 19:45.
 * email fanhongyu@hrsoft.net.
 */

public interface IDataCallback<T> {

    void onSuccess(T data);

    void onFailed();

    void whatever();
}
