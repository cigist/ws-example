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
public class InqueryJawaPos implements Serializable {

    private String kodeProduct;
    private String idPelanggan1;
    private String idPelanggan2;
    private String idPelanggan3;
    private String ref1;
    private String nama;
    private String alamat;
    private String kodeKecamatan;
    private String kodeKelurahan;
    private String rt;
    private String rw;
    private String noHp;
    private String tempatLahir;
    private String tanggalLahir;
    private String noIdentitas;
    private String tanggalAwalPengiriman;

    public String getKodeProduct() {
        return kodeProduct;
    }

    public void setKodeProduct(String kodeProduct) {
        this.kodeProduct = kodeProduct;
    }

    public String getIdPelanggan1() {
        return idPelanggan1;
    }

    public void setIdPelanggan1(String idPelanggan1) {
        this.idPelanggan1 = idPelanggan1;
    }

    public String getIdPelanggan2() {
        return idPelanggan2;
    }

    public void setIdPelanggan2(String idPelanggan2) {
        this.idPelanggan2 = idPelanggan2;
    }

    public String getIdPelanggan3() {
        return idPelanggan3;
    }

    public void setIdPelanggan3(String idPelanggan3) {
        this.idPelanggan3 = idPelanggan3;
    }

    public String getRef1() {
        return ref1;
    }

    public void setRef1(String ref1) {
        this.ref1 = ref1;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKodeKecamatan() {
        return kodeKecamatan;
    }

    public void setKodeKecamatan(String kodeKecamatan) {
        this.kodeKecamatan = kodeKecamatan;
    }

    public String getKodeKelurahan() {
        return kodeKelurahan;
    }

    public void setKodeKelurahan(String kodeKelurahan) {
        this.kodeKelurahan = kodeKelurahan;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getRw() {
        return rw;
    }

    public void setRw(String rw) {
        this.rw = rw;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getNoIdentitas() {
        return noIdentitas;
    }

    public void setNoIdentitas(String noIdentitas) {
        this.noIdentitas = noIdentitas;
    }

    public String getTanggalAwalPengiriman() {
        return tanggalAwalPengiriman;
    }

    public void setTanggalAwalPengiriman(String tanggalAwalPengiriman) {
        this.tanggalAwalPengiriman = tanggalAwalPengiriman;
    }

}
