package com.wangdao.our.spread_2.bean;

import android.graphics.Bitmap;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/22 0022.
 */
public class RecommendArticle {

    private String writing_title;
    private String writing_use;
    private String writing_img;
    private String id;
    private String writing_brief;

    public RecommendArticle() {
    }


    public String getWriting_title() {
        return writing_title;
    }

    public void setWriting_title(String writing_title) {
        this.writing_title = writing_title;
    }

    public String getWriting_use() {
        return writing_use;
    }

    public void setWriting_use(String writing_use) {
        this.writing_use = writing_use;
    }

    public String getWriting_img() {
        return writing_img;
    }

    public void setWriting_img(String writing_img) {
        this.writing_img = writing_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWriting_brief() {
        return writing_brief;
    }

    public void setWriting_brief(String writing_brief) {
        this.writing_brief = writing_brief;
    }
}
