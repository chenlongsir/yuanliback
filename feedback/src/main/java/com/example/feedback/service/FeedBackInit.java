package com.example.feedback.service;

public class FeedBackInit {
    private FeedBack feedBack;

    private FeedBackInit(){

    }

    public static FeedBackInit getInstance(){
        return InnerFeedBack.feedBackInit;
    }

    public void init(FeedBack feedBack){
        this.feedBack = feedBack;
    }

    private static class InnerFeedBack{
        public static FeedBackInit feedBackInit = new FeedBackInit();
    }

    public String getName(){
        return feedBack.getName();
    };

    public String getAccount(){
        return feedBack.getAccount();
    }

    public String getAppName(){
        return feedBack.getAppName();
    }


    public void toast(String msg) {
         feedBack.toast(msg);
    }

    public interface FeedBack{
        String getName();
        String getAccount();
        String getAppName();

        void toast(String msg);
    }
}
