package cn.ck.security;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yanzhenjie.permission.AndPermission;

import java.util.List;

import butterknife.BindView;
import cn.ck.security.base.activity.BaseActivity;
import cn.ck.security.business.security.model.Car;
import cn.ck.security.common.CacheKey;
import cn.ck.security.network.NetworkFactory;
import cn.ck.security.network.response.ApiCallBack;
import cn.ck.security.network.response.ApiResponse;
import cn.ck.security.network.services.ApiService;
import cn.ck.security.utils.CacheUtil;
import cn.ck.security.utils.ToastUtil;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_hello)
    TextView tvHello;

    private String[] permissions;

    @Override
    protected void initData(Bundle savedInstanceState) {

        permissions = new String[]{Manifest.permission.CAMERA};
        CacheUtil.put(CacheKey.TOKEN, "662964be4715897212e6319846d3150f");
    }

    @Override
    protected void initView() {
        initPermission();

        tvHello.setText("mvp");
        tvHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show(App.getAppContext(), "MainActivity");
                NetworkFactory.getInstance().creatService(ApiService.class)
                        .getPassedCars("5", "2")
                        .enqueue(new ApiCallBack<List<Car>>() {
                            @Override
                            protected void onDataBack(ApiResponse<List<Car>> response) {
                                Log.i("ck", response.getData().get(0).getCarNumber());
                            }

                            @Override
                            protected void onError(int code) {

                            }
                        });
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initPermission() {
        AndPermission.with(this)
                .permission(permissions)
                .start();
    }

}
