package com.yaya.myvr.app;

import com.yaya.myvr.bean.DictInfo;
import com.yaya.myvr.util.ConvertUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public static final Map<String, String> DICT_AREA = new HashMap<>();
    public static final Map<String, String> DICT_FORMAT = new HashMap<>();
    public static final Map<String, String> DICT_QUALITY = new HashMap<>();
    public static final List<DictInfo.DataBean.CategoryBean> DICT_CATEGORY = new ArrayList<>();
    public static boolean IS_DICT_REQUESTED = false;

    public static final String TYPE_POSITION = "type_postion";
    public static final String TYPE_CATE = "type_cate";
    public static final String TYPE_TAG = "type_tag";


    public static final int LOCAL_VIDEO = 100;
    public static final int COLLECT_VIDEO = 101;
    public static final int CACHE_VIDEO = 102;
    public static final int ONLINE_VIDEO = 103;
    public static final int LOGIN = 104;
    public static final int REGISTER = 105;
    public static final int SETTING = 106;
}
