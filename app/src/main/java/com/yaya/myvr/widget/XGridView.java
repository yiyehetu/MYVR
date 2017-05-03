package com.yaya.myvr.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * Created by admin on 2017/5/3.
 */

public class XGridView extends LinearLayout {
    public interface OnItemClickListener {
        void onItemClick(View view, int positon);
    }

    private static final String TAG = XGridView.class.getSimpleName();

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public XGridView(Context context) {
        super(context);
    }

    public XGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(BaseAdapter adapter) {
        int count = adapter.getCount();
        LinearLayout topLayout = (LinearLayout) getChildAt(0);
        topLayout.setOrientation(HORIZONTAL);
        topLayout.setWeightSum(count / 2);
        LinearLayout bottomLayout = (LinearLayout) getChildAt(1);
        bottomLayout.setOrientation(HORIZONTAL);
        bottomLayout.setWeightSum(count / 2);

        for (int i = 0; i < count; i++) {
            final int position = i;
            View view = adapter.getView(i, null, null);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });

            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
            if (i < count / 2) {
                topLayout.addView(view);
            } else {
                bottomLayout.addView(view);
            }
        }
    }


}
