package cn.ck.security.business.security;

import android.Manifest;

/**
 * @author chengkun
 * @since 2019/1/16 16:36
 */
public class Constans {
    public static final String[] permissions={Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static final String CAR_INFO = "carInfo";
    public static final String CAR_NUM = "carNum";
}
