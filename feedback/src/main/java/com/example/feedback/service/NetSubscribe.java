package com.example.feedback.service;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by wjy.
 * Date: 2019/11/22
 * Time: 15:48
 * Describe: ${describe}
 */
public abstract class NetSubscribe<T> implements Observer<T> {

    private Context mContext;

    public NetSubscribe(Context mContext, boolean showDialog) {
        this.mContext = mContext;


    }

    @Override
    public void onNext(T response) {
        String result = response.toString();
        Log.e("NetSubscribe","请求返回数据："+result);
    }

    @Override
    public void onSubscribe(Disposable d) {
        Log.e("NetSubscribe", "onSubscribe  d=" + d);
    }

    @Override
    public void onComplete() {
        Log.e("NetSubscribe", "onComplete");
    }

    @Override
    public void onError(Throwable e) {
        String errorMsg;
        if (e instanceof IOException) {
            /** 没有网络 */
            errorMsg = "Please check your network status";
        } else if (e instanceof HttpException) {
            /** 网络异常，http 请求失败，即 http 状态码不在 [200, 300) 之间, such as: "server internal error". */
            errorMsg = ((HttpException) e).response().message();
        } else {
            /** 其他未知错误 */
            errorMsg = !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "unknown error";
        }
        Log.e("NetSubscribe", "errorMsg=" + errorMsg);
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
