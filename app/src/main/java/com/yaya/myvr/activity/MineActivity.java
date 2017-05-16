package com.yaya.myvr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yaya.myvr.R;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.fragment.CacheVideoFragment;
import com.yaya.myvr.fragment.CollectVideoFragment;
import com.yaya.myvr.fragment.LocalVideoFragment;
import com.yaya.myvr.fragment.LoginFragment;
import com.yaya.myvr.fragment.RegisterFragment;
import com.yaya.myvr.fragment.SettingFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的页面相关
 */
public class MineActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    private static final String POSITON = "position";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int position = getIntent().getIntExtra(POSITON, -1);
        if(position == -1){
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content, getFragment(position)).commit();

    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    /**
     * 获得对应位置Fragment
     *
     * @param position
     * @return
     */
    private BaseFragment getFragment(int position) {
        BaseFragment fragment = null;
        switch (position) {
            case AppConst.LOCAL_VIDEO:
                tvTitle.setText("本地视频");
                fragment = new LocalVideoFragment();
                break;
            case AppConst.COLLECT_VIDEO:
                tvTitle.setText("我的收藏");
                fragment = new CollectVideoFragment();
                break;
            case AppConst.CACHE_VIDEO:
                tvTitle.setText("我的缓存");
                fragment = new CacheVideoFragment();
                break;
            case AppConst.LOGIN:
                tvTitle.setText("登陆");
                tvRegister.setVisibility(View.VISIBLE);
                fragment = new LoginFragment();
                break;
            case AppConst.REGISTER:
                tvTitle.setText("注册");
                fragment = new RegisterFragment();
                break;
            case AppConst.SETTING:
                tvTitle.setText("设置");
                fragment = new SettingFragment();
                break;
        }

        return fragment;
    }

    /**
     * 统一启动方法
     * @param context
     * @param position
     */
    public static void start(Context context, int position) {
        Intent intent = new Intent(context, MineActivity.class);
        intent.putExtra(POSITON, position);
        context.startActivity(intent);
    }

    @OnClick(R.id.tv_register)
    void clickRegiser(){
        start(this, AppConst.REGISTER);
        finish();
    }

}
