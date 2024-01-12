package com.example.feedback.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedback.R;
import com.example.feedback.adapter.SuggestListAdapter;
import com.example.feedback.service.FeedBackInit;
import com.example.feedback.service.NetSubscribe;
import com.example.feedback.service.RetrofitHelper;
import com.example.feedback.service.entity.SuggestListBean;
import com.hjq.toast.ToastUtils;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 反馈列表
 */
public class SuggestListActivity extends AppCompatActivity {
    private LinearLayout mEmptyView;
    private PullToRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SuggestListAdapter mAdapter;
    private int mCurrentPage = 1, mLimit = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gt_activity_suggest_list);
        ToastUtils.init(getApplication());
//        GTUtils.setImmersionStatusBar(this);
        initView();
    }

    /**
     * 初始化页面
     */
    private void initView() {
        ImageView back = findViewById(R.id.iv_back);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        TextView add = findViewById(R.id.tv_add);
        back.setOnClickListener(v -> onBackPressed());
        add.setOnClickListener(v ->
                startActivityForResult(new Intent(SuggestListActivity.this, SuggestAddActivity.class), 121));
        initRecyclerView();
    }

    /**
     * 初始化列表
     */
    private void initRecyclerView() {
        mAdapter = new SuggestListAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.gt_emptyview,null));
        mRecyclerView.setAdapter(mAdapter);


        mRefreshLayout.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                getData(true);
                mRefreshLayout.finishRefresh();
            }

            @Override
            public void loadMore() {
                getData(false);
                mRefreshLayout.finishLoadMore();
            }
        });
        getData(true);
        mAdapter.addChildClickViewIds(R.id.item);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Intent intent = new Intent(SuggestListActivity.this, SuggestDetailActivity.class);
            intent.putExtra("data", (Serializable) mAdapter.getData().get(position));
            startActivityForResult(intent, 121);
        });
    }

    /**
     * 获取数据
     */
    private void getData(boolean isRefresh) {
        if (isRefresh) {
            mCurrentPage = 1;
        } else {
            mCurrentPage += 1;
        }

        Map<String, String> map = new HashMap<>();
        map.put("userid", FeedBackInit.getInstance().getAccount());
        map.put("appname", FeedBackInit.getInstance().getAppName());
        map.put("pageindex", mCurrentPage + "");
        map.put("pagesize", "10");
        RetrofitHelper.getInstance()
                .FeedBackList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSubscribe<SuggestListBean>(this, false) {
                    @Override
                    public void onNext(SuggestListBean baseBean) {
                        if (baseBean.isIssucc()) {
                            mAdapter.setList(baseBean.getData());
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
            getData(true);
        }
    }
}
