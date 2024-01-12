package com.example.feedback.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedback.R;
import com.example.feedback.adapter.ReplyAdapter;
import com.example.feedback.service.NetSubscribe;
import com.example.feedback.service.RetrofitHelper;
import com.example.feedback.service.entity.BaseResultBean;
import com.example.feedback.service.entity.ReplyBean;
import com.example.feedback.service.entity.SuggestDetailBean;
import com.example.feedback.service.entity.SuggestListBean;
import com.hjq.toast.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 反馈详情
 */
public class SuggestDetailActivity extends AppCompatActivity {
    private TextView mTimeText, mStateText, mTitleText, mTypeText, mContentText, mExitText,
            mReplyText;
    private RecyclerView mPicRecyclerView, mReplyRecyclerView;
    private NestedScrollView mScrollView;
    private ProgressBar mProgressBar;
    private int mServiceId;
    private ReplyAdapter mReplyAdapter;

    private SuggestListBean.DataDTO dataDTO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gt_activity_suggest_detail);
//        GTUtils.setImmersionStatusBar(this);
        initView();
    }

    /**
     * 初始化页面
     */
    private void initView() {
        dataDTO = (SuggestListBean.DataDTO) getIntent().getSerializableExtra("data");
        mServiceId = dataDTO.getId();
        ImageView back = findViewById(R.id.iv_back);
        mExitText = findViewById(R.id.tv_exit);
        mReplyText = findViewById(R.id.tv_reply);
        mTimeText = findViewById(R.id.tv_time);
        mStateText = findViewById(R.id.tv_state);
        mTitleText = findViewById(R.id.tv_suggest_title);
        mTypeText = findViewById(R.id.tv_type);
        mContentText = findViewById(R.id.tv_content);
        mPicRecyclerView = findViewById(R.id.recyclerview);
        mReplyRecyclerView = findViewById(R.id.recyclerview1);
        mScrollView = findViewById(R.id.scrollView);
        back.setOnClickListener(v -> onBackPressed());
        mExitText.setOnClickListener(v -> endService());
        mReplyText.setOnClickListener(v -> {
            Intent intent = new Intent(SuggestDetailActivity.this, SuggestAddActivity.class);
            intent.putExtra("data", mServiceId);
            startActivityForResult(intent, 121);
        });
        initRecyclerView();


        setView();

        if (mServiceId != -1) {
            getData();
        }
    }

    private void setView() {
        mTimeText.setText(dataDTO.getAddtime());
        String temp = "待回复";
        switch (dataDTO.getStatus()) {
            case 0:
                temp = "待回复";
                break;
            case 1:
                temp = "已回复";
                break;
            case 2:
                temp = "已解决";
                mExitText.setVisibility(View.GONE);
                mReplyText.setVisibility(View.GONE);
                break;
        }
        mStateText.setText(temp);
        mTypeText.setText("意见反馈");/** 类型： **/
        mTitleText.setText(dataDTO.getTitle());/** 标题： **/
        mContentText.setText(dataDTO.getContent());
    }

    /**
     * 初始化列表
     */
    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mPicRecyclerView.setLayoutManager(manager);
        mPicRecyclerView.setNestedScrollingEnabled(false);
        mReplyRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        mReplyRecyclerView.setLayoutManager(manager1);
        mReplyAdapter = new ReplyAdapter(null);
        mReplyRecyclerView.setAdapter(mReplyAdapter);
    }

    /**
     * 获取数据
     */
    private void getData() {


        Map<String, String> map = new HashMap<>();
        map.put("feedbackid", mServiceId + "");

        RetrofitHelper.getInstance()
                .FeedBackDetails(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSubscribe<SuggestDetailBean>(this, false) {
                    @Override
                    public void onNext(SuggestDetailBean baseBean) {
                        if (baseBean.isIssucc()) {
                            List<ReplyBean> data = new ArrayList<>();
                            for (SuggestDetailBean.DataDTO dataDTO:baseBean.getData()){
                                ReplyBean replyBeanUser=  new ReplyBean();
                                replyBeanUser.setType(ReplyBean.RIGHT);
                                replyBeanUser.setAddtime(dataDTO.getAddtime());
                                replyBeanUser.setDescribe(dataDTO.getContent());
                                data.add(replyBeanUser);

                                ReplyBean replyBeanKf=  new ReplyBean();
                                replyBeanKf.setType(ReplyBean.LEFT);
                                if (dataDTO.getReply() != null){
                                    if (dataDTO.getReply().getReply().equals("")){
                                        replyBeanKf.setDescribe("由于工单系统目前正在升级，可能会存在响应迟缓或未响应的情况。");
                                    }else {
                                        replyBeanKf.setDescribe(dataDTO.getReply().getReply());
                                    }

                                    if (dataDTO.getReply().getAddtime().contains("0001-01-01")){
                                        replyBeanKf.setAddtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                .format(System.currentTimeMillis()));
                                    }else {
                                        replyBeanKf.setAddtime(dataDTO.getReply().getAddtime());
                                    }

                                }
                                data.add(replyBeanKf);
                            }
//                            Collections.reverse(data);
                            mReplyAdapter.setList(data);
                            mScrollView.post(() -> mScrollView.fullScroll(View.FOCUS_DOWN));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 结束服务
     */
    private void endService() {
        Map<String, String> map = new HashMap<>();
        map.put("feedbackid", mServiceId + "");
        RetrofitHelper.getInstance()
                .FeedBackEnd(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSubscribe<BaseResultBean>(this, false) {
                    @Override
                    public void onNext(BaseResultBean baseBean) {
                        if (baseBean.isIssucc()) {
                            ToastUtils.show("服务已结束");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 121 && resultCode == RESULT_OK) {
            getData();
        }
    }
}
