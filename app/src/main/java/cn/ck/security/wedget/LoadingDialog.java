package cn.ck.security.wedget;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ck.security.R;


/**
 * 加载中......
 *
 * @author chengkun
 * @since 2018/11/15 23:52
 */
public class LoadingDialog {
    private Context mContext;
    private Dialog mDialog;
    private View mContentView;
    private ImageView mLoadingImage;
    private TextView mLoadingMsg;

    public LoadingDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext, R.style.dialog_loading);
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
        mLoadingImage = (ImageView) mContentView.findViewById(R.id.img_loading);
        mLoadingMsg = (TextView) mContentView.findViewById(R.id.txt_loading_msg);
        mDialog.setContentView(mContentView);
        mDialog.setCanceledOnTouchOutside(false);
    }

    public LoadingDialog setLoadingMsg(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            mLoadingMsg.setText(msg);
        }
        return this;
    }

    public LoadingDialog setLoadingMsg(@StringRes final int resId) {
        mLoadingMsg.setText(resId);

        return this;
    }

    public void show() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mLoadingImage, "rotation", 0, 360);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1500);
        animator.setRepeatCount(-1);
        animator.start();
        mDialog.show();
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
