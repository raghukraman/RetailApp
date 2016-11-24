package com.retail.retailapp.vo;

/**
 * Created by raghuramankumarasamy on 21/11/16.
 */
public class Order {

    private String orderNo;

    private String creationDate;

    private Integer status;

    public String getOrderNo() {
        return orderNo;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNo='" + orderNo + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", status=" + status +
                '}';
    }
}
