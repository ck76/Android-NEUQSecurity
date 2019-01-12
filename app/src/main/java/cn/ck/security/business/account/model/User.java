package cn.ck.security.business.account.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author chengkun
 * @since 2019/1/12 19:40
 */
public class User {
    /**
     * userId : 1010
     * tokenStr : 0fbc16e112a7b7906cf789e6797acaac
     */

    @SerializedName("user_id")
    private int userId;
    private String tokenStr;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTokenStr() {
        return tokenStr;
    }

    public void setTokenStr(String tokenStr) {
        this.tokenStr = tokenStr;
    }
}
