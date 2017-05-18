package com.yaya.myvr.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yaya.myvr.R;
import com.yaya.myvr.activity.MineActivity;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.LoginInfo;
import com.yaya.myvr.bean.SmsInfo;
import com.yaya.myvr.util.ConvertUtils;
import com.yaya.myvr.util.EncryptUtils;
import com.yaya.myvr.util.LogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by admin on 2017/5/15.
 */

public class RegisterFragment extends BaseFragment {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_sms)
    EditText etSms;
    @BindView(R.id.tv_sms)
    TextView tvSms;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_see)
    CheckBox cbSee;
    @BindView(R.id.btn_register)
    Button btnRegister;

    private static final String POSITON = "position";
    private AlertDialog alertDialog;
    private String phone;
    private String sms;
    private String password;
    private Subscription countSubscription;
    private Map<String, String> smsMap = new HashMap<>();
    private Map<String, String> registerMap = new HashMap<>();


    /**
     * 静态方法获得实例
     * 区分注册与重置密码
     * @param position
     * @return
     */
    public static RegisterFragment getInstance(int position){
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putInt(POSITON, position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initView() {
        cbSee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                // 光标跟随
                int length = etPassword.getText().length();
                etPassword.setSelection(length);
            }
        });
    }

    @Override
    protected void initData() {
        smsMap.putAll(ApiConst.BASE_MAP);
        registerMap.putAll(ApiConst.BASE_MAP);

        int position = getArguments().getInt(POSITON);
        switch (position) {
            case AppConst.REGISTER :
                smsMap.put("cmd", "getRegSmsCode");
                registerMap.put("cmd", "userReg");
                btnRegister.setText("注册");
                break;
            case AppConst.RESET :
                smsMap.put("cmd", "getResetPassSmsCode");
                registerMap.put("cmd", "resetUserPass");
                btnRegister.setText("重置");
                break;
        }

    }

    @OnClick(R.id.tv_sms)
    public void onTvSmsClicked() {
        hideInput();
        if (countSubscription != null) {
            return;
        }

        if (checkPhone()) {
            requestSmsData();
        }
    }

    private boolean checkPhone() {
        phone = etPhone.getText().toString().trim();
        if (phone.length() == 0) {
            Toast.makeText(getContext().getApplicationContext(), "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone.length() != 11) {
            Toast.makeText(getContext().getApplicationContext(), "手机号码有误", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @OnClick(R.id.btn_register)
    public void onBtnRegisterClicked() {
        hideInput();

        if (!checkPhone()) {
            return;
        }

        sms = etSms.getText().toString().trim();
        if (sms.length() == 0) {
            Toast.makeText(getContext().getApplicationContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        password = etPassword.getText().toString().trim();
        if (password.length() == 0) {
            Toast.makeText(getContext().getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            Toast.makeText(getContext().getApplicationContext(), "密码长度不足8位", Toast.LENGTH_SHORT).show();
            return;
        }

        password = EncryptUtils.encryptMD5ToString(password).toLowerCase();
        showAlert();
        requestRegister();
    }


    /**
     * 请求注册
     */
    private void requestRegister() {
        Subscription subscription = ApiManager.getInstance().getApiService().getRegisterInfo(registerMap, password, sms, phone)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                        hideAlert();
                    }

                    @Override
                    public void onNext(LoginInfo registerInfo) {
                        LogUtils.e(TAG, "onNext... registerInfo = " + registerInfo);
                        hideAlert();
                        bindRegisterInfo(registerInfo);
                    }
                });

        subscriptionList.add(subscription);
    }

    /**
     * 绑定注册信息
     *
     * @param registerInfo
     */
    private void bindRegisterInfo(LoginInfo registerInfo) {
        if (registerInfo != null) {
            if (registerInfo.getErrCode() == 0) {
                Toast.makeText(getContext().getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                // 跳转登陆
                MineActivity.start(getContext(), AppConst.LOGIN);
                getActivity().finish();
            } else {
                Toast.makeText(getContext().getApplicationContext(), registerInfo.getErrMsg(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 开启计时任务
     */
    private void startTimer() {
        countSubscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        int time = (int) (59 - aLong);
                        if (time < 0) {
                            tvSms.setSelected(false);
                            tvSms.setText("获取验证码");
                            clearTimer();
                        } else {
                            tvSms.setSelected(true);
                            tvSms.setText(time + "s后重试");
                        }
                    }
                });

    }

    /**
     * 请求验证码
     */
    private void requestSmsData() {
        smsMap.put("phoneNumber", phone);
        Subscription subscription = ApiManager.getInstance().getApiService().getSmsInfo(smsMap)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SmsInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                    }

                    @Override
                    public void onNext(SmsInfo smsInfo) {
                        LogUtils.e(TAG, "onNext... smsInfo = " + smsInfo);
                        bindSmsInfo(smsInfo);
                    }
                });

        subscriptionList.add(subscription);
    }

    /**
     * 绑定注册验证码信息
     *
     * @param smsInfo
     */
    private void bindSmsInfo(SmsInfo smsInfo) {
        if (smsInfo != null) {
            if (smsInfo.getErrCode() != 0) {
                Toast.makeText(getContext().getApplicationContext(), smsInfo.getErrMsg(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext().getApplicationContext(), "请稍候...", Toast.LENGTH_SHORT).show();
                startTimer();
            }
        }
    }

    private void clearTimer() {
        if (countSubscription == null) {
            return;
        }

        if (!countSubscription.isUnsubscribed()) {
            countSubscription.unsubscribe();
            countSubscription = null;
        }
    }

    private void showAlert() {
        if (alertDialog != null) {
            return;
        }

        View alertView = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_progress, null, false);
        alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.show();
        alertDialog.setContentView(alertView);
        Window dialogWindow = alertDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ConvertUtils.dp2px(getContext(), 100);
        lp.height = ConvertUtils.dp2px(getContext(), 100);
        dialogWindow.setAttributes(lp);

    }

    private void hideAlert() {
        if (alertDialog == null) {
            return;
        }
        alertDialog.hide();
        alertDialog = null;
    }

    // 隐藏键盘
    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearTimer();
    }
}
