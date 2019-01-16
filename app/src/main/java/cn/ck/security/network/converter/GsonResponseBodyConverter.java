package cn.ck.security.network.converter;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import cn.ck.security.common.Config;
import cn.ck.security.network.exception.ServerException;
import cn.ck.security.network.response.ApiResponse;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 响应时解析ResponseBody
 *
 * @author FanHongyu.
 * @since 18/1/17 22:40.
 * email fanhongyu@hrsoft.net.
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson mGson;
    private final Type mType;

    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.mGson = gson;
        this.mType = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        //将返回的json数据储存在String类型的response中
        String response = value.string();
        //将外层的数据解析到ApiResponse中
        ApiResponse apiResponse = mGson.fromJson(response, ApiResponse.class);

        //遍历正确码
        for (int code : Config.NET_CORRECT_CODE_ARRAYS) {
            if (apiResponse.getCode() == code) {
                //直接解析，正确请求不会导致json解析异常
                return mGson.fromJson(response, mType);
            }
        }
        //通过抛出自定义异常传递错误码及错误信息
        throw (ServerException) mGson.fromJson(response, ServerException.class);
    }
}

