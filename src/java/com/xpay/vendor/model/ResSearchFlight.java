/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.vendor.model;

/**
 *
 * @author Irwan Cigist <cigist.developer@gmail.com>
 */
public class ResSearchFlight {
    private String searchQueries;
    private String departures;
    private String goDate;
    private String retDate;
    private String nearbyGoDate;
    private String nearbyRetDate;
    private String status;

    public String getSearchQueries() {
        return searchQueries;
    }

    public void setSearchQueries(String searchQueries) {
        this.searchQueries = searchQueries;
    }

    public String getDepartures() {
        return departures;
    }

    public void setDepartures(String departures) {
        this.departures = departures;
    }

    public String getGoDate() {
        return goDate;
    }

    public void setGoDate(String goDate) {
        this.goDate = goDate;
    }

    public String getRetDate() {
        return retDate;
    }

    public void setRetDate(String retDate) {
        this.retDate = retDate;
    }

    public String getNearbyGoDate() {
        return nearbyGoDate;
    }

    public void setNearbyGoDate(String nearbyGoDate) {
        this.nearbyGoDate = nearbyGoDate;
    }

    public String getNearbyRetDate() {
        return nearbyRetDate;
    }

    public void setNearbyRetDate(String nearbyRetDate) {
        this.nearbyRetDate = nearbyRetDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   
    
}
