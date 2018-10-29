/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.api.vendor;

import com.cigist.framework.core.CigistHttpConn;
import com.xpay.util.StaticParam;
import com.xpay.vendor.model.ReqFlightAddOrder;
import com.xpay.vendor.model.ResSearchFlight;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author xmedia
 */
public class TiketDotComAPI {

    CigistHttpConn conn = new CigistHttpConn();
    String BASE_URL = "http://api-sandbox.tiket.com/";
    StringBuilder sBuild = new StringBuilder();

    public Vector<ResSearchFlight> searchFlight(String departureAirportCode, String arivalAirportCode, String departDate, String returnDate, String adult, String child, String infant) {
        Vector<ResSearchFlight> dataRespone = new Vector<>();
        sBuild.setLength(0);
        String param = sBuild.append("&d=").append(departureAirportCode).append("&a=").append(arivalAirportCode).append("&date=").append(departDate).append("&ret_date=").append(returnDate).append("&adult=")
                .append(adult)
                .append("&child=").append(child).append("&infant=").append(infant).append("&token=" + StaticParam.TIKET_COM_TOKEN).append("&v=3").append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "search/flight?" + param);
        JSONObject root = new JSONObject(respone);
        String iagnostic = root.getJSONObject("diagnostic").toString();
        JSONObject status = new JSONObject(iagnostic);
        Boolean roundTrip = root.getBoolean("round_trip");
        ResSearchFlight flight = new ResSearchFlight();
        if (status.get("confirm").equals("success")) {
            if (roundTrip) {
                flight.setSearchQueries(root.getJSONObject("search_queries").toString());
                flight.setGoDate(root.getJSONObject("go_det").toString());
                flight.setRetDate(root.getJSONObject("ret_det").toString());
                flight.setDepartures(root.getJSONObject("departures").toString());
                flight.setNearbyGoDate(root.getJSONObject("nearby_go_date").toString());
                flight.setNearbyRetDate(root.getJSONObject("nearby_ret_date").toString());
                flight.setStatus(iagnostic);
                dataRespone.add(flight);
                return dataRespone;
            } else {
                flight.setSearchQueries(root.getJSONObject("search_queries").toString());
                flight.setGoDate(root.getJSONObject("go_det").toString());
                flight.setNearbyGoDate(root.getJSONObject("nearby_go_date").toString());
                flight.setDepartures(root.getJSONObject("departures").toString());
                flight.setStatus(iagnostic);
                dataRespone.add(flight);
                return dataRespone;
            }

        } else {
            flight.setStatus(iagnostic);
            dataRespone.add(flight);
            return dataRespone;
        }

    }

    public String getNearestAirport(String ip) {
        sBuild.setLength(0);
        String param = sBuild.append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&ip=" + ip).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "search/flight?" + param);
        JSONObject root = new JSONObject(respone);
        String dataDeparture = root.getJSONObject("nearest_airport").toString();
        JSONObject daparture = new JSONObject(dataDeparture);
        JSONArray dataArray = daparture.getJSONArray("airport");
        return dataArray.toString();

    }

    public String searchAirport() {
        sBuild.setLength(0);
        String param = sBuild.append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "flight_api/all_airport?" + param);
        JSONObject root = new JSONObject(respone);
        String dataDeparture = root.getJSONObject("diagnostic").toString();
        JSONObject status = new JSONObject(dataDeparture);
        System.out.println();
        if (status.get("confirm").equals("success")) {
            return root.getJSONObject("all_airport").toString();
        } else {
            return dataDeparture;
        }
    }

    public String getPopularAirport(String depart) {
        sBuild.setLength(0);
        String param = sBuild.append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&depart=").append(depart).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "flight_api/getPopularDestination?" + param);
        JSONObject root = new JSONObject(respone);
        String dataDeparture = root.getJSONObject("diagnostic").toString();
        JSONObject status = new JSONObject(dataDeparture);
        System.out.println();
        if (status.get("confirm").equals("success")) {
            return root.getJSONObject("popular_destinations").toString();
        } else {
            return dataDeparture;
        }
    }

    public String checkUpdate(String departureAirportCode, String arivalAirportCode, String departDate, String adult, String child, String infant, String time) {
        sBuild.setLength(0);
        String param = sBuild.append("&d=" + departureAirportCode).append("&a=" + arivalAirportCode).append("&date=" + departDate).append("&adult=" + adult).append("&child=" + child).append("&infant=" + infant)
                .append("&time=" + time).append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "ajax/mCheckFlightUpdated?" + param);
        JSONObject root = new JSONObject(respone);
        String dataDeparture = root.getJSONObject("diagnostic").toString();
        return dataDeparture;
    }

    public String flighAddtOrder(ReqFlightAddOrder addOrder) {
        sBuild.setLength(0);
        String param = sBuild.append("&flight_id=" + addOrder.getFlightId()).append("&ret_flight_id=" + addOrder.getRetFlightId()).append("&lioncaptcha=" + addOrder.getLionCaptcha()).append("&lionsessionid=" + addOrder.getLionSessionId()).append("&child=" + addOrder.getChild())
                .append("&adult=" + addOrder.getAdult()).append("&conSalutation=" + addOrder.getConSolutation()).append("&conFirstName=" + addOrder.getConFirstName()).append("&conLastName=" + addOrder.getConLastName()).append("&conPhone=" + addOrder.getConPhone()).append("&conEmailAddress=" + addOrder.getConEmailAddress())
                .append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "order?" + param);
        JSONObject root = new JSONObject(respone);
        String diagnotic = root.getJSONObject("diagnostic").toString();
        JSONObject status = new JSONObject(diagnotic);
        System.out.println();
        if (status.get("confirm").equals("success")) {
            return root.getJSONObject("myorder").toString();
        } else {
            return diagnotic;
        }
    }

    public String flightOrder() {
        sBuild.setLength(0);
        String param = sBuild.append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "order?" + param);
        JSONObject root = new JSONObject(respone);
        String diagnotic = root.getJSONObject("diagnostic").toString();
        JSONObject status = new JSONObject(diagnotic);
        System.out.println();
        if (status.get("confirm").equals("success")) {
            return root.getJSONObject("myorder").toString();
        } else {
            return diagnotic;
        }
    }

    public String deleteOrder(String orderDetailID) {
        sBuild.setLength(0);
        String param = sBuild.append("&order_detail_id=").append(orderDetailID).append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "order/delete_order?" + param);
        JSONObject root = new JSONObject(respone);
        String diagnotic = root.getJSONObject("diagnostic").toString();
        return diagnotic;

    }

    public String checkOut() {
        sBuild.setLength(0);
        String param = sBuild.append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "order/checkout/20604252/IDR?" + param);
        JSONObject root = new JSONObject(respone);
        String diagnotic = root.getJSONObject("diagnostic").toString();
        JSONObject status = new JSONObject(diagnotic);
        return diagnotic;
    }

    public String checkOutPayment() {
        sBuild.setLength(0);
        String param = sBuild.append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "checkout/checkout_payment?" + param);
        JSONObject root = new JSONObject(respone);
        String diagnotic = root.getJSONObject("diagnostic").toString();
        JSONObject status = new JSONObject(diagnotic);
        return diagnotic;
    }

//    HOTEl API
    public String searchHotel(String location, String starDate, String night, String endate, String room, String adult, String child, String sort, String minprice, String macprice, String minstart, String maxstar, String latitude, String longitude) {
        sBuild.setLength(0);
        String param = sBuild.append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&q=" + location).append("&startdate=" + starDate).append("&night=" + night).
                append("&enddate=" + endate).append("&room=" + room).append("&adult=" + adult).append("&child=" + child).append("&sort=" + sort).append("&minprice=" + minprice).
                append("&maxprice=" + macprice).append("&minstart=" + minstart).append("&maxstart=" + maxstar).append("&latitude=" + latitude).append("&longitude=" + longitude).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "search/hotel?" + param);
        JSONObject root = new JSONObject(respone);
        String dataDeparture = root.getJSONObject("results").toString();
        JSONObject daparture = new JSONObject(dataDeparture);
        JSONArray dataArray = daparture.getJSONArray("result");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject airport = dataArray.getJSONObject(i);
            System.out.println(airport);
//            System.out.println(airport.getString("code"));
//            System.out.println(airport.getString("name"));
//            System.out.println(airport.getString("group"));
        }
        return dataArray.toString();

    }

    public String searchHotelByArea(String cityCode) {
        sBuild.setLength(0);
        String param = sBuild.append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&uid=" + cityCode).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "search/search_area?" + param);
        JSONObject root = new JSONObject(respone);
        String dataDeparture = root.getJSONObject("results").toString();
        JSONObject daparture = new JSONObject(dataDeparture);
        JSONArray dataArray = daparture.getJSONArray("result");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject airport = dataArray.getJSONObject(i);
            System.out.println(airport);
//            System.out.println(airport.getString("code"));
//            System.out.println(airport.getString("name"));
//            System.out.println(airport.getString("group"));
        }
        return dataArray.toString();

    }

    public String searchHotelByName(String name) {
        sBuild.setLength(0);
        String param = sBuild.append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&q=" + name).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "search/autocomplete/hotel?" + param);
        JSONObject root = new JSONObject(respone);
        String dataDeparture = root.getJSONObject("results").toString();
        JSONObject daparture = new JSONObject(dataDeparture);
        JSONArray dataArray = daparture.getJSONArray("result");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject airport = dataArray.getJSONObject(i);
            System.out.println(airport);
//            System.out.println(airport.getString("code"));
//            System.out.println(airport.getString("name"));
//            System.out.println(airport.getString("group"));
        }
        return dataArray.toString();

    }

    public String searchHotelPromo() {
        sBuild.setLength(0);
        String param = sBuild.append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "home/hotelDeals?" + param);
        JSONObject root = new JSONObject(respone);
        String dataDeparture = root.getJSONObject("results").toString();
        JSONObject daparture = new JSONObject(dataDeparture);
        JSONArray dataArray = daparture.getJSONArray("result");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject airport = dataArray.getJSONObject(i);
            System.out.println(airport);
//            System.out.println(airport.getString("code"));
//            System.out.println(airport.getString("name"));
//            System.out.println(airport.getString("group"));
        }
        return dataArray.toString();

    }

    public String searchHotelAddOrder(String starDate, String night, String endate, String room, String adult, String child, String minprice, String maxprice, String minstart, String maxstart, String hotelName, String roomId, String hasPromo) {
        sBuild.setLength(0);
        String param = sBuild.append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&startdate=").append(starDate).append("&night=").append(night).append("&enddate=").
                append(endate).
                append("&room=").append(room).append("&adult=").append(adult).append("&child=").append(child).append("&minprice=").append(minprice).append("&maxprice=").
                append(maxprice).
                append("&minstart=").append(minstart).append("&maxstart=").append(maxstart).append("&hotelname=").append(hotelName).append("&room_id=").append(roomId).append("&hasPromo=").append(hasPromo).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "order/add/hotel?" + param);
        JSONObject root = new JSONObject(respone);
        String dataDeparture = root.getJSONObject("diagnostic").toString();
        JSONObject status = new JSONObject(dataDeparture);
        if (status.get("status").equals("200")) {
            return root.getJSONObject("order").toString();
        } else {
            return dataDeparture;
        }
    }

    public String searchHotelOrder() {
        sBuild.setLength(0);
        String param = sBuild.append("&token=").append(StaticParam.TIKET_COM_TOKEN).append("&output=json").toString();
        String respone = conn.serviceGet(BASE_URL + "order?" + param);
        JSONObject root = new JSONObject(respone);
        String dataDeparture = root.getJSONObject("myorder").toString();
        JSONObject daparture = new JSONObject(dataDeparture);
        JSONArray dataArray = daparture.getJSONArray("data");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject airport = dataArray.getJSONObject(i);
            System.out.println(airport);
//            System.out.println(airport.getString("code"));
//            System.out.println(airport.getString("name"));
//            System.out.println(airport.getString("group"));
        }
        return dataArray.toString();

    }
}
