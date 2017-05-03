package com.yaya.myvr.fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by admin on 2017/5/3.
 */

public class LoopFragment extends BaseFragment {
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_loop;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        Bundle args = getArguments();
        String img = args.getString(AppConst.LOOP_IMG);
        String title = args.getString(AppConst.LOOP_TITLE);
        id = args.getString(AppConst.LOOP_ID);

        Glide.with(getContext())
                .load(img)
                .centerCrop()
                .crossFade()
                .into(ivImg);
        tvTitle.setText(title);
    }
}
