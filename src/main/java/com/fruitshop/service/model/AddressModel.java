//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fruitshop.service.model;

public class AddressModel {
    private Integer id;
    private Integer userId;
    private String addresseeName;
    private String addresseeTelphone;
    private String address;
    private String postcode;
    private Integer status;

    public AddressModel() {
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddresseeName() {
        return this.addresseeName;
    }

    public void setAddresseeName(String addresseeName) {
        this.addresseeName = addresseeName;
    }

    public String getAddresseeTelphone() {
        return this.addresseeTelphone;
    }

    public void setAddresseeTelphone(String addresseeTelphone) {
        this.addresseeTelphone = addresseeTelphone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
