package com.example.feedback.service.entity;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SuggestDetailBean {

    @SerializedName("data")
    private List<DataDTO> data;
    @SerializedName("issucc")
    private boolean issucc;
    @SerializedName("msg")
    private String msg;
    @SerializedName("code")
    private String code;

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public boolean isIssucc() {
        return issucc;
    }

    public void setIssucc(boolean issucc) {
        this.issucc = issucc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class DataDTO {
        @SerializedName("title")
        private String title;
        @SerializedName("content")
        private String content;
        @SerializedName("addtime")
        private String addtime;
        @SerializedName("reply")
        private ReplyDTO reply;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddtime() {
            return dealDateFormat(addtime);
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public ReplyDTO getReply() {
            return reply;
        }

        public void setReply(ReplyDTO reply) {
            this.reply = reply;
        }

        public static class ReplyDTO {
            @SerializedName("reply")
            private String reply;
            @SerializedName("addtime")
            private String addtime;

            public String getReply() {
                return reply;
            }

            public void setReply(String reply) {
                this.reply = reply;
            }

            public String getAddtime() {
                return dealDateFormat(addtime);
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }
        }
    }



    /**
     * 将 2018-08-21T03:12:58.000+0000 格式化为日期
     */
    public static String dealDateFormat(String oldDate) {
        Log.d("TAG", "dealDateFormat: " + oldDate);
        String str = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = df.parse(oldDate);
            Log.d("TAG", "dealDateFormat: " + date.toString());
//            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK);
//            date1 = df1.parse(date.toString());
//            Log.d("TAG", "dealDateFormat: " + date1.toString());
            SimpleDateFormat  df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = df2.format(date);
            Log.d("TAG", "dealDateFormat: " + str);

        } catch (ParseException e) {
            Log.d("TAG", "dealDateFormat: " + e.getMessage());
            e.printStackTrace();
        }
        return str;
    }

}
