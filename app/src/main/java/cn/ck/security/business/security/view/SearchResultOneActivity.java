package cn.ck.security.business.security.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

public class SearchResultOneActivity extends BaseActivity {

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_result_one;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
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
        fuzzySearch();
    }

    @OnClick(R.id.image_clear)
    public void onClearClick() {
        editInput.setText("");
    }

    private void fuzzySearch() {
        String carNum = editInput.getText().toString().trim();
        if (!TextUtils.isEmpty(carNum)) {
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
        } else {
            ToastUtil.show(App.getAppContext(), "请输入车牌号");
        }
    }
}
