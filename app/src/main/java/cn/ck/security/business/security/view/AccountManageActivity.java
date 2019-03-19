package cn.ck.security.business.security.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.ck.security.R;
import cn.ck.security.base.activity.BaseActivity;
import cn.ck.security.business.security.adapter.AccountManageAdapter;
import cn.ck.security.network.NetworkFactory;
import cn.ck.security.network.response.ApiCallBack;
import cn.ck.security.network.response.ApiResponse;
import cn.ck.security.network.services.ApiService;
import cn.ck.security.wedget.ChangePwdDialog;

import static cn.ck.security.business.security.adapter.AccountManageAdapter.FILED_ID;

public class AccountManageActivity extends BaseActivity {

    @BindView(R.id.recv_account_manage)
    RecyclerView recvAccountManage;

    private List<JsonObject> userList;
    private AccountManageAdapter mManageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_manage;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userList = new ArrayList<>();
        getNormalScannerList();
    }

    private void getNormalScannerList() {
        NetworkFactory
                .getInstance()
                .creatService(ApiService.class)
                .getNormalScannerList()
                .enqueue(new ApiCallBack<List<JsonObject>>() {
                    @Override
                    protected void onDataBack(ApiResponse<List<JsonObject>> response) {
                        userList = response.getData();
                        mManageAdapter.notifyDataChanged(userList);
                    }

                    @Override
                    protected void onError(int code) {

                    }
                });
    }

    @Override
    protected void initView() {
        initRecv();
    }

    /**
     * 初始化recv
     */
    private void initRecv() {
        recvAccountManage.setLayoutManager(new LinearLayoutManager(this));
        mManageAdapter = new AccountManageAdapter(this, userList);
        recvAccountManage.setAdapter(mManageAdapter);
        mManageAdapter.setOnItemClickListener(new AccountManageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showDialog(userList.get(position).get(FILED_ID).getAsString());
            }
        });
    }

    /**
     * 弹修改密码dialog
     */
    private void showDialog(String userId) {
        new ChangePwdDialog(mContext, userId)
                .setHintText()
                .show();
    }

}
