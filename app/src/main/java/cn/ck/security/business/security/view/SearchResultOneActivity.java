package cn.ck.security.business.security.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ck.security.R;
import cn.ck.security.base.activity.BaseActivity;

public class SearchResultOneActivity extends BaseActivity {

    @BindView(R.id.edit_input)
    EditText editInput;
    @BindView(R.id.txt_search)
    TextView txtSearch;
    @BindView(R.id.txt_hint)
    TextView txtHint;
    @BindView(R.id.recv_result)
    RecyclerView recvResult;

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_result_one;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
