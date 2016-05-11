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
}
