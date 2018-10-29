/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.service;

/**
 *
 * @author cigist
 */
import com.cigist.framework.util.CigistEncrypDecryp;
import com.xpay.api.vendor.FasTravelAPI;
import com.xpay.dao.AuthenticationMitra;
import com.xpay.vendor.model.ReqHotel;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(urlPatterns = "/api/sesearchhotel")
public class sesearchhotel extends HttpServlet {

    AuthenticationMitra vMitra = new AuthenticationMitra();
    JSONObject result = new JSONObject();
    JSONArray data = new JSONArray();
    FasTravelAPI api = new FasTravelAPI();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Cache-Control", "public");
//
        String username = request.getParameter("xuid");
        String password = CigistEncrypDecryp.encrypt(request.getParameter("xpass"));
        boolean authMitra = vMitra.validateAccountMitra(username, password);

        if ( authMitra && !username.isEmpty() && !password.isEmpty()) {
            ReqHotel hotel = new ReqHotel();
            hotel.setCityId(request.getParameter("xcityid"));
            hotel.setRoom(Integer.parseInt(request.getParameter("xroom")));
            hotel.setGuest(Integer.parseInt(request.getParameter("xguest")));
            hotel.setCheckInDate(request.getParameter("xcheckindate"));
            hotel.setCheckOutDate(request.getParameter("xcheckoutdate"));
            
            String dataResponse=api.searchHotel(hotel);
            result.put("STATUS", "OK");;
            result.put("DATA", dataResponse);
        } else {
            result.put("STATUS", "ERROR");;
            result.put("DATA", "Username dan Password anda tidak valid !");
        }

        out.println(result);
    }

}
