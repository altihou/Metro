package com.accelerator.metro.bean;

/**
 * Created by Nicholas on 2016/8/3.
 */
public class FinishOrder {

    private String orderSn;
    private int orderType;
    private int isComplete;
    private String orderMoney;
    private int time;
    private String startPoint;
    private String endPoint;
    private String userId;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "FinishOrder{" +
                "orderSn='" + orderSn + '\'' +
                ", orderType=" + orderType +
                ", isComplete=" + isComplete +
                ", orderMoney='" + orderMoney + '\'' +
                ", time=" + time +
                ", startPoint='" + startPoint + '\'' +
                ", endPoint='" + endPoint + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
