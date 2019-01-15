package cn.ck.security.business.security.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ck.security.R;
import cn.ck.security.base.activity.BaseActivity;

public class SecurityActivity extends BaseActivity {

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

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_security;
    }

    @OnClick({R.id.ll_search, R.id.image_scan, R.id.btn_scan, R.id.btn_search, R.id.image_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                break;
            case R.id.image_scan:
                break;
            case R.id.btn_scan:
                break;
            case R.id.btn_search:
                break;
            case R.id.image_voice:
                break;
            default:
                break;
        }
    }
}
