package cn.ck.security.business.security.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ck.security.R;
import cn.ck.security.base.activity.BaseActivity;
import cn.ck.security.business.security.Constans;
import cn.ck.security.business.security.model.Car;

public class SearchResultTwoActivity extends BaseActivity {

    @BindView(R.id.txt_car_num)
    TextView txtCarNum;
    @BindView(R.id.txt_owner_name)
    TextView txtOwnerName;
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.txt_department)
    TextView txtDepartment;
    @BindView(R.id.image_call)
    ImageView imageCall;
    @BindView(R.id.image_back)
    ImageView imageBack;
    private Car mCar;

    @Override
    protected void initData(Bundle savedInstanceState) {
        mCar = (Car) Objects.requireNonNull(getIntent().getExtras()).getSerializable(Constans.CAR_INFO);
    }

    @Override
    protected void initView() {
        if (mCar != null) {
            txtCarNum.setText(mCar.getCarNumber());
            txtOwnerName.setText(mCar.getName());
            txtPhone.setText(mCar.getPhone());
            txtDepartment.setText(mCar.getDepartment());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_result_two;
    }

    @OnClick(R.id.image_call)
    public void onViewClicked() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mCar.getPhone()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.image_back)
    public void onBackClicked() {
        finish();
    }
}
