/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.dao;

/**
 *
 * @author cigist
 */
public class SqlQuery {
    public final String SELECT_PPOB = "SELECT a.distri_id,a.provider_id,a.product_id,b.description,a.distri_price,a.distri_admin_fee,a.distri_fee FROM MST_PRICE_PPOB a"
                                                + " join MST_PRODUCT_PPOB b on a.product_id=b.product_id where distri_id=?";
    
}
