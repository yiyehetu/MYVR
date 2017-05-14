package com.yaya.myvr.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.yaya.myvr.R;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.fragment.CacheVideoFragment;
import com.yaya.myvr.fragment.CollectVideoFragment;
import com.yaya.myvr.fragment.LocalVideoFragment;

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

    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    /**
     * 获得对应位置Fragment
     * @param position
     * @return
     */
    private BaseFragment getFragment(int position) {
        BaseFragment fragment = null;
        switch (position) {
            case AppConst.LOCAL_VIDEO:
                fragment = new LocalVideoFragment();
                break;
            case AppConst.COLLECT_VIDEO:
                fragment = new CollectVideoFragment();
                break;
            case AppConst.CACHE_VIDEO:
                fragment = new CacheVideoFragment();
                break;
        }

        return fragment;
    }
}
