
package com.fruitshop.dataobject;

public class AddressDO {
    private Integer id;
    private Integer userId;
    private String addresseeName;
    private String addresseeTelphone;
    private String address;
    private String postcode;
    private Integer status;

    public AddressDO() {
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
        this.addresseeName = addresseeName == null ? null : addresseeName.trim();
    }

    public String getAddresseeTelphone() {
        return this.addresseeTelphone;
    }

    public void setAddresseeTelphone(String addresseeTelphone) {
        this.addresseeTelphone = addresseeTelphone == null ? null : addresseeTelphone.trim();
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode == null ? null : postcode.trim();
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
