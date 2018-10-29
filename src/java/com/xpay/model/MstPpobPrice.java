/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author cigist
 */
public class MstPpobPrice extends MstProductPPOB implements Serializable{
    private String distriId;
    private String providerId;
    private String productId;
    private Double distriPrice;
    private Double distriAdminFee;
    private Double distriFee;

    public String getDistriId() {
        return distriId;
    }

    public void setDistriId(String distriId) {
        this.distriId = distriId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
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

   
}
