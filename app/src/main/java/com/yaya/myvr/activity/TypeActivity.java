package com.yaya.myvr.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.adapter.TypePagerAdapter;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.app.VRApp;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.bean.DictInfo;
import com.yaya.myvr.util.ConvertUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * 分类展示页面
 */
public class TypeActivity extends BaseActivity {
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tl_type)
    TabLayout tlType;
    @BindView(R.id.vp_type)
    ViewPager vpType;
    @BindView(R.id.abl_type)
    AppBarLayout ablType;
    @BindView(R.id.iv_pic)
    ImageView ivPic;

    private static final String TAG = TypeActivity.class.getSimpleName();
    private static final float BASE_Y = ConvertUtils.dp2px(VRApp.getAppInstance().getApplicationContext(), 130);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_type;
    }

    @Override
    protected void initView() {
        ablType.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int currY = Math.abs(verticalOffset);
                int alpha = (int) (currY / BASE_Y * 255);
                llType.setBackgroundColor(Color.argb(alpha, 47, 79, 79));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(AppConst.TYPE_POSITION, -1);
        if (position == -1) {
            return;
        }
        if (AppConst.DICT_CATEGORY.isEmpty()) {
            return;
        }

        DictInfo.DataBean.CategoryBean category = AppConst.DICT_CATEGORY.get(position);
        tvTitle.setText(category.getName());
        String tags = category.getTags();
        // 需要转义
        String[] titles = tags.split("\\|");
        int length = titles.length + 1;
        String[] newTitles = new String[length];
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                newTitles[i] = "全部";
            } else {
                newTitles[i] = titles[i - 1];
            }
        }

        TypePagerAdapter typePagerAdapter = new TypePagerAdapter(getSupportFragmentManager(), newTitles, category.getCate());
        vpType.setAdapter(typePagerAdapter);
        tlType.setupWithViewPager(vpType);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppEvent event) {
        if ("type_first_pic".equals(event.getMark())) {
            Glide.with(this)
                    .load(event.getData())
                    .placeholder(R.drawable.icon_black_alpha_top_mask)
                    .centerCrop()
                    .crossFade()
                    .into(ivPic);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
