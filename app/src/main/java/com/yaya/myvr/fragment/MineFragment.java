package com.yaya.myvr.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yaya.myvr.R;
import com.yaya.myvr.activity.MineActivity;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.dao.Favor;
import com.yaya.myvr.dao.Task;
import com.yaya.myvr.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2017/5/2.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.iv_msg)
    ImageView ivMsg;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_local)
    TextView tvLocal;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.rl_history)
    RelativeLayout rlHistory;
    @BindView(R.id.rl_download)
    RelativeLayout rlDownload;
    @BindView(R.id.rl_suggest)
    RelativeLayout rlSuggest;
    @BindView(R.id.rl_about)
    RelativeLayout rlAbout;

    private static final String TAG = MineFragment.class.getSimpleName();
    private MediaLocalTask localTask;
    private MediaCacheTask cacheTask;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        LogUtils.e("MineFragment init View...");
        EventBus.getDefault().register(this);

        if (ApiConst.IS_LOGIN) {
            tvLogin.setText(ApiConst.PHONE);
        } else {
            tvLogin.setText("立即登陆");
        }
    }

    @Override
    protected void initData() {
        LogUtils.e("MineFragment init Data...");
        startTask();
        updateFavor();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void startTask() {
        localTask = new MediaLocalTask();
        localTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        cacheTask = new MediaCacheTask();
        cacheTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void clearTask(AsyncTask task) {
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
            task = null;
        }
    }


    // 检索本地视频
    class MediaLocalTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvLocal.setText("0");
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return getLoadMedia();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            tvLocal.setText(integer + "");
        }
    }

    // 检索本地缓存
    class MediaCacheTask extends AsyncTask<Void, Integer, Integer>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvCache.setText("0");
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return getCacheSize();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            tvCache.setText(integer + "");
        }
    }


    private int getLoadMedia() {
        int count = 0;
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        try {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return count;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppEvent event) {
        if ("login_success".equals(event.getMark())) {
            tvLogin.setText(ApiConst.PHONE);
        } else if ("exit".equals(event.getMark())) {
            tvLogin.setText("立即登陆");
        } else if("update_favor".equals(event.getMark())){
            updateFavor();
        } else if("update_cache".equals(event.getMark())){
            updateCache();
        }

    }

    /**
     * 更新收藏
     */
    private void updateFavor() {
        int count = SQLite.select()
                .from(Favor.class)
                .queryList()
                .size();
        tvCollect.setText(count + "");
    }

    /**
     * 更新缓存
     */
    private void updateCache() {
        int count = SQLite.select()
                .from(Task.class)
                .queryList()
                .size();
        tvCache.setText(count + "");
    }

    /**
     * 查询缓存
     */
    private int getCacheSize(){
        int sum = 0;
        List<Task> taskList = SQLite.select()
                .from(Task.class)
                .queryList();

        // 本地验证
        for(int i = 0; i < taskList.size(); i++) {
            final String cacheDir = new StringBuilder().append(FileDownloadUtils.getDefaultSaveRootPath())
                    .append(File.separator)
                    .append(ApiConst.VIDEO_CACHE)
                    .append(File.separator)
                    .append(taskList.get(i).videoId)
                    .toString();
            File file = new File(cacheDir);
            if(file.exists()){
                sum++;
            }else{
                taskList.get(i).delete();
            }
        }

        return sum;
    }

    @OnClick(R.id.ll_local)
    void clickLocalVideo() {
        MineActivity.start(getContext(), AppConst.LOCAL_VIDEO);
    }

    @OnClick(R.id.ll_collect)
    void clickCollectVideo() {
        MineActivity.start(getContext(), AppConst.COLLECT_VIDEO);
    }

    @OnClick(R.id.ll_cache)
    void clickCacheVideo() {
        MineActivity.start(getContext(), AppConst.CACHE_VIDEO);
    }

    @OnClick(R.id.ll_login)
    void clickLogin() {
        if (ApiConst.IS_LOGIN) {
            MineActivity.start(getContext(), AppConst.SETTING);
        } else {
            MineActivity.start(getContext(), AppConst.LOGIN);
        }
    }

    @OnClick(R.id.iv_setting)
    void clickSetting(){
        MineActivity.start(getContext(), AppConst.SETTING);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearTask(localTask);
        clearTask(cacheTask);
        EventBus.getDefault().unregister(this);
    }
}
