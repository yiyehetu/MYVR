package com.yaya.myvr.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yaya.myvr.R;
import com.yaya.myvr.adapter.HistoryAdapter;
import com.yaya.myvr.base.BaseActivity;
import com.yaya.myvr.dao.History;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {
    private static final String TAG = SearchActivity.class.getSimpleName();

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvSearch.setLayoutManager(layoutManager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {

        for(int i = 0; i < 10; i++) {
            History history = new History();
            history.data = "哈哈" + i;
            history.save();
        }


        List<History> historyList = SQLite.select()
                .from(History.class)
                .queryList();

        Log.e(TAG, "historyList = " + historyList);
        HistoryAdapter historyAdapter = new HistoryAdapter(historyList, this);
        rvSearch.setAdapter(historyAdapter);
    }

    @OnClick(R.id.btn_cancel)
    public void onViewClicked() {
    }
}
