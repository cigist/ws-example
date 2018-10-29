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
public class ReqFlight implements Serializable{
    private String airline;
    private String departure;
    private String arrival;
    private String departureDate;
    private String returnDate;
    private Boolean isLowestPrice;
    private Integer adult;
    private Integer child;
    private Integer infant; 

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public Boolean getIsLowestPrice() {
        return isLowestPrice;
    }

    public void setIsLowestPrice(Boolean isLowestPrice) {
        this.isLowestPrice = isLowestPrice;
    }

    public Integer getAdult() {
        return adult;
    }

    public void setAdult(Integer adult) {
        this.adult = adult;
    }

    public Integer getChild() {
        return child;
    }

    public void setChild(Integer child) {
        this.child = child;
    }

    public Integer getInfant() {
        return infant;
    }

    public void setInfant(Integer infant) {
        this.infant = infant;
    }
}
