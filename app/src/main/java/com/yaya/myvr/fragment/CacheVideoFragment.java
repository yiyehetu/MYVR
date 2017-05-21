package com.yaya.myvr.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yaya.myvr.R;
import com.yaya.myvr.adapter.CacheVideoAdapter;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.dao.Task;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/5/15.
 */

public class CacheVideoFragment extends BaseFragment {
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.rv_video)
    RecyclerView rvVideo;
    @BindView(R.id.ll_error)
    LinearLayout llError;
    private LinearLayoutManager layoutManager;
    private CacheVideoAdapter cacheVideoAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        layoutManager = new LinearLayoutManager(getContext());
        rvVideo.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData() {
        List<Task> tasks = SQLite.select()
                .from(Task.class)
                .queryList();
        pbProgress.setVisibility(View.GONE);

//        final Map<String, Integer> map = new HashMap<>();
//        for (int index = 0; index < tasks.size(); index++) {
//            map.put(tasks.get(index).videoId, index);
//        }

        if (tasks.size() > 0) {
            cacheVideoAdapter = new CacheVideoAdapter(getContext(), tasks);
            rvVideo.setAdapter(cacheVideoAdapter);

            EventBus.getDefault().register(cacheVideoAdapter);
        } else {
            llError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(cacheVideoAdapter != null){
            EventBus.getDefault().unregister(cacheVideoAdapter);
        }
    }
}
