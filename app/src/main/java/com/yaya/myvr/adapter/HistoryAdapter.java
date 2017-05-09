package com.yaya.myvr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yaya.myvr.R;
import com.yaya.myvr.dao.History;

import java.util.List;

/**
 * Created by admin on 2017/5/10.
 * <p>
 * 搜索历史适配器
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {
    private static final int DEFAULT = 100;
    private static final int FOOTER = 101;
    private List<History> historyList;
    private Context context;
    private LayoutInflater layoutInflater;

    public HistoryAdapter(List<History> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FOOTER:
                View footerView = layoutInflater.inflate(R.layout.item_history_footer, parent, false);
                HistoryHolder footerHolder = new HistoryHolder(footerView);
                footerHolder.tvDelete = (TextView) footerView;
                return footerHolder;
            default:
                View defaultView = layoutInflater.inflate(R.layout.item_history_default, parent, false);
                HistoryHolder defaultHolder = new HistoryHolder(defaultView);
                defaultHolder.tvHistory = (TextView) defaultView.findViewById(R.id.tv_history);
                defaultHolder.ivDelete = (ImageView) defaultView.findViewById(R.id.iv_delete);
                return defaultHolder;
        }

    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        switch (getItemViewType(position)) {
            case FOOTER:
                bindFooter(holder);
                break;
            case DEFAULT:
                bindDefault(holder, position);
        }
    }

    private void bindFooter(HistoryHolder holder) {
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "delete all", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindDefault(HistoryHolder holder, int position) {
        History history = historyList.get(position);
        holder.tvHistory.setText(history.data);
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "delete item", Toast.LENGTH_SHORT).show();
            }
        });

        holder.tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == historyList.size()) {
            return FOOTER;
        } else {
            return DEFAULT;
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size() + 1;
    }

    static class HistoryHolder extends RecyclerView.ViewHolder {
        TextView tvHistory;
        ImageView ivDelete;
        TextView tvDelete;

        public HistoryHolder(View itemView) {
            super(itemView);
        }
    }

}
