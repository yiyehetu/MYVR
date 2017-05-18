package com.yaya.myvr.fragment;

import android.app.AlertDialog;
import android.content.Context;
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

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yaya.myvr.R;
import com.yaya.myvr.activity.MineActivity;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.app.VRApp;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.bean.LoginInfo;
import com.yaya.myvr.dao.Personal;
import com.yaya.myvr.util.ConvertUtils;
import com.yaya.myvr.util.EncryptUtils;
import com.yaya.myvr.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by admin on 2017/5/15.
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_see)
    CheckBox cbSee;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private Map<String, String> map = new HashMap<>();
    private AlertDialog alertDialog;
    private String phone;
    private String password;
    private String deviceId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
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
        map.putAll(ApiConst.BASE_MAP);
        map.put("cmd", "userLogin");
    }

    @OnClick(R.id.tv_forget)
    void forget(){
        MineActivity.start(getContext(), AppConst.RESET);
        getActivity().finish();
    }

    @OnClick(R.id.btn_login)
    void login() {
        hideInput();

        deviceId = "Auv8WGLjpnBQQ6YoAZcg4hx8svqMpw0p60RFuqXrd93Y";
        phone = etPhone.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        // 输入校验
        if (phone.length() == 0 && password.length() == 0) {
            Toast.makeText(getContext(), "手机号码和密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (phone.length() == 0) {
            Toast.makeText(getContext(), "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() == 0) {
            Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (phone.length() != 11) {
            Toast.makeText(getContext(), "手机号码有误", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 8) {
            Toast.makeText(getContext(), "密码长度不足8位", Toast.LENGTH_SHORT).show();
            return;
        }

        password = EncryptUtils.encryptMD5ToString(password).toLowerCase();
        showAlert();
        requestLogin();
    }

    /**
     * 请求登陆
     *
     */
    private void requestLogin() {
        Subscription subscription = ApiManager.getInstance().getApiService().getLoginInfo(map, deviceId, password, phone)
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
                    public void onNext(LoginInfo loginInfo) {
                        LogUtils.e(TAG, "onNext... loginInfo = " + loginInfo);
                        hideAlert();
                        bindLoginInfo(loginInfo);
                    }
                });

        subscriptionList.add(subscription);
    }

    private void bindLoginInfo(LoginInfo loginInfo) {
        if (loginInfo != null && loginInfo.getErrCode() == 0) {
            ApiConst.LOGIN_KEY = loginInfo.getData().getLoginKey();
            ApiConst.IS_LOGIN = true;
            ApiConst.PHONE = phone;
            // 写入数据库
            writeData();
            // 记录登陆状态
            VRApp.getAppInstance().getSpUtil().put("isLogin", true);

            EventBus.getDefault().post(new AppEvent("login_success", null));
            getActivity().finish();
        } else {
            // 登陆失败
            Toast.makeText(getContext(), "登陆失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeData() {
        // 删除先前记录
        SQLite.delete()
                .from(Personal.class)
                .query();

        Personal personal = new Personal();
        personal.deviceId = deviceId;
        personal.phone = phone;
        personal.password = password;
        personal.save();
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
    private void hideInput(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
    }

}
