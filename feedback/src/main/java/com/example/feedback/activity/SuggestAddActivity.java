package com.example.feedback.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feedback.R;
import com.example.feedback.service.FeedBackInit;
import com.example.feedback.service.NetSubscribe;
import com.example.feedback.service.RetrofitHelper;
import com.example.feedback.service.entity.BaseResultBean;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SuggestAddActivity extends AppCompatActivity {

    EditText title,content;
    Button submit;
    String userName,userId;
    int feedbackid = -1;
    LinearLayout llTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_add);

        initView();
    }


    private void initView() {
        feedbackid = getIntent().getIntExtra("data",-1);
        //获取用户名
        userName = FeedBackInit.getInstance().getName();
        userId = FeedBackInit.getInstance().getAccount();
        llTitle = findViewById(R.id.ll_title);

        if (feedbackid != -1){
            llTitle.setVisibility(View.GONE);
        }else {
            llTitle.setVisibility(View.VISIBLE);
        }

        submit = findViewById(R.id.f_sub);
        title = findViewById(R.id.f_title);
        content = findViewById(R.id.f_edit);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        submit.setOnClickListener(v -> add());
    }

    private void add() {
        if (content.getText().toString().equals("")){
            FeedBackInit.getInstance().toast("请填写内容");
            return;
        }


        Map<String,String> map = new HashMap<>();
        map.put("content",content.getText().toString());
        if (feedbackid == -1){

            if (title.getText().toString().equals("")){
                FeedBackInit.getInstance().toast("请填写标题");
                return;
            }
            map.put("userid",userId);
            map.put("username", userName);
            map.put("title",title.getText().toString());
            map.put("appname", FeedBackInit.getInstance().getAppName());

            RetrofitHelper.getInstance()
                    .FeedBackAdd(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetSubscribe<BaseResultBean>(this, false) {
                        @Override
                        public void onNext(BaseResultBean baseBean) {
                            if (baseBean.isIssucc()){
                                Toast.makeText(SuggestAddActivity.this,baseBean.getMsg(),Toast.LENGTH_SHORT);
                                setResult(RESULT_OK);
                                finish();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    });
        }else {
            map.put("feedbackid",feedbackid+"");
            RetrofitHelper.getInstance()
                    .FeedBackReply(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetSubscribe<BaseResultBean>(this, false) {
                        @Override
                        public void onNext(BaseResultBean baseBean) {
                            if (baseBean.isIssucc()){
                                Toast.makeText(SuggestAddActivity.this,baseBean.getMsg(),Toast.LENGTH_SHORT);
                                setResult(RESULT_OK);
                                finish();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
}