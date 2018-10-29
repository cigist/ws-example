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
public class FlightDepAirport implements Serializable{
    private String airportCode;
    private String internasional;
    private String transNameId;
    private String bannerImage;
    private String shortNameTransId;
    private String businessNAmeTrnasId;
    private String businessCountry;
    private String businnessId;
    private String countryName;
    private String cityName;
    private String provinceName;
    private String shortName;
    private String locationName;

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getInternasional() {
        return internasional;
    }

    public void setInternasional(String internasional) {
        this.internasional = internasional;
    }

    public String getTransNameId() {
        return transNameId;
    }

    public void setTransNameId(String transNameId) {
        this.transNameId = transNameId;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getShortNameTransId() {
        return shortNameTransId;
    }

    public void setShortNameTransId(String shortNameTransId) {
        this.shortNameTransId = shortNameTransId;
    }

    public String getBusinessNAmeTrnasId() {
        return businessNAmeTrnasId;
    }

    public void setBusinessNAmeTrnasId(String businessNAmeTrnasId) {
        this.businessNAmeTrnasId = businessNAmeTrnasId;
    }

    public String getBusinessCountry() {
        return businessCountry;
    }

    public void setBusinessCountry(String businessCountry) {
        this.businessCountry = businessCountry;
    }

    public String getBusinnessId() {
        return businnessId;
    }

    public void setBusinnessId(String businnessId) {
        this.businnessId = businnessId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
}
