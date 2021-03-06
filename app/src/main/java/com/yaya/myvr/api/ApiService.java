package com.yaya.myvr.api;

import com.yaya.myvr.bean.AttentionInfo;
import com.yaya.myvr.bean.BrandBottomInfo;
import com.yaya.myvr.bean.BrandInfo;
import com.yaya.myvr.bean.BrandTopInfo;
import com.yaya.myvr.bean.DictInfo;
import com.yaya.myvr.bean.FindInfo;
import com.yaya.myvr.bean.HomeInfo;
import com.yaya.myvr.bean.LoginInfo;
import com.yaya.myvr.bean.RelativeInfo;
import com.yaya.myvr.bean.SearchInfo;
import com.yaya.myvr.bean.SmsInfo;
import com.yaya.myvr.bean.TypeInfo;
import com.yaya.myvr.bean.VideoInfo;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by yph
 * Time is 2016/11/30 17:52
 * Good Good Study, Day Day Up
 */

public interface ApiService {
    // 首页信息
    @GET("jsonApi")
    Observable<HomeInfo> getHomeInfo(@QueryMap Map<String, String> map);

    // 分类信息
    @GET("jsonApi")
    Observable<DictInfo> getDictInfo(@QueryMap Map<String, String> map);

    @GET("jsonApi")
    Observable<TypeInfo> getTypeInfo(@QueryMap Map<String, String> map);

    // 品牌信息
    @GET("jsonApi")
    Observable<BrandInfo> getBrandInfo(@QueryMap Map<String, String> map);

    // 品牌详情
    @GET("jsonApi")
    Observable<BrandTopInfo> getBrandTopInfo(@QueryMap Map<String, String> map);

    @GET("jsonApi")
    Observable<BrandBottomInfo> getBrandBottomInfo(@QueryMap Map<String, String> map);

    // 发现信息
    @GET("jsonApi")
    Observable<FindInfo> getFindInfo(@QueryMap Map<String, String> map);

    // 视频信息
    @GET("jsonApi")
    Observable<VideoInfo> getVideoInfo(@QueryMap Map<String, String> map);

    @GET("jsonApi")
    Observable<RelativeInfo> getRelativeInfo(@QueryMap Map<String, String> map);

    // 搜索信息
    @GET("jsonApi")
    Observable<SearchInfo> getSearchInfo(@QueryMap Map<String, String> map);

    // 登陆
    @FormUrlEncoded
    @POST("userApi")
    Observable<LoginInfo> getLoginInfo(@QueryMap Map<String, String> map, @Field("deviceId") String deviceId, @Field("password") String password, @Field("phoneNumber") String phoneNumber);

    // 注册验证码
    @GET("userApi")
    Observable<SmsInfo> getSmsInfo(@QueryMap Map<String, String> map);

    // 注册
    @FormUrlEncoded
    @POST("userApi")
    Observable<LoginInfo> getRegisterInfo(@QueryMap Map<String, String> map, @Field("password") String password, @Field("code") String code, @Field("phoneNumber") String phoneNumber);

    // 关注
    @GET("userApi")
    Observable<AttentionInfo> getAttentionInfo(@QueryMap Map<String, String> map);
}
