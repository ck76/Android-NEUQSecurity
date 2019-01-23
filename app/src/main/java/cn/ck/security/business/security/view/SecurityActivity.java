package cn.ck.security.business.security.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yanzhenjie.permission.Action;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.ck.security.App;
import cn.ck.security.R;
import cn.ck.security.base.mvp.view.BasePresenterActivity;
import cn.ck.security.business.account.view.LoginActivity;
import cn.ck.security.business.security.Constans;
import cn.ck.security.business.security.contract.SecurityContract;
import cn.ck.security.business.security.model.Car;
import cn.ck.security.business.security.presenter.SecurityPresenter;
import cn.ck.security.common.CacheKey;
import cn.ck.security.common.CommonConstans;
import cn.ck.security.event.MessageEvent;
import cn.ck.security.network.NetworkFactory;
import cn.ck.security.network.response.ApiCallBack;
import cn.ck.security.network.response.ApiResponse;
import cn.ck.security.network.services.ApiService;
import cn.ck.security.utils.CacheUtil;
import cn.ck.security.utils.DialogUtil;
import cn.ck.security.utils.NetworkUtils;
import cn.ck.security.utils.ToastUtil;
import cn.ck.security.voice.mini.AutoCheck;
import cn.ck.security.wedget.VoiceDialog;

public class SecurityActivity extends BasePresenterActivity<SecurityContract.SecurityPresenter>
        implements SecurityContract.SecurityView, EventListener {

    public static final int REQUEST_CODE = 200;

    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.image_scan)
    ImageView imageScan;
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.image_voice)
    ImageView imageVoice;
    @BindView(R.id.txt_logout)
    TextView txtLogout;
    @BindView(R.id.txt_result)
    TextView txtResult;

    private String mScanResult;
    private String mType;
    private String mResult;

    //语音识别
    private EventManager asr;
    private String logTxt;
    private VoiceDialog mVoiceDialog;

    private void transform() {
        String[] resultArray = mScanResult.split(CommonConstans.SPLITE);
        if (resultArray.length >= 2) {
            mType = resultArray[0];
            mResult = resultArray[1];
            excute();
        } else {
            ToastUtil.show(App.getAppContext(), "请扫描出入证上的二维码");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_security;
    }

    @Override
    protected SecurityContract.SecurityPresenter getPresenter() {
        return new SecurityPresenter(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    protected void initView() {
        initVoice();
        checkPermission(Constans.permissions, new Action() {
            @Override
            public void onAction(List<String> permissions) {

            }
        }, new Action() {
            @Override
            public void onAction(List<String> permissions) {
                ToastUtil.show(App.getAppContext(), "请授予权限，以防影响正常使用");
            }
        });
    }

    private void initVoice() {
        // 基于sdk集成1.1 初始化EventManager对象
        asr = EventManagerFactory.create(this, "asr");
        //  EventListener 中 onEvent方法
        asr.registerListener(this);
    }

    @OnClick({R.id.ll_search, R.id.image_scan, R.id.btn_scan, R.id.btn_search, R.id.image_voice,
            R.id.txt_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                startActivity(SearchResultOneActivity.class);
                break;
            case R.id.image_scan:
                startActivityForResult(ScanActivity.class, REQUEST_CODE);
                break;
            case R.id.btn_scan:
                startActivityForResult(ScanActivity.class, REQUEST_CODE);
                break;
            case R.id.btn_search:
                startActivity(SearchResultOneActivity.class);
                break;
            case R.id.image_voice:
                startVoice();
                break;
            case R.id.txt_logout:
                new DialogUtil.QuickDialog(this).setClickListener(() -> {
                    CacheUtil.put(CacheKey.TOKEN, "");
                    finish();
                    startActivity(LoginActivity.class);
                }).showDialog("确认退出登录？");
                break;
            default:
                break;
        }
    }


    private void excute() {
        switch (mType) {
            case CommonConstans
                    .TYPE_CAR:
                searchCar();
                break;
            default:
                ToastUtil.show(App.getAppContext(), "请扫描出入证上的二维码");
                break;
        }

    }


    private void searchCar() {
        showLoading();
        NetworkFactory.getInstance()
                .creatService(ApiService.class)
                .searchByNum(mResult)
                .enqueue(new ApiCallBack<List<Car>>() {
                    @Override
                    protected void onDataBack(ApiResponse<List<Car>> response) {
                        closeLoading();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constans.CAR_INFO, response.getData().get(0));
                        startActivity(SearchResultTwoActivity.class, bundle);
                    }

                    @Override
                    protected void onError(int code) {
                        closeLoading();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    mScanResult = bundle.getString(CodeUtils.RESULT_STRING);
                    transform();
                    Toast.makeText(this, "解析结果:" + mScanResult, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private void startVoice() {
        if (!NetworkUtils.isConnected()) {
            ToastUtil.show(App.getAppContext(), "请连接网路");
            return;
        }
        checkPermission(Constans.permissions, new Action() {
            @Override
            public void onAction(List<String> permissions) {
                mVoiceDialog = new VoiceDialog(mContext);
                mVoiceDialog
                        .setStartListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                start();
                            }
                        })
                        .setStopListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                stop();
                            }
                        })
                        .setSearchListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!TextUtils.isEmpty(mVoiceDialog.getResultRext())) {
                                    SearchResultOneActivity.startActivity(mContext, mVoiceDialog.getResultRext());
                                } else {
                                    ToastUtil.show(App.getAppContext(), "内容不可为空");
                                }
                            }
                        })
                        .show();
            }
        }, new Action() {
            @Override
            public void onAction(List<String> permissions) {
                ToastUtil.show(App.getAppContext(), "请开启语音权限");
            }
        });


    }

    /**
     * 开始识别
     */
    @SuppressLint("HandlerLeak")
    private void start() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START;
        // 基于SDK集成2.1 设置识别参数
        //params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // params.put(SpeechConstant.NLU, "enable");
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        (new AutoCheck(getApplicationContext(), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage();
                    }
                }
            }
        }, false)).checkAsr(params);
        String json = new JSONObject(params).toString();
        asr.send(event, json, null, 0, 0);
    }

    /**
     * 点击停止按钮
     */
    private void stop() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }

    /**
     * 语音回调事件统一由
     * public void onEvent(String name, String params, byte[] data, int offset, int length)
     * 该方法回调 其中name是回调事件， params是回调参数。
     * （data，offset，length）缓存临时数据，三者一起，生效部分为 data[offset] 开始，长度为length。
     */
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        logTxt = "";
        JsonObject result = null;

        if (params != null && !params.isEmpty()) {
            result = new JsonParser().parse(params).getAsJsonObject();
        }
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
//            if (params != null && params.contains("\"nlu_result\"")) {
//                if (length > 0 && data.length > 0) {
//                    logTxt += ", 语义解析结果：" + new String(data, offset, length);
//                }
//            }
            if (!TextUtils.isEmpty(result.get("best_result").getAsString())) {
                logTxt += result.get("best_result").getAsString();
            }

        }
        txtResult.setText(logTxt);
        EventBus.getDefault().post(new MessageEvent(logTxt));
    }

    @Override
    protected void onPause() {
        super.onPause();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        asr.unregisterListener(this);
        mVoiceDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        App.getInstance().exitAppWithTwiceClick();
    }

}
