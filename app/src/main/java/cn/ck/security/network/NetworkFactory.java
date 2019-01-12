package cn.ck.security.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cn.ck.security.common.CacheKey;
import cn.ck.security.common.Config;
import cn.ck.security.network.converter.ResponseConverterFactory;
import cn.ck.security.utils.CacheUtil;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * @author chengkun
 * @since 2018/11/24 02:20
 */

public final class NetworkFactory {

    private OkHttpClient sOkHttpClient;
    private Retrofit sRetrofit;
    private HashMap<String, Object> mServiceCache;

    private NetworkFactory() {
        mServiceCache = new HashMap<>();
    }

    public static NetworkFactory getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        private static NetworkFactory sInstance = new NetworkFactory();
    }

    /**
     * 构造Retrofit,设置相关参数
     *
     * @return Retrofit
     */
    private Retrofit getRetrofit() {

        if (sRetrofit == null) {

            sRetrofit = new Retrofit.Builder()
                    .client(getOkHttpClient())
                    .baseUrl(Config.APP_SERVER_BASE_URL)
                    //增加对返回值为自定义Response类型的支持,默认是Gson
                    .addConverterFactory(ResponseConverterFactory.create())
                    //增加对RxJava的适配
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return sRetrofit;
    }

    /**
     * 构造Retrofit
     */
    public <T> T creatService(Class<T> service) {
        T retrofitService = (T) mServiceCache.get(service.getCanonicalName());
        if (retrofitService == null) {
            retrofitService = getRetrofit().create(service);
            mServiceCache.put(service.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }

    /**
     * 构造OkHttp客户端，设置相关参数
     *
     * @return OkHttp客户端
     */
    private OkHttpClient getOkHttpClient() {

        if (sOkHttpClient == null) {

            sOkHttpClient = new OkHttpClient.Builder()
                    //设置超时时间为8s
                    .connectTimeout(Config.APP_SERVER_CONNECT_TIME_OUT, TimeUnit.SECONDS)
                    //设置出现错误时重新连接
                    .retryOnConnectionFailure(true)
                    //添加应用拦截器，统一打印请求与响应的json
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    //添加网络拦截器，发送请求时加入token
                    .addNetworkInterceptor(getNetworkInterceptor())
                    .build();

        }
        return sOkHttpClient;
    }


    /**
     * 自定义网络拦截器,添加token请求头
     *
     * @return Interceptor
     */
    private Interceptor getNetworkInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String token = CacheUtil.getSP().getString(CacheKey.TOKEN, "");
                //请求时加入token
                Request request = chain.request().newBuilder()
                        .header(CacheKey.TOKEN, token)
                        .build();
                return chain.proceed(request);
            }
        };
    }
}