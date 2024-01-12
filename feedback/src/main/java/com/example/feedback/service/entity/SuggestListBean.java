package com.example.feedback.service.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SuggestListBean extends BaseResultBean implements Serializable {

    @SerializedName("page")
    private int page;
    @SerializedName("count")
    private int count;
    @SerializedName("items")
    private List<DataDTO> items;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DataDTO> getData() {
        return items;
    }

    public void setItems(List<DataDTO> items) {
        this.items = items;
    }

    public static class DataDTO implements Serializable{
        @SerializedName("id")
        private int id;
        @SerializedName("userid")
        private String userid;
        @SerializedName("username")
        private String username;
        @SerializedName("title")
        private String title;
        @SerializedName("content")
        private String content;
        @SerializedName("status")
        private int status;
        @SerializedName("addtime")
        private String addtime;
        @SerializedName("appname")
        private String appname;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAddtime() {
            return dealDateFormat(addtime);
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }
    }

    /**
     * 将 2018-08-21T03:12:58.000+0000 格式化为日期
     */
    public static String dealDateFormat(String oldDate) {
        Date date1 = null;
        DateFormat df2 = null;
        String str = "";
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = df.parse(oldDate);
//            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK);
//            date1 = df1.parse(date.toString());
            df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str =  df2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
