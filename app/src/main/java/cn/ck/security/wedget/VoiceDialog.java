package cn.ck.security.wedget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;
import cn.ck.security.R;
import cn.ck.security.event.MessageEvent;

/**
 * @author chengkun
 * @since 2019/1/23 20:06
 */
public class VoiceDialog {
    private Context mContext;
    private Dialog mDialog;
    private View mContentView;

    ImageView imageCancle;
    EditText editResult;
    Button btnSearch;
    WaveViewByBezier waveBezier;
    Button btnStart;
    Button btnStop;

    @SuppressLint("InflateParams")
    public VoiceDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext, R.style.dialog_voice);
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_voice, null);
        mDialog.setContentView(mContentView);
        mDialog.setCanceledOnTouchOutside(false);
        imageCancle = mContentView.findViewById(R.id.image_cancle);
        editResult = mContentView.findViewById(R.id.edit_result);
        btnSearch = mContentView.findViewById(R.id.btn_search);
        btnStart = mContentView.findViewById(R.id.btn_start);
        btnStop = mContentView.findViewById(R.id.btn_stop);
        waveBezier = mContentView.findViewById(R.id.wave_bezier);
        imageCancle.setOnClickListener(view -> dismiss());
        //EventBus
        EventBus.getDefault().register(this);
        Log.i("ck", "VoiceDialog: " + EventBus.getDefault().isRegistered(this));
    }


    @OnClick({R.id.image_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_cancle:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setResultText(MessageEvent messageEvent) {
        Log.i("ck", "接收：" + messageEvent.getMessage());
        editResult.setText(messageEvent.getMessage().trim());
    }

    public String getResultRext() {
        return editResult.getText().toString().trim();
    }

    public VoiceDialog setStartListener(View.OnClickListener listener) {
        if (listener != null) {
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                    waveBezier.startAnimation();
                }
            });
        }
        return this;
    }

    public VoiceDialog setStopListener(View.OnClickListener listener) {
        if (listener != null) {
            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                    waveBezier.stopAnimation();
                }
            });
        }
        return this;
    }

    public VoiceDialog setSearchListener(View.OnClickListener listener) {
        if (listener != null) {
            btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                    dismiss();
                }
            });
        }
        return this;
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        waveBezier.stopAnimation();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
