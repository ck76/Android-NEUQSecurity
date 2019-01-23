package cn.ck.security.business.security.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ck.security.App;
import cn.ck.security.R;
import cn.ck.security.base.activity.BaseActivity;
import cn.ck.security.business.security.Constans;
import cn.ck.security.business.security.adapter.ResultListAdapter;
import cn.ck.security.business.security.model.Car;
import cn.ck.security.network.NetworkFactory;
import cn.ck.security.network.response.ApiCallBack;
import cn.ck.security.network.response.ApiResponse;
import cn.ck.security.network.services.ApiService;
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

    private ResultListAdapter mAdapter;
    private List<Car> mCars;

    private String mVoiceCarNum;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_result_one;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mVoiceCarNum = getIntent().getStringExtra(Constans.CAR_NUM);
        if (!TextUtils.isEmpty(mVoiceCarNum)) {
            fuzzySearch(mVoiceCarNum);
        }
    }

    @Override
    protected void initView() {
        editInput.addTextChangedListener(this);
    }

    private void initRecv() {
        recvResult.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ResultListAdapter(mCars, this);
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
    public void onViewClicked() {
        String carNum = editInput.getText().toString().trim();
        if (!TextUtils.isEmpty(carNum)) {
            fuzzySearch(carNum);
        } else {
            ToastUtil.show(App.getAppContext(), "请输入车牌号");
        }
    }

    @OnClick(R.id.image_clear)
    public void onClearClick() {
        editInput.setText("");
    }

    private void fuzzySearch(String carNum) {

        NetworkFactory.getInstance()
                .creatService(ApiService.class)
                .fuzzySearch(carNum)
                .enqueue(new ApiCallBack<List<Car>>() {
                    @Override
                    protected void onDataBack(ApiResponse<List<Car>> response) {
                        mCars = response.getData();
                        initRecv();
                        txtHint.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onError(int code) {

                    }
                });
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
