package com.yaya.myvr.fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yaya.myvr.R;
import com.yaya.myvr.activity.MineActivity;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.base.BaseFragment;
import com.yaya.myvr.util.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    Unbinder unbinder;

    private static final String TAG = MineFragment.class.getSimpleName();
    private MediaLoadTask loadTask;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        LogUtils.e("MineFragment init View...");
    }

    @Override
    protected void initData() {
        LogUtils.e("MineFragment init Data...");
    }

    @Override
    public void onStart() {
        super.onStart();
        startTask();
    }

    @Override
    public void onStop() {
        super.onStop();
        clearTask();
    }

    private void startTask() {
        if (loadTask != null) {
            return;
        }
        loadTask = new MediaLoadTask();
        loadTask.execute();
    }

    private void clearTask() {
        if (loadTask != null && !loadTask.isCancelled()) {
            loadTask.cancel(true);
            loadTask = null;
        }
    }


    // 检索本地视频
    class MediaLoadTask extends AsyncTask<Void, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvLocal.setText("...");
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
    void clickLogin(){
        MineActivity.start(getContext(), AppConst.LOGIN);
    }

}
