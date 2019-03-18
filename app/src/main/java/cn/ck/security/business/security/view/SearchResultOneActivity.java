package cn.ck.security.business.security.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ck.security.App;
import cn.ck.security.R;
import cn.ck.security.base.activity.BaseActivity;
import cn.ck.security.business.security.Constans;
import cn.ck.security.business.security.adapter.ItemLineDecoration;
import cn.ck.security.business.security.adapter.ResultListAdapter;
import cn.ck.security.business.security.model.Car;
import cn.ck.security.common.CacheKey;
import cn.ck.security.network.NetworkFactory;
import cn.ck.security.network.response.ApiCallBack;
import cn.ck.security.network.response.ApiResponse;
import cn.ck.security.network.services.ApiService;
import cn.ck.security.utils.CacheUtil;
import cn.ck.security.utils.ToastUtil;

public class SearchResultOneActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.edit_input)
    EditText editInput;
    @BindView(R.id.txt_search)
    TextView txtSearch;
    @BindView(R.id.txt_hint)
    TextView txtHint;
    @BindView(R.id.recv_result)
    RecyclerView recvResult;
    @BindView(R.id.txt_account_manage)
    TextView txtAccountManage;

    private ResultListAdapter mAdapter;
    private ItemLineDecoration mItemLineDecoration;
    private List<Car> mCars;

    private String mVoiceCarNum;
    private SpannableString mSpannableString;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_result_one;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mCars = new ArrayList<>();
        mSpannableString = new SpannableString(txtHint.getText());
        mVoiceCarNum = getIntent().getStringExtra(Constans.CAR_NUM);
        if (!TextUtils.isEmpty(mVoiceCarNum)) {
            editInput.setText(mVoiceCarNum);
            fuzzySearch(mVoiceCarNum);
        }
    }

    @Override
    protected void initView() {
        Log.i("ck", Constans.SEARCH_TIP);
        mSpannableString.setSpan(new StyleSpan(Typeface.BOLD),
                16, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtHint.setText(mSpannableString);
        editInput.addTextChangedListener(this);
        initRecv();
    }

    private void initRecv() {
        //test
//        for (int i = 0; i < 20; i++) {
//            Car car = new Car();
//            car.setCarNumber(String.valueOf(i));
//            mCars.add(car);
//        }
        recvResult.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ResultListAdapter(mCars, this);
        mItemLineDecoration = new ItemLineDecoration(this);
        recvResult.addItemDecoration(mItemLineDecoration);
        recvResult.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ResultListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constans.CAR_INFO, mCars.get(position));
                startActivity(SearchResultTwoActivity.class, bundle);
            }
        });
        mAdapter.setOnCallListener(new ResultListAdapter.OnCallListener() {
            @Override
            public void onCallClick(int position) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mCars.get(position).getPhone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.txt_search)
    public void onSearckTxtClicked() {
        String carNum = editInput.getText().toString().trim();
        if (TextUtils.isEmpty(carNum)) {
            ToastUtil.show(App.getAppContext(), "请输入车牌号");
        } else {
            fuzzySearch(carNum);
        }
    }

    @OnClick(R.id.txt_account_manage)
    public void onManageTxtClicked() {
        startActivity(AccountManageActivity.class);
    }

    @OnClick(R.id.image_clear)
    public void onClearClick() {
        editInput.setText("");
    }

    /**
     * 队长的号码范围是1066~1070 闭区间
     */
    private void fuzzySearch(String carNum) {
        if (carNum.equals(Constans.MANAGE_KEY)
                && Integer.valueOf(CacheUtil.getSP().getString(CacheKey.USER_NAME, "1000")) >= 1066
                && Integer.valueOf(CacheUtil.getSP().getString(CacheKey.USER_NAME, "1000")) <= 1070) {
            txtHint.setVisibility(View.GONE);
            txtAccountManage.setVisibility(View.VISIBLE);
            recvResult.setVisibility(View.GONE);
        } else {
            txtAccountManage.setVisibility(View.GONE);
            recvResult.setVisibility(View.VISIBLE);

            NetworkFactory.getInstance()
                    .creatService(ApiService.class)
                    .fuzzySearch(carNum)
                    .enqueue(new ApiCallBack<List<Car>>() {
                        @Override
                        protected void onDataBack(ApiResponse<List<Car>> response) {
                            mCars = response.getData();
                            mAdapter.notifyDataChanged(mCars);
                            txtHint.setVisibility(View.GONE);
                        }

                        @Override
                        protected void onError(int code) {

                        }
                    });
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        fuzzySearch(charSequence.toString().trim());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public static void startActivity(Context context, String carNum) {
        Intent intent = new Intent(context, SearchResultOneActivity.class);
        intent.putExtra(Constans.CAR_NUM, carNum);
        context.startActivity(intent);
    }
}
