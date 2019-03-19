package cn.ck.security.network.services;

import com.google.gson.JsonObject;

import java.util.List;

import cn.ck.security.business.account.model.User;
import cn.ck.security.business.security.model.Car;
import cn.ck.security.network.response.ApiResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author chengkun
 * @since 2019/1/3 21:31
 */
public interface ApiService {

    /**
     * 登录
     *
     * @param id       id
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("user/loginapp")
    Call<ApiResponse<User>> login(@Field("id") String id, @Field("password") String password);


    /**
     * 通过车牌号查询信息
     *
     * @param carNum 车牌
     * @return
     */
    @FormUrlEncoded
    @POST("pass/search")
    Call<ApiResponse<List<Car>>> searchByNum(@Field("car_number") String carNum);

    /**
     * 车牌号模糊查询
     *
     * @param carNum 车牌
     * @return
     */
    @GET("pass/like/{carNumber}")
    Call<ApiResponse<List<Car>>> fuzzySearch(@Path("carNumber") String carNum);

    /**
     * 获取没过的列表
     */
    @GET("pass/getpasses/{limit}/{offset}")
    Call<ApiResponse<List<Car>>> getUnPassedCars(@Path("offset") String start, @Path("limit") String offset);

    /**
     * 获取审核过的列表
     */
    @GET("pass/getcheckedpasses/{limit}/{offset}")
    Call<ApiResponse<List<Car>>> getPassedCars(@Path("offset") String start, @Path("limit") String offset);

    /**
     * 获取普通安保处人员的id列表
     * 只用有安保处队长的token才有权限。安保处人员id改为从1050~1070，1
     * 066~1070为安保处队长账号，普通安保人员的role为1，安保处人员的role为66.所有安保处的role可用小于100确定
     */
    @GET("user/getNormalScannerList")
    Call<ApiResponse<List<JsonObject>>> getNormalScannerList();

    /**
     * 只能重置安保处普通人员的密码
     *
     * @param id       人员id
     * @param password 重置后密码
     */
    @FormUrlEncoded
    @POST("user/resetNormalScannerPassword")
    Call<ApiResponse<Void>> resetPwd(@Field("id") String id, @Field("password") String password);
}
