/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.vendor.model;

import java.io.Serializable;

/**
 *
 * @author Irwan Cigist <cigist.developer@gmail.com>
 */
public class ResPaymentPLNPascabayar extends InqueryTagihan implements Serializable{
    private String kodeProduk;
    private String waktu;
    private String idpelanggan1;
    private String idpelanggan2;
    private String idpelanggan3;
    private String nominal;
    private String biayaAdmin;
    private String ref1;
    private String ref2;
    private String ref3;
    private String status;
    private String keterangan;
    private String switcherid;
    private String subscriberid;
    private String billstatus;
    private String paymentStatus;
    private String totalOutstandingBill;
    private String swreferenceNumber;
    private String subcriberName;

    public String getKodeProduk() {
        return kodeProduk;
    }

    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getIdpelanggan1() {
        return idpelanggan1;
    }

    public void setIdpelanggan1(String idpelanggan1) {
        this.idpelanggan1 = idpelanggan1;
    }

    public String getIdpelanggan2() {
        return idpelanggan2;
    }

    public void setIdpelanggan2(String idpelanggan2) {
        this.idpelanggan2 = idpelanggan2;
    }

    public String getIdpelanggan3() {
        return idpelanggan3;
    }

    public void setIdpelanggan3(String idpelanggan3) {
        this.idpelanggan3 = idpelanggan3;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getBiayaAdmin() {
        return biayaAdmin;
    }

    public void setBiayaAdmin(String biayaAdmin) {
        this.biayaAdmin = biayaAdmin;
    }

    public String getRef1() {
        return ref1;
    }

    public void setRef1(String ref1) {
        this.ref1 = ref1;
    }

    public String getRef2() {
        return ref2;
    }

    public void setRef2(String ref2) {
        this.ref2 = ref2;
    }

    public String getRef3() {
        return ref3;
    }

    public void setRef3(String ref3) {
        this.ref3 = ref3;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getSwitcherid() {
        return switcherid;
    }

    public void setSwitcherid(String switcherid) {
        this.switcherid = switcherid;
    }

    public String getSubscriberid() {
        return subscriberid;
    }

    public void setSubscriberid(String subscriberid) {
        this.subscriberid = subscriberid;
    }

    public String getBillstatus() {
        return billstatus;
    }

    public void setBillstatus(String billstatus) {
        this.billstatus = billstatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTotalOutstandingBill() {
        return totalOutstandingBill;
    }

    public void setTotalOutstandingBill(String totalOutstandingBill) {
        this.totalOutstandingBill = totalOutstandingBill;
    }

    public String getSwreferenceNumber() {
        return swreferenceNumber;
    }

    public void setSwreferenceNumber(String swreferenceNumber) {
        this.swreferenceNumber = swreferenceNumber;
    }

    public String getSubcriberName() {
        return subcriberName;
    }

    public void setSubcriberName(String subcriberName) {
        this.subcriberName = subcriberName;
    }
    
}
