package com.yaya.myvr.app;

import com.yaya.myvr.bean.DictInfo;
import com.yaya.myvr.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/3.
 */

public class AppConst {
    public static final String LOOP_IMG = "loop_img";
    public static final String LOOP_TITLE = "loop_title";
    public static final String LOOP_ID = "loop_id";
    public static final String BRAND_ID = "brand_id";
    public static final String VIDEO_ID = "video_id";

    public static final String BRAND_NAME = "brand_name";
    public static final int SWIPE_START = ConvertUtils.dp2px(VRApp.getAppInstance().getApplicationContext(), 20);
    public static final int SWIPE_END = ConvertUtils.dp2px(VRApp.getAppInstance().getApplicationContext(), 92);

    public static final List<DictInfo.DataBean.AreaBean> DICT_AREA = new ArrayList<>();
    public static final List<DictInfo.DataBean.CategoryBean> DICT_CATEGORY = new ArrayList<>();
    public static final List<DictInfo.DataBean.FormatBean> DICT_FORMAT = new ArrayList<>();
    public static final List<DictInfo.DataBean.QualityBean> DICT_QUALITY = new ArrayList<>();


    public static boolean IS_DICT_REQUESTED = false;

    public static final String TYPE_POSITION = "type_postion";
    public static final String TYPE_CATE = "type_cate";
    public static final String TYPE_TAG = "type_tag";
}
