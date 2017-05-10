package com.yaya.myvr.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yaya.myvr.R;
import com.yaya.myvr.adapter.HistoryAdapter;
import com.yaya.myvr.adapter.SearchAdapter;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.api.ApiManager;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.bean.SearchInfo;
import com.yaya.myvr.dao.History;
import com.yaya.myvr.dao.History_Table;
import com.yaya.myvr.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends BaseActivity {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;

    private static final String TAG = SearchActivity.class.getSimpleName();
    private Map<String, String> map = new HashMap<>();
    private String searchData = null;
    private HistoryAdapter historyAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initData();
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvSearch.setLayoutManager(layoutManager);

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            // 设置键盘搜索键
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 为空显示历史
                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchData = etSearch.getText().toString().trim();
                // 为空显示历史
                if (TextUtils.isEmpty(searchData)) {
                    rvSearch.setAdapter(historyAdapter);
                } else {
                    requestData();
                }
            }
        });
    }


    private void initData() {
        map.putAll(ApiConst.BASE_MAP);
        map.put("cmd", "getVideoByCate");
        map.put("latestId", "0");
        map.put("limit", "6");

        List<History> historyList = SQLite.select()
                .from(History.class)
                .orderBy(History_Table.id, false)
                .queryList();

        Log.e(TAG, "historyList = " + historyList);
        historyAdapter = new HistoryAdapter(historyList, this);
        rvSearch.setAdapter(historyAdapter);
    }

    /**
     * 请求数据
     */
    private void requestData() {
        map.put("kw", searchData);
        Subscription subscription = ApiManager.getInstance().getApiService().getSearchInfo(map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchInfo>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted...");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "onError... e = " + e.getMessage());
                    }

                    @Override
                    public void onNext(SearchInfo searchInfo) {
                        LogUtils.e(TAG, "onNext... searchInfo = " + searchInfo);
                        bindSearchData(searchInfo);
                    }
                });

        subscriptionList.add(subscription);
    }


    private void bindSearchData(SearchInfo searchInfo) {
        if (searchInfo != null && searchInfo.getErrCode() == 0) {
            List<SearchInfo.DataBean> dataList = searchInfo.getData();
            SearchAdapter searchAdapter = new SearchAdapter(dataList, this);
            rvSearch.setAdapter(searchAdapter);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppEvent event) {
        switch (event.getMark()) {
            // 保存数据
            case "search_click":
                finish();
                if (TextUtils.isEmpty(searchData)) {
                    return;
                }

                // 刪除已有项
                SQLite.delete()
                        .from(History.class)
                        .where(History_Table.data.eq(searchData))
                        .query();

                History history = new History();
                history.data = searchData;
                history.save();
                break;
            // 读取数据
            case "history_click":
                String data = event.getData();
                etSearch.setText(data);
                etSearch.setSelection(data.length());
                break;
        }
    }


    @OnClick(R.id.btn_cancel)
    public void onViewClicked() {
        searchData = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(searchData)) {
            finish();
        } else {
            etSearch.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
