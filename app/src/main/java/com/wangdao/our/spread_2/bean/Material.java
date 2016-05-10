package com.wangdao.our.spread_2.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/22 0022.
 * 素材
 */
public class Material implements Serializable{

    private String icon_url;
    private String mTitle;
    private String mInfo;
    private String mUrl;
    private String mAdress;

    private boolean mTop;
    private boolean mBottom;
    private boolean mTailor;

    private String mName;
    private String mPhone;

    private int mType;//类型

    private String mId;//广告的id
    private boolean isChoose = false;

    public Material() {
    }

    public Material(String mTitle, String mInfo,  String mUrl, String mAdress, boolean mTop, boolean mBottom, boolean mTailor, int mType) {
        this.mTitle = mTitle;
        this.mInfo = mInfo;
        this.mUrl = mUrl;
        this.mAdress = mAdress;
        this.mTop = mTop;
        this.mBottom = mBottom;
        this.mTailor = mTailor;
        this.mType = mType;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmInfo() {
        return mInfo;
    }

    public void setmInfo(String mInfo) {
        this.mInfo = mInfo;
    }




    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmAdress() {
        return mAdress;
    }

    public void setmAdress(String mAdress) {
        this.mAdress = mAdress;
    }

    public boolean ismTop() {
        return mTop;
    }

    public void setmTop(boolean mTop) {
        this.mTop = mTop;
    }

    public boolean ismBottom() {
        return mBottom;
    }

    public void setmBottom(boolean mBottom) {
        this.mBottom = mBottom;
    }

    public boolean ismTailor() {
        return mTailor;
    }

    public void setmTailor(boolean mTailor) {
        this.mTailor = mTailor;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }
}
