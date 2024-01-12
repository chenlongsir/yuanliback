package com.example.feedback.service.entity;

import com.google.gson.annotations.SerializedName;

public class BaseResultBean {
    @SerializedName("issucc")
    private boolean issucc;
    @SerializedName("msg")
    private String msg;
    @SerializedName("code")
    private String code;

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
}
