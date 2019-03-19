package cn.ck.security.wedget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.ck.security.App;
import cn.ck.security.R;
import cn.ck.security.network.NetworkFactory;
import cn.ck.security.network.response.ApiCallBack;
import cn.ck.security.network.response.ApiResponse;
import cn.ck.security.network.services.ApiService;
import cn.ck.security.utils.DensityUtil;
import cn.ck.security.utils.ToastUtil;

/**
 * 修改密码dialog
 *
 * @author chengkun
 * @since 2019/3/20 01:37
 */
public class ChangePwdDialog {
    private TextView txtHint;
    private EditText editNewPwd;
    private Button btnCancle;
    private Button btnChangePwd;

    private Context mContext;
    private Dialog mDialog;
    private View mContentView;
    private OnClickChangeistener listener;
    private SpannableString spannableString;
    private String userId;


    @SuppressLint("InflateParams")
    public ChangePwdDialog(Context context, String userId) {
        this.mContext = context;
        this.userId = userId;

        mDialog = new Dialog(mContext, R.style.dialog_voice);
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_change_pwd, null);
        txtHint = (TextView) mContentView.findViewById(R.id.txt_hint);
        editNewPwd = (EditText) mContentView.findViewById(R.id.edit_new_pwd);
        btnCancle = (Button) mContentView.findViewById(R.id.btn_cancle);
        btnChangePwd = (Button) mContentView.findViewById(R.id.btn_change_pwd);

        mDialog.setContentView(mContentView);
        mDialog.setCanceledOnTouchOutside(false);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(getNewPwd()) && getNewPwd().length() > 6) {
                    NetworkFactory
                            .getInstance()
                            .creatService(ApiService.class)
                            .resetPwd(userId, getNewPwd())
                            .enqueue(new ApiCallBack<Void>() {
                                @Override
                                protected void onDataBack(ApiResponse<Void> response) {
                                    ToastUtil.show(App.getAppContext(), "修改成功");
                                    dismiss();
                                }

                                @Override
                                protected void onError(int code) {
                                    ToastUtil.show(App.getAppContext(), "修改失败");
                                    dismiss();
                                }
                            });
                } else {
                    ToastUtil.show(App.getAppContext(), "请输入大于6位的密码");
                }
            }
        });
    }

    public ChangePwdDialog setHintText() {
        String hint = "您将修改账号" + userId + "的密码";
        spannableString = new SpannableString(hint);
        //加红操作
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
        spannableString.setSpan(foregroundColorSpan, 6, 10, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        txtHint.setText(spannableString);
        return this;
    }

    public String getNewPwd() {
        return editNewPwd.getText().toString().trim();
    }

    public void show() {
        WindowManager.LayoutParams layoutParams=mDialog.getWindow().getAttributes();
        layoutParams.gravity=Gravity.CENTER;
        layoutParams.width=DensityUtil.dp2px(mContext,306);
        layoutParams.height=DensityUtil.dp2px(mContext,184);
        mDialog.getWindow().setAttributes(layoutParams);
        mDialog.show();
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public interface OnClickChangeistener {
        void OnClick();
    }

    public ChangePwdDialog setOnClickChangeListener(OnClickChangeistener listener) {
        this.listener = listener;
        return this;
    }

}
