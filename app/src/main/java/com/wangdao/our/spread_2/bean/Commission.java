package com.wangdao.our.spread_2.bean;

/**
 * Created by Administrator on 2016/5/11 0011.
 * 佣金
 */
public class Commission {

    private String cIconUrl;//头像
    private String cTime;   //创建时间
    private String cRemark; //信息
    private String cPrice;  //钱
    private String cNum;
    private String payWay;
    private String cId;
    private String cStatus;

    public Commission() {
    }

    public String getcIconUrl() {
        return cIconUrl;
    }

    public void setcIconUrl(String cIconUrl) {
        this.cIconUrl = cIconUrl;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public String getcRemark() {
        return cRemark;
    }

    public void setcRemark(String cRemark) {
        this.cRemark = cRemark;
    }

    public String getcPrice() {
        return cPrice;
    }

    public void setcPrice(String cPrice) {
        this.cPrice = cPrice;
    }

    public String getcNum() {
        return cNum;
    }

    public void setcNum(String cNum) {
        this.cNum = cNum;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcStatus() {
        return cStatus;
    }

    public void setcStatus(String cStatus) {
        this.cStatus = cStatus;
    }
}
