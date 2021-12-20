package com.fruitshop.service.model;

import java.util.Date;

public class LogisticsModel {
    private String id;
    //订单号
    private String orderId;
    //地址号
    private Integer addressId;
    //创建时间
    private Date createTime;

    public String getId() {
        return id;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
