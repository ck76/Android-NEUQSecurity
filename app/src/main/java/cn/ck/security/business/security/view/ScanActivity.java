package cn.ck.security.business.security.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ck.security.R;
import cn.ck.security.base.activity.BaseActivity;
import cn.ck.security.utils.StatusBarUtils;

public class ScanActivity extends BaseActivity {
    @BindView(R.id.btn_light)
    ImageView btnLight;
    @BindView(R.id.image_back)
    ImageView imageBack;

    private CodeUtils.AnalyzeCallback mAnalyzeCallback;
    private boolean isOpen;

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void initView() {
        initCallBack();
        StatusBarUtils.setImmersiveStatusBar(getWindow());

        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(mAnalyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

    }

    private void initCallBack() {
        /**
         * 二维码解析回调函数
         */
        mAnalyzeCallback = new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                bundle.putString(CodeUtils.RESULT_STRING, result);
                resultIntent.putExtras(bundle);
                ScanActivity.this.setResult(RESULT_OK, resultIntent);
                ScanActivity.this.finish();
            }

            @Override
            public void onAnalyzeFailed() {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
                bundle.putString(CodeUtils.RESULT_STRING, "");
                resultIntent.putExtras(bundle);
                ScanActivity.this.setResult(RESULT_OK, resultIntent);
                ScanActivity.this.finish();
            }
        };
    }

    @OnClick({R.id.btn_light, R.id.image_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_light:
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }
                break;
            case R.id.image_back:
                finish();
                break;
            default:
                break;
        }
    }
}
