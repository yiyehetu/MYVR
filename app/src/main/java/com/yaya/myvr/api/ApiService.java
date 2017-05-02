package com.yaya.myvr.api;

import com.yaya.myvr.bean.HomeInfo;

import java.util.Map;

import retrofit2.http.GET;
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
//    // 画报
//    @GET("articles/daily/simple/")
//    Observable<BildInfo> getBildInfo(@QueryMap Map<String, String> map);
//    // 画报内容
//    @GET("article/{id}/")
//    Observable<BildContentInfo> getBildContentInfo(@Path("id") int id, @QueryMap Map<String, String> map);
//    // 我关注的
//    @GET("designers/followuser/{id}/")
//    Observable<DesignerInfo> getFollowUserInfo(@Path("id") String id, @QueryMap Map<String, String> map);
//    // 推荐
//    @GET("designers/recommend/")
//    Observable<DesignerInfo> getRecommendInfo(@QueryMap Map<String, String> map);
//    // 最受欢迎
//    @GET("designers/mostfavor/")
//    Observable<DesignerInfo> getMostFavorInfo(@QueryMap Map<String, String> map);
//    // 设计师
//    @GET("designers/category/{id}/")
//    Observable<DesignerInfo> getDesignerInfo(@Path("id") int id, @QueryMap Map<String, String> map);
//    // 关注
//    @FormUrlEncoded
//    @POST("designer/{id}/follow/")
//    Observable<DesignerInfo> followDesigner(@Path("id") int id, @QueryMap Map<String, String> map, @Field("user_id") String user_id);
//    // 取消关注
//    @FormUrlEncoded
//    @POST("designer/{id}/follow/cancel/")
//    Observable<DesignerInfo> unFollowDesigner(@Path("id") int id, @QueryMap Map<String, String> map, @Field("user_id") String user_id);
}
