package com.example.feedback.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.feedback.R;
import com.example.feedback.service.entity.ReplyBean;

import java.util.List;

/**
 * 反馈回复适配器
 */
public class ReplyAdapter extends BaseMultiItemQuickAdapter<ReplyBean, BaseViewHolder> {

    public ReplyAdapter(List<ReplyBean> data) {
        super(data);
        addItemType(ReplyBean.LEFT, R.layout.feed_reply_item_left);
        addItemType(ReplyBean.RIGHT, R.layout.feed_reply_item_right);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReplyBean item) {
        helper.setText(R.id.tv_content, item.getDescribe());
        helper.setText(R.id.tv_time, item.getAddtime());
    }
}
