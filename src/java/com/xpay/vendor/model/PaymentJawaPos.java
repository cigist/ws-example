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
public class PaymentJawaPos extends InqueryJawaPos implements Serializable{
    private String nominal;
    private String ref2;
    private String ref3;

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
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
}
