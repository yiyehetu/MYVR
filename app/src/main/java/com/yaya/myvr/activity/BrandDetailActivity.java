package com.yaya.myvr.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yaya.myvr.R;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.util.LogUtils;

import butterknife.BindView;

/**
 * 品牌详情页面
 */
public class BrandDetailActivity extends BaseActivity {
    @BindView(R.id.rv_brand)
    RecyclerView rvBrand;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_brand)
    RelativeLayout rlBrand;

    private static final String TAG = BrandDetailActivity.class.getSimpleName();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_brand_detail;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String brandId = getIntent().getStringExtra(AppConst.BRAND_ID);
        LogUtils.e(TAG, "brandId = " + brandId);

    }
}
