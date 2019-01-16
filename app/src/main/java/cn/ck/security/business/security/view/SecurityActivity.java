package cn.ck.security.business.security.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ck.security.App;
import cn.ck.security.R;
import cn.ck.security.base.mvp.view.BasePresenterActivity;
import cn.ck.security.business.account.view.LoginActivity;
import cn.ck.security.business.security.Constans;
import cn.ck.security.business.security.contract.SecurityContract;
import cn.ck.security.business.security.model.Car;
import cn.ck.security.business.security.presenter.SecurityPresenter;
import cn.ck.security.common.CacheKey;
import cn.ck.security.common.CommonConstans;
import cn.ck.security.network.NetworkFactory;
import cn.ck.security.network.response.ApiCallBack;
import cn.ck.security.network.response.ApiResponse;
import cn.ck.security.network.services.ApiService;
import cn.ck.security.utils.CacheUtil;
import cn.ck.security.utils.DialogUtil;
import cn.ck.security.utils.ToastUtil;

public class SecurityActivity extends BasePresenterActivity<SecurityContract.SecurityPresenter>
        implements SecurityContract.SecurityView {

    public static final int REQUEST_CODE = 200;

    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.image_scan)
    ImageView imageScan;
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.image_voice)
    ImageView imageVoice;
    @BindView(R.id.txt_logout)
    TextView txtLogout;

    private String[] permissions;
    private String mScanResult;
    private String mType;
    private String mResult;

    private void transform() {
        String[] resultArray = mScanResult.split("#");
        mType = resultArray[0];
        mResult = resultArray[1];

    }

    @Override
    protected SecurityContract.SecurityPresenter getPresenter() {
        return new SecurityPresenter(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        permissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    protected void initView() {
        initPermission();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_security;
    }

    private void initPermission() {
        AndPermission.with(this)
                .permission(permissions)
                .start();
    }

    @OnClick({R.id.ll_search, R.id.image_scan, R.id.btn_scan, R.id.btn_search, R.id.image_voice,
            R.id.txt_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                startActivity(SearchResultOneActivity.class);
                break;
            case R.id.image_scan:
                startActivityForResult(ScanActivity.class, REQUEST_CODE);
                break;
            case R.id.btn_scan:
                startActivityForResult(ScanActivity.class, REQUEST_CODE);
                break;
            case R.id.btn_search:
                startActivity(SearchResultOneActivity.class);
                break;
            case R.id.image_voice:
                ToastUtil.show(App.getAppContext(), "正在开发中...");
                break;
            case R.id.txt_logout:
                new DialogUtil.QuickDialog(this).setClickListener(() -> {
                    CacheUtil.put(CacheKey.TOKEN, "");
                    finish();
                    startActivity(LoginActivity.class);
                }).showDialog("确认退出登录？");
                break;
            default:
                break;
        }
    }


    private void excute() {
        switch (mType) {
            case CommonConstans
                    .TYPE_CAR:
                searchCar();
                break;
            default:
                break;
        }

    }


    private void searchCar() {
        showLoading();
        NetworkFactory.getInstance()
                .creatService(ApiService.class)
                .searchByNum(mResult)
                .enqueue(new ApiCallBack<List<Car>>() {
                    @Override
                    protected void onDataBack(ApiResponse<List<Car>> response) {
                        closeLoading();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constans.CAR_INFO, response.getData().get(0));
                        startActivity(SearchResultTwoActivity.class, bundle);
                    }

                    @Override
                    protected void onError(int code) {
                        closeLoading();
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    mScanResult = bundle.getString(CodeUtils.RESULT_STRING);
                    transform();
                    excute();
                    Toast.makeText(this, "解析结果:" + mScanResult, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}
