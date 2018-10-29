/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.vendor.model;

import java.io.Serializable;

/**
 *
 * @author xmedia
 */
public class PaymentBpjs implements Serializable{
    private String tanggal;
    private String noResi;
    private String noBpjsPeserta;
    private String namaPeserta;
    private String jmlPeserta;
    private String noRef;
    private String noHandPhone;
    private String noRefrensi;
    private String jumlahPremi;
    private String jumlahTagihan;
    private String biayaAdmin;
    private String totalBayar;

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNoResi() {
        return noResi;
    }

    public void setNoResi(String noResi) {
        this.noResi = noResi;
    }

    public String getNoBpjsPeserta() {
        return noBpjsPeserta;
    }

    public void setNoBpjsPeserta(String noBpjsPeserta) {
        this.noBpjsPeserta = noBpjsPeserta;
    }

    public String getNamaPeserta() {
        return namaPeserta;
    }

    public void setNamaPeserta(String namaPeserta) {
        this.namaPeserta = namaPeserta;
    }

    public String getJmlPeserta() {
        return jmlPeserta;
    }

    public void setJmlPeserta(String jmlPeserta) {
        this.jmlPeserta = jmlPeserta;
    }

    public String getNoRef() {
        return noRef;
    }

    public void setNoRef(String noRef) {
        this.noRef = noRef;
    }

    public String getNoHandPhone() {
        return noHandPhone;
    }

    public void setNoHandPhone(String noHandPhone) {
        this.noHandPhone = noHandPhone;
    }

    public String getNoRefrensi() {
        return noRefrensi;
    }

    public void setNoRefrensi(String noRefrensi) {
        this.noRefrensi = noRefrensi;
    }

    public String getJumlahPremi() {
        return jumlahPremi;
    }

    public void setJumlahPremi(String jumlahPremi) {
        this.jumlahPremi = jumlahPremi;
    }

    public String getJumlahTagihan() {
        return jumlahTagihan;
    }

    public void setJumlahTagihan(String jumlahTagihan) {
        this.jumlahTagihan = jumlahTagihan;
    }

    public String getBiayaAdmin() {
        return biayaAdmin;
    }

    public void setBiayaAdmin(String biayaAdmin) {
        this.biayaAdmin = biayaAdmin;
    }

    public String getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(String totalBayar) {
        this.totalBayar = totalBayar;
    }
            
}
