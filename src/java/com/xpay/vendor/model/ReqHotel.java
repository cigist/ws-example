/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.vendor.model;

import java.io.Serializable;

/**
 *
 * @author cigist
 */
public class ReqHotel implements Serializable{
    private String cityId;
    private Integer room;
    private Integer guest;
    private String checkInDate;
    private String checkOutDate;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Integer getGuest() {
        return guest;
    }

    public void setGuest(Integer guest) {
        this.guest = guest;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

   
}
