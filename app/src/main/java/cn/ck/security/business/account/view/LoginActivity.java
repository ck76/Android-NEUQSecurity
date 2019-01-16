package cn.ck.security.business.account.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ck.security.App;
import cn.ck.security.R;
import cn.ck.security.base.activity.BaseActivity;
import cn.ck.security.business.account.model.User;
import cn.ck.security.business.security.view.SecurityActivity;
import cn.ck.security.common.CacheKey;
import cn.ck.security.network.NetworkFactory;
import cn.ck.security.network.response.ApiCallBack;
import cn.ck.security.network.response.ApiResponse;
import cn.ck.security.network.services.ApiService;
import cn.ck.security.utils.CacheUtil;
import cn.ck.security.utils.ToastUtil;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_pwd)
    EditText editPwd;

    private String id;
    private String pwd;
    private String token;

    @Override
    protected void initData(Bundle savedInstanceState) {
        token = CacheUtil.getSP().getString(CacheKey.TOKEN, "");
    }

    @Override
    protected void initView() {
        if (!TextUtils.isEmpty(token)) {
            startActivity(SecurityActivity.class);
            finish();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (checkFormat()) {
            login();
        }
    }

    private void login() {
        mLoadingDialog.show();
        NetworkFactory
                .getInstance()
                .creatService(ApiService.class)
                .login(id, pwd)
                .enqueue(new ApiCallBack<User>() {
                    @Override
                    protected void onDataBack(ApiResponse<User> response) {
                        CacheUtil.put(CacheKey.TOKEN, response.getData().getTokenStr());
                        ToastUtil.show(App.getAppContext(), "登陆成功");
                        startActivity(SecurityActivity.class);
                        finish();
                        mLoadingDialog.dismiss();
                    }

                    @Override
                    protected void onError(int code) {
                        mLoadingDialog.dismiss();
                    }
                });
    }

    private boolean checkFormat() {
        id = editName.getText().toString().trim();
        pwd = editPwd.getText().toString().trim();
        if (TextUtils.isEmpty(id)) {
            ToastUtil.show(App.getAppContext(), "账号不可为空");
            return false;
        } else if (TextUtils.isEmpty(pwd)) {
            ToastUtil.show(App.getAppContext(), "密码不可为空");
            return false;
        }
        return true;
    }
}
