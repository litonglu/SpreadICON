package com.wangdao.our.spread_2.bean;

import android.graphics.Bitmap;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/22 0022.
 */
public class RecommendArticle {

    private Bitmap rd_icon;
    private String title;
    private String tryNum;
    private String iconUrl;
    private String aId;

    public RecommendArticle() {
    }

    public RecommendArticle(Bitmap rd_icon, String title, String tryNum) {
        this.rd_icon = rd_icon;
        this.title = title;
        this.tryNum = tryNum;
    }

    public Bitmap getRd_icon() {
        return rd_icon;
    }

    public void setRd_icon(Bitmap rd_icon) {
        this.rd_icon = rd_icon;
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

    public String getaId() {
        return aId;
    }

    public void setaId(String aId) {
        this.aId = aId;
    }
}
