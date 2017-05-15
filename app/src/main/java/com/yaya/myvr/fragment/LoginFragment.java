package com.yaya.myvr.fragment;

import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yaya.myvr.R;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.LoginInfo;
import com.yaya.myvr.util.EncryptUtils;
import com.yaya.myvr.util.LogUtils;

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

    @OnClick(R.id.btn_login)
    void login() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

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

        requestLogin(phone, password );
    }

    /**
     * 请求登陆
     *
     * @param phone
     * @param password
     */
    private void requestLogin(String phone, String password) {
        String deviceId = "Auv8WGLjpnBQQ6YoAZcg4hx8svqMpw0p60RFuqXrd93Y";
        password = EncryptUtils.encryptMD5ToString(password).toLowerCase();
        LogUtils.e(TAG, "password = " + password);

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
                    }

                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        LogUtils.e(TAG, "onNext... loginInfo = " + loginInfo);
                    }
                });

        subscriptionList.add(subscription);
    }

}
