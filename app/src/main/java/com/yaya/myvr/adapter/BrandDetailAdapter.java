package com.yaya.myvr.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yaya.myvr.R;
import com.yaya.myvr.activity.MineActivity;
import com.yaya.myvr.activity.VideoInfoActivity;
import com.yaya.myvr.api.ApiConst;
import com.yaya.myvr.app.AppConst;
import com.yaya.myvr.bean.AppEvent;
import com.yaya.myvr.bean.BrandBottomInfo;
import com.yaya.myvr.bean.BrandTopInfo;
import com.yaya.myvr.util.ConvertUtils;
import com.yaya.myvr.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/6.
 */

public class BrandDetailAdapter extends RecyclerView.Adapter<BrandDetailAdapter.BrandDetailHolder> {
    private static final String TAG = BrandDetailAdapter.class.getSimpleName();
    private static final int HEADER = 100;
    private static final int DEFAULT = 101;
    private static final String ATTENTIONED = "已关注";
    private static final String ADD_ATTENTION = "+ 关注";
    private BrandTopInfo.DataBean topData;
    private List<BrandBottomInfo.DataBean> bottomData;
    private Context context;
    private LayoutInflater layoutInflater;
    private TextView textView;

    public BrandDetailAdapter(BrandTopInfo.DataBean topData, List<BrandBottomInfo.DataBean> bottomData, Context context) {
        this.topData = topData;
        this.bottomData = bottomData;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public BrandDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                LogUtils.e(TAG, "onCreateViewHolder..." + viewType);
                View headerView = layoutInflater.inflate(R.layout.item_branddetail_header, parent, false);
                BrandDetailHolder headerHolder = new BrandDetailHolder(headerView);
                headerHolder.ivBrandimg = (ImageView) headerView.findViewById(R.id.iv_brandimg);
                headerHolder.tvFocus = (TextView) headerView.findViewById(R.id.tv_focus);
                headerHolder.tvProfile = (TextView) headerView.findViewById(R.id.tv_profile);
                headerHolder.llTag = (LinearLayout) headerView.findViewById(R.id.ll_tag);
                headerHolder.tvAttention = (TextView) headerView.findViewById(R.id.tv_attention);

                // 添加标签View
                String tags = topData.getTags();
                if (!TextUtils.isEmpty(tags)) {
                    List<String> tagList = getTagArray(tags);
                    for (int i = 0; i < tagList.size(); i++) {
                        TextView textView = (TextView) layoutInflater.inflate(R.layout.item_branddetail_tag, headerHolder.llTag, false);
                        textView.setText(tagList.get(i));
                        headerHolder.llTag.addView(textView);

                        if (i != 0) {
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
                            params.leftMargin = ConvertUtils.dp2px(context, 10);
                            textView.setLayoutParams(params);
                        }
                    }
                }
                return headerHolder;
            default:
                LogUtils.e(TAG, "onCreateViewHolder..." + viewType);
                View defaultView = layoutInflater.inflate(R.layout.item_branddetail_default, parent, false);
                BrandDetailHolder defaultHolder = new BrandDetailHolder(defaultView);
                defaultHolder.ivPic = (ImageView) defaultView.findViewById(R.id.iv_pic);
                defaultHolder.tvMark = (TextView) defaultView.findViewById(R.id.tv_mark);
                defaultHolder.tvTitle = (TextView) defaultView.findViewById(R.id.tv_title);
                defaultHolder.tvActor = (TextView) defaultView.findViewById(R.id.tv_actor);
                defaultHolder.tvContent = (TextView) defaultView.findViewById(R.id.tv_content);
                defaultHolder.tvScore = (TextView) defaultView.findViewById(R.id.tv_score);
                return defaultHolder;
        }
    }

    @Override
    public void onBindViewHolder(BrandDetailHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HEADER:
                bindHeader(holder);
                break;
            default:
                bindDefault(holder, position);
                break;
        }
    }

    /**
     * 绑定头部数据
     *
     * @param holder
     */
    private void bindHeader(final BrandDetailHolder holder) {
        Glide.with(context)
                .load(topData.getBrandImg())
                .placeholder(R.drawable.icon_black_alpha_top_mask)
                .centerCrop()
                .crossFade()
                .into(holder.ivBrandimg);
        holder.tvProfile.setText(topData.getProfile());
        holder.tvAttention.setText("粉丝:" + topData.getAttention());

        if(topData.isIsAttention()){
            holder.tvFocus.setText(ATTENTIONED);
        }else{
            holder.tvFocus.setText(ADD_ATTENTION);
        }

        holder.tvFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ATTENTIONED.equals(holder.tvFocus.getText())){
//                    holder.tvFocus.setText(ADD_ATTENTION);
                    textView = holder.tvFocus;
                    EventBus.getDefault().post(new AppEvent("attention", "removeBrandAttention"));
                }else{
                    // 登陆判断
                    if(ApiConst.IS_LOGIN){
//                        holder.tvFocus.setText(ATTENTIONED);
                        textView = holder.tvFocus;
                        EventBus.getDefault().post(new AppEvent("attention", "addBrandAttention"));
                    }else{
                        // 跳转登陆
                        MineActivity.start(context, AppConst.LOGIN);
                    }

                }
            }
        });
    }

    private List<String> getTagArray(String tags) {
        List<String> tagList = new ArrayList<>();

        int index = -1;
        while ((index = tags.indexOf("]")) != -1) {
            tagList.add(tags.substring(1, index));
            tags = tags.substring(index + 1);
        }

        return tagList;
    }

    /**
     * 绑定底部数据
     *
     * @param holder
     * @param position
     */
    private void bindDefault(BrandDetailHolder holder, int position) {
        final BrandBottomInfo.DataBean bean = bottomData.get(position - 1);
        Glide.with(context)
                .load(bean.getPicture())
                .placeholder(R.drawable.icon_placeholder)
                .centerCrop()
                .crossFade()
                .into(holder.ivPic);
        String format = bean.getFormat();
        if ("3".equals(format) || "4".equals(format)) {
            holder.tvMark.setText("360");
        } else {
            holder.tvMark.setText("3D");
        }

        holder.tvTitle.setText(bean.getTitle());
        holder.tvActor.setText("主演:" + bean.getActor());
        holder.tvContent.setText(bean.getProfile());
        holder.tvScore.setText(bean.getScore() + ".0");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoInfoActivity.class);
                intent.putExtra(AppConst.VIDEO_ID, bean.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bottomData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else {
            return DEFAULT;
        }
    }

    public void setAttentionUpdate() {
        if(textView == null){
            return;
        }

        if(ATTENTIONED.equals(textView.getText())){
            textView.setText(ADD_ATTENTION);
        }else{
            textView.setText(ATTENTIONED);
        }

        textView = null;
    }

    static class BrandDetailHolder extends RecyclerView.ViewHolder {
        ImageView ivBrandimg;
        TextView tvFocus;
        TextView tvProfile;
        LinearLayout llTag;
        TextView tvAttention;

        ImageView ivPic;
        TextView tvMark;
        TextView tvTitle;
        TextView tvActor;
        TextView tvContent;
        TextView tvScore;
        View itemView;

        public BrandDetailHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
