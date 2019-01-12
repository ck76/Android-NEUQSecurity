package cn.ck.security.network.exception;

import com.google.gson.annotations.SerializedName;

/**
 * 服务器定义错误
 *
 * @author chengkun
 * @since 2018/10/30 02:15
 */

public class ServerException extends RuntimeException {

    /**
     * 错误码
     */
    @SerializedName("code")
    private int code;
    /**
     * 错误信息
     */
    @SerializedName("message")
    private String msg;

    public ServerException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
