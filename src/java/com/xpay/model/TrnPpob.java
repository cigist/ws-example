/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.model;

import java.io.Serializable;

/**
 *
 * @author xmedia
 */
public class TrnPpob extends MstDistriPpob implements Serializable{
    private String trnNo;
    private String trnRefNo;
    private String distriId;
    private Double amount;
    private Double amountFee;
    private Double amountAdmin;
    private Double totalAmount;
    private String trnStatus;
    private String userTrn;
    private String dateTirmTrn;
    private String ipAddress;

    public String getTrnNo() {
        return trnNo;
    }

    public void setTrnNo(String trnNo) {
        this.trnNo = trnNo;
    }

    public String getTrnRefNo() {
        return trnRefNo;
    }

    public void setTrnRefNo(String trnRefNo) {
        this.trnRefNo = trnRefNo;
    }

    public String getDistriId() {
        return distriId;
    }

    public void setDistriId(String distriId) {
        this.distriId = distriId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountFee() {
        return amountFee;
    }

    public void setAmountFee(Double amountFee) {
        this.amountFee = amountFee;
    }

    public Double getAmountAdmin() {
        return amountAdmin;
    }

    public void setAmountAdmin(Double amountAdmin) {
        this.amountAdmin = amountAdmin;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTrnStatus() {
        return trnStatus;
    }

    public void setTrnStatus(String trnStatus) {
        this.trnStatus = trnStatus;
    }

    public String getUserTrn() {
        return userTrn;
    }

    public void setUserTrn(String userTrn) {
        this.userTrn = userTrn;
    }

    public String getDateTirmTrn() {
        return dateTirmTrn;
    }

    public void setDateTirmTrn(String dateTirmTrn) {
        this.dateTirmTrn = dateTirmTrn;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}