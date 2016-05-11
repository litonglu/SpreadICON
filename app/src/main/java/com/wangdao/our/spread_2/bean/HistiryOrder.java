package com.wangdao.our.spread_2.bean;

import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/10 0010.
 */
public class HistiryOrder {

    private String time;
    private String price;
    private String account;
    private String result;
    private boolean wx;
    private String payWay;

    public HistiryOrder() {
    }

    public HistiryOrder(String time, String price, String account, String result, boolean wx) {
        this.time = time;
        this.price = price;
        this.account = account;
        this.result = result;
        this.wx = wx;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isWx() {
        return wx;
    }

    public void setWx(boolean wx) {
        this.wx = wx;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }
}
