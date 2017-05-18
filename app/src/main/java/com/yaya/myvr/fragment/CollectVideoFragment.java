package com.yaya.myvr.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yaya.myvr.R;
import com.yaya.myvr.adapter.FavorVideoAdapter;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.dao.Favor;

import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/5/15.
 */

public class CollectVideoFragment extends BaseFragment {
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.rv_video)
    RecyclerView rvVideo;
    @BindView(R.id.ll_error)
    LinearLayout llError;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvVideo.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {
        List<Favor> favors = SQLite.select()
                .from(Favor.class)
                .queryList();

        pbProgress.setVisibility(View.GONE);
        if(favors.size() > 0){
            rvVideo.setAdapter(new FavorVideoAdapter(getContext(), favors));
        }else{
            llError.setVisibility(View.VISIBLE);
        }
    }

}
