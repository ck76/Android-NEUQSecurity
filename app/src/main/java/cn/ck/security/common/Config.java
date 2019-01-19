package cn.ck.security.common;

/**
 * @author FanHongyu.
 * @since 18/4/18 17:31.
 * email fanhongyu@hrsoft.net.
 */

public class Config {

    public static final boolean IS_DEBUG = true;

    /**
     * BuglyAppId
     */
    public static final String BUGLY_APP_ID = "235d3eed39";

    /**
     * 当前app版本号
     */
    public static final String APP_VERSION = "";

    /**
     * 网络请求BaseURL
     */
    public static final String APP_SERVER_BASE_URL = "http://neuqsecurity.lyzwhh.top/";

    /**
     * 网络请求连接超时时间，单位：s
     */
    public static final int APP_SERVER_CONNECT_TIME_OUT = 8;

    /**
     * 正确返回码
     */
    public static final int NET_CORRECT_CODE = 0;

    /**
     * 正确返回码数组
     */
    public static final int[] NET_CORRECT_CODE_ARRAYS = {1000,0};


}
