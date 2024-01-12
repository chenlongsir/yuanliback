package com.example.feedback.adapter;


import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.feedback.R;
import com.example.feedback.service.entity.SuggestListBean;


/**
 * 反馈列表适配器
 */
public class SuggestListAdapter extends BaseQuickAdapter<SuggestListBean.DataDTO, BaseViewHolder> {

    public SuggestListAdapter() {
        super(R.layout.gt_item_suggest);
    }

    @Override
    protected void convert(BaseViewHolder helper, SuggestListBean.DataDTO item) {
        helper.setText(R.id.tv_time, item.getAddtime());
        String temp = "待回复";
        switch (item.getStatus()) {
            case 0:
                temp = "待回复";
                helper.setTextColor(R.id.tv_state, Color.parseColor("#FF584B"));
                break;
            case 1:
                temp = "已回复";
                helper.setTextColor(R.id.tv_state, Color.parseColor("#FF584B"));
                break;
            case 2:
                temp = "已解决";
                helper.setTextColor(R.id.tv_state, Color.parseColor("#7E7E7E"));
                break;

        }
        helper.setText(R.id.tv_state,  temp);/** 状态："**/
        helper.setText(R.id.tv_type, "意见反馈");/** 类型： **/
        helper.setText(R.id.tv_title, item.getTitle());/** 标题： **/
        if (item.getContent().length()>=10){
            helper.setText(R.id.tv_content, item.getContent().substring(0, 10) + "...");/** 内容： **/
        }else {
            helper.setText(R.id.tv_content, item.getContent());/** 内容： **/
        }

    }
}
