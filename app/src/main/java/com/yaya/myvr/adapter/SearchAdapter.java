package com.yaya.myvr.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yaya.myvr.R;
import com.yaya.myvr.activity.VideoInfoActivity;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.bean.SearchInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by admin on 2017/5/10.
 * <p>
 * 搜索适配器
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> {
    private List<SearchInfo.DataBean> dataList;
    private Context context;
    private LayoutInflater layoutInflater;

    public SearchAdapter(List<SearchInfo.DataBean> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View defaultView = layoutInflater.inflate(R.layout.item_search_default, parent, false);
        Holder defaultHolder = new Holder(defaultView);
        defaultHolder.tvSearch = (TextView) defaultView.findViewById(R.id.tv_search);
        return defaultHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final SearchInfo.DataBean bean = dataList.get(position);
        holder.tvSearch.setText(bean.getTitle());
        holder.tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoInfoActivity.class);
                intent.putExtra(AppConst.VIDEO_ID, bean.getId());
                context.startActivity(intent);

                EventBus.getDefault().post(new AppEvent("search_click", null));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tvSearch;

        public Holder(View itemView) {
            super(itemView);
        }
    }

}
