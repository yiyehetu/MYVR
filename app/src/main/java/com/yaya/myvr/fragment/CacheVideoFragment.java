package com.yaya.myvr.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yaya.myvr.R;
import com.yaya.myvr.adapter.CacheVideoAdapter;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.bean.CacheProgress;
import com.yaya.myvr.dao.Task;
import com.yaya.myvr.util.LogUtils;
import com.yaya.myvr.widget.cache.VideoCacheTask2;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

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
    private Map<String, Integer> positionMap = new HashMap<>();
    private List<Task> taskList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        layoutManager = new LinearLayoutManager(getContext());
        rvVideo.setLayoutManager(layoutManager);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        List<Task> tasks = SQLite.select()
                .from(Task.class)
                .queryList();
        pbProgress.setVisibility(View.GONE);

        if (tasks.size() > 0) {
            taskList.addAll(tasks);
            for (int i = 0; i < taskList.size(); i++) {
                positionMap.put(taskList.get(i).videoId, i);
            }

            cacheVideoAdapter = new CacheVideoAdapter(getContext(), taskList);
            rvVideo.setAdapter(cacheVideoAdapter);
        } else {
            llError.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppEvent event) {
        String mark = event.getMark();
        if (VideoCacheTask2.PROGRESS.equals(mark) || VideoCacheTask2.PAUSE.equals(mark) || VideoCacheTask2.START.equals(mark) || VideoCacheTask2.COMPLETED.equals(mark) || VideoCacheTask2.IDLE.equals(mark)) {
            CacheProgress data = (CacheProgress) event.getObj();
            String videoId = data.getVideoId();
            int progress = data.getProgress();
            int status = data.getStatus();
            String state = AppConst.DOWNLOAD_STATUS.get(status) + progress + "%";
            LogUtils.e(TAG, "state = " + state);

            int postion = positionMap.get(videoId);
            Task task = taskList.get(postion);
            task.status = status;
            task.progress = progress;
            cacheVideoAdapter.notifyItemChanged(postion, CacheVideoAdapter.PAYLOAD);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
