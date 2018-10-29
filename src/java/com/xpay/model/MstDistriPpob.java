/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.model;

import java.io.Serializable;

/**
 *
 * @author irwan cigist /irwancigist@gmail.com
 */
public class MstDistriPpob implements Serializable {

    private String trnType;
    private String providerId;
    private String productType;
    private String productId;
    private Double distriPrice;
    private Double distriAdminFee;
    private Double distriFee;
    private String description;
    private String responese;

    public String getTrnType() {
        return trnType;
    }

    public void setTrnType(String trnType) {
        this.trnType = trnType;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Double getDistriPrice() {
        return distriPrice;
    }

    public void setDistriPrice(Double distriPrice) {
        this.distriPrice = distriPrice;
    }

    public Double getDistriAdminFee() {
        return distriAdminFee;
    }

    public void setDistriAdminFee(Double distriAdminFee) {
        this.distriAdminFee = distriAdminFee;
    }

    public Double getDistriFee() {
        return distriFee;
    }

    public void setDistriFee(Double distriFee) {
        this.distriFee = distriFee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponese() {
        return responese;
    }

    public void setResponese(String responese) {
        this.responese = responese;
    }

}
