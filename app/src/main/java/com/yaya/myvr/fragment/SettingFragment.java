package com.yaya.myvr.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yaya.myvr.R;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.app.VRApp;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.AppEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by admin on 2017/5/16.
 */

public class SettingFragment extends BaseFragment {
    @BindView(R.id.rl_clean)
    RelativeLayout rlClean;
    @BindView(R.id.rl_update)
    RelativeLayout rlUpdate;
    @BindView(R.id.btn_exit)
    Button btnExit;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView() {
        if (!ApiConst.IS_LOGIN) {
            btnExit.setVisibility(View.GONE);
        } else {
            btnExit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.rl_clean)
    public void onRlCleanClicked() {
    }

    @OnClick(R.id.rl_update)
    public void onRlUpdateClicked() {
    }

    @OnClick(R.id.btn_exit)
    public void onBtnExitClicked() {
        // 延时任务
        Subscription subscription = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        ApiConst.IS_LOGIN = false;
                        ApiConst.PHONE = "";
                        ApiConst.LOGIN_KEY = "";
                        VRApp.getAppInstance().getSpUtil().put("isLogin", false);
                        Toast.makeText(getContext().getApplicationContext(), "退出成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new AppEvent("exit", null));
                        getActivity().finish();
                    }
                });

        subscriptionList.add(subscription);
    }
}
