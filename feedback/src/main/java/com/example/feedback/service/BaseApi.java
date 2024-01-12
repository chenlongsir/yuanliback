package com.example.feedback.service;




import com.example.feedback.service.entity.BaseResultBean;
import com.example.feedback.service.entity.SuggestDetailBean;
import com.example.feedback.service.entity.SuggestListBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 存放一些与 API 有关的东西,如请求地址,请求码等
 */
public interface BaseApi {
    String IP3 = "http://query.csweimei.cn/";// 正式地址 http://master.dreamyin.cn/ 测试 https://testweb.utools.club/
//


    // 意见反馈
    @POST("app/feeback.add")
    Observable<BaseResultBean> FeedBackAdd(@Body RequestBody body);


    // 回复
    @POST("app/feeback.reply")
    Observable<BaseResultBean> FeedBackReply(@Body RequestBody body);

    // 反馈列表
    @POST("app/feeback.list")
    Observable<SuggestListBean> FeedBackList(@Body RequestBody body);

    // 反馈详情
    @POST("app/feeback.details")
    Observable<SuggestDetailBean> FeedBackDetails(@Body RequestBody body);

    // 结束反馈
    @POST("app/feeback.end")
    Observable<BaseResultBean> FeedBackEnd(@Body RequestBody body);






}


