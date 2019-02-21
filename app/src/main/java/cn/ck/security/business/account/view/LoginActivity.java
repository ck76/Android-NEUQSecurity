package cn.ck.security.business.account.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
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
import cn.ck.security.utils.KeyBoardUtil;
import cn.ck.security.utils.ToastUtil;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_pwd)
    EditText editPwd;
    @BindView(R.id.fl_login)
    FrameLayout flLogin;

    private String id;
    private String pwd;
    private String token;

    @Override
    protected void initData(Bundle savedInstanceState) {
        token = CacheUtil.getSP().getString(CacheKey.TOKEN, "");
    }

    @Override
    protected void initView() {
        KeyBoardUtil.scrollLayoutAboveKeyBoard(this, root, flLogin);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (!TextUtils.isEmpty(token)) {
            startActivity(SecurityActivity.class);
            finish();
        }
        editPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                switch (actionId) {
                    //点击GO键
                    case EditorInfo.IME_ACTION_GO:
                        return true;
                    //点击Done
                    case EditorInfo.IME_ACTION_DONE:
                        if (checkFormat()) {
                            login();
                        }
                        return true;
                    //点击Next
                    case EditorInfo.IME_ACTION_NEXT:
                        return true;
                    //点击Previous
                    case EditorInfo.IME_ACTION_PREVIOUS:
                        return true;
                    //点击None
                    case EditorInfo.IME_ACTION_NONE:
                        return true;
                    //点击Send
                    case EditorInfo.IME_ACTION_SEND:
                        return true;
                    default:
                        break;
                }
                //如果需要消费事件，则需要return true
                return true;
            }
        });
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

    /**
     * 登录
     */
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
                        ToastUtil.show(App.getAppContext(), "登录成功");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
