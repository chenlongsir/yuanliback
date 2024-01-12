package com.example.feedback.service;

import android.util.Base64;
import android.util.Log;

import com.example.feedback.service.entity.BaseResultBean;
import com.example.feedback.service.entity.SuggestDetailBean;
import com.example.feedback.service.entity.SuggestListBean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wjy.
 * Date: 2019/11/22
 * Time: 9:45
 * Describe: Retrofit网络请求工具类,用于初始化Retrofit，设置请求API的baseUrl、gson解析方式
 */
public class RetrofitHelper {

    private String key ="c456018db89f1edcbe5f76d5bc4ff76b###888";
    private Map<String, String> resultMap;
    private MessageDigest alga;

    private static RetrofitHelper instance = null;
    private static OkHttpClient okHttpClient;
    private BaseApi apiService;
    public Retrofit retrofit;

    public RetrofitHelper(OkHttpClient okHttpClient){
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseApi.IP3)
                .addConverterFactory(GsonConverterFactory.create())//用Gson把服务端返回的json数据解析成实体
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();//生成一个Retrofit实例

        apiService = retrofit.create(BaseApi.class);//传入网络接口类,得到接口对象实例,调用接口类中的方法。
    }


    public static RetrofitHelper getInstance(){
        if (instance == null){
            instance = new RetrofitHelper(getOkhttpClient());
        }
        return instance;
    }


    //okhttp连接的一些设置
    public static OkHttpClient getOkhttpClient(){
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)//连接超时时间
                .readTimeout(20,TimeUnit.SECONDS)//读超时时间
                .writeTimeout(20,TimeUnit.SECONDS)//写超时时间
                .retryOnConnectionFailure(true)//失败重连
                .addInterceptor(headerInterceptor)//设置头信息
                .addInterceptor(loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY))// 在此处添加拦截器即可，默认日志级别为BASIC
                .build();
        return okHttpClient;
    }

    //日志
    static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
        //打印retrofit日志
        Log.i("Arms", "retrofitBack = " + message);
    });

    /**
     * 设置头信息
     */
    static Interceptor headerInterceptor = chain -> {
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "text/plain; charset=utf-8")
                .method(originalRequest.method(), originalRequest.body());
        Request request = requestBuilder.build();
        return chain.proceed(request);
    };

    /**
     * 设置缓存
     */
    public static final String CACHE_NAME = "MyCache";


    /**
     * map根据key值比较大小
     */
    private static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<>((str1, str2) -> str1.compareTo(str2));
        sortMap.putAll(map);
        return sortMap;
    }


    /**
     * 针对微信加密机制的问题，提供一个外部方法来解决
     */
    public void changeDigest() {
        if (alga != null) {
            alga.digest();
        }
    }

    /**
     * 内部处理Map集合
     * 得到from表单 (post请求)
     */
    private RequestBody getRequestBody(Map<String, String> map) {
        try {
            alga = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        RequestBody requestBody;
        FormBody.Builder builder = new FormBody.Builder();
        map.put("sign", null);
        resultMap = sortMapByKey(map);
        Log.e("DEF默认请求参数：", "map:" + resultMap.toString());
        String str = "";
        boolean isFirst = true;

        /**
         * 循环遍历获取key值，拼接sign字符串
         */
        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            if (isFirst) {
                str += entry.getKey() + "=" + Base64.encodeToString(entry.getValue().getBytes(), Base64.DEFAULT).trim();
                isFirst = false;
            } else {
                str = str.trim();
                str += "&" + entry.getKey() + "=" + Base64.encodeToString(entry.getValue().getBytes(), Base64.DEFAULT).trim();
            }
        }
        str = str+"&" + "key" + "=" + key;
        str = str.replace("+", "%2B");//去除+号
        str = str.replace("\n", "");//去除换行
        str = str.replace("\\s", "");//去除空格
        str = str.replace("&sign=", "");//去除空格
        changeDigest();
        alga.update(str.getBytes());
        /**
         * 循环遍历value值，添加到表单
         */
        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null) {
                value = "";
            }
            if (key.equals("sign")) {
                value = Base64.encodeToString(byte2hex(alga.digest()).getBytes(), Base64.DEFAULT);
            } else if (key.equals("key")) {
                continue;
            }
            value.replace("+", "%2B").replace("\n", "").replace("\\s", "");
            builder.add(key, value);
            Log.e("DEF默认请求参数：", "key:" + key + "--value:" + value);
        }
        requestBody = builder.build();
        return requestBody;
    }

    /**
     * 数组转字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp;
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    //=========================以下是  通过接口对象,调用接口类中的方法================================//
    public Observable<BaseResultBean> FeedBackAdd(Map<String,String> map){
        return apiService.FeedBackAdd(getRequestBody(map));
    }

    public Observable<BaseResultBean> FeedBackReply(Map<String,String> map){
        return apiService.FeedBackReply(getRequestBody(map));
    }

    public Observable<SuggestListBean> FeedBackList(Map<String,String> map){
        return apiService.FeedBackList(getRequestBody(map));
    }

    public Observable<SuggestDetailBean> FeedBackDetails(Map<String,String> map){
        return apiService.FeedBackDetails(getRequestBody(map));
    }

    public Observable<BaseResultBean> FeedBackEnd(Map<String,String> map){
        return apiService.FeedBackEnd(getRequestBody(map));
    }


}
