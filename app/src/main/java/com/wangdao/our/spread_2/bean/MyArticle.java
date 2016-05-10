package com.wangdao.our.spread_2.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/4/22 0022.
 */
public class MyArticle {

    private Bitmap articleIcon;
    private String exposureNum;
    private String clickNum;
    private String time;
    private String title;
    private String tryNum;
    private String iconUrl;
    private String id;
    private String iid;

    public MyArticle() {
    }

    public MyArticle(Bitmap articleIcon, String exposureNum, String clickNum, String time) {
        this.articleIcon = articleIcon;
        this.exposureNum = exposureNum;
        this.clickNum = clickNum;
        this.time = time;
    }

    public Bitmap getArticleIcon() {
        return articleIcon;
    }

    public void setArticleIcon(Bitmap articleIcon) {
        this.articleIcon = articleIcon;
    }

    public String getExposureNum() {
        return exposureNum;
    }

    public void setExposureNum(String exposureNum) {
        this.exposureNum = exposureNum;
    }

    public String getClickNum() {
        return clickNum;
    }

    public void setClickNum(String clickNum) {
        this.clickNum = clickNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTryNum() {
        return tryNum;
    }

    public void setTryNum(String tryNum) {
        this.tryNum = tryNum;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }
}
