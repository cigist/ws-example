/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.api.vendor;

import com.cigist.framework.core.CigistHttpConn;
import com.xpay.util.StaticParam;
import com.xpay.vendor.model.ReqFlight;
import com.xpay.vendor.model.ReqHotel;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author cigist
 */
public class FasTravelAPI {

    CigistHttpConn conn = new CigistHttpConn();
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

    public String getAirport(String product, String token) {
        nameValuePairs.add(new BasicNameValuePair("product", product));
        nameValuePairs.add(new BasicNameValuePair("token", token));
        String respone = conn.Post("flight/airport", nameValuePairs.toString());
        if (respone != null) {
            JSONObject root = new JSONObject(respone);
            JSONArray dataArray = root.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject airport = dataArray.getJSONObject(i);
                System.out.println(airport.getString("code"));
                System.out.println(airport.getString("name"));
                System.out.println(airport.getString("group"));
            }

        }
        return respone;

    }

    // API HOTEL
    public String searchHotel(ReqHotel hotel) {
        nameValuePairs.add(new BasicNameValuePair("cityId", hotel.getCityId()));
        nameValuePairs.add(new BasicNameValuePair("room", hotel.getRoom().toString()));
        nameValuePairs.add(new BasicNameValuePair("guest", hotel.getGuest().toString()));
        nameValuePairs.add(new BasicNameValuePair("checkInDate", hotel.getCheckInDate()));
        nameValuePairs.add(new BasicNameValuePair("checkOutDate", hotel.getCheckOutDate()));
        nameValuePairs.add(new BasicNameValuePair("token", StaticParam.FASFAY_TOKEN));
        String respone = conn.Post("hotel/search", nameValuePairs.toString());
        if (respone != null) {
            JSONObject root = new JSONObject(respone);
            JSONArray dataArray = root.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject airport = dataArray.getJSONObject(i);
            }
        }
        return respone;
    }

    public String searchFlight(ReqFlight flight) {
        nameValuePairs.add(new BasicNameValuePair("airline", flight.getAirline()));
        nameValuePairs.add(new BasicNameValuePair("departure", flight.getDeparture()));
        nameValuePairs.add(new BasicNameValuePair("arrival", flight.getArrival()));
        nameValuePairs.add(new BasicNameValuePair("departuredate", flight.getDepartureDate()));
        nameValuePairs.add(new BasicNameValuePair("returndate", flight.getReturnDate()));
        nameValuePairs.add(new BasicNameValuePair("islowestprice", flight.getIsLowestPrice().toString()));
        nameValuePairs.add(new BasicNameValuePair("adult", flight.getAdult().toString()));
        nameValuePairs.add(new BasicNameValuePair("child", flight.getChild().toString()));
        nameValuePairs.add(new BasicNameValuePair("infant", flight.getInfant().toString()));
        nameValuePairs.add(new BasicNameValuePair("token", StaticParam.FASFAY_TOKEN));
        String respone = conn.Post("flight/search", nameValuePairs.toString());
        if (respone != null) {
            JSONObject root = new JSONObject(respone);
            JSONArray dataArray = root.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject flightRes = dataArray.getJSONObject(i);
            }
        } else {

        }

        return respone;
    }
}
