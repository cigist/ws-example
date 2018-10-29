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
import com.xpay.vendor.model.ReqFlight;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(urlPatterns = "/api/sesearchflight")
public class sesearchflight extends HttpServlet {

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

        if (authMitra && !username.isEmpty() && !password.isEmpty()) {
            ReqFlight flight = new ReqFlight();
            flight.setAirline(request.getParameter("xairline"));
            flight.setDeparture(request.getParameter("xdeparture"));
            flight.setArrival(request.getParameter("xarrival"));
            flight.setDepartureDate(request.getParameter("xdepartureDate"));
            flight.setReturnDate(request.getParameter("xreturnDate"));
            flight.setIsLowestPrice(Boolean.valueOf(request.getParameter("xisLowestPrice")));
            flight.setAdult(Integer.parseInt(request.getParameter("xadult")));
            flight.setChild(Integer.parseInt(request.getParameter("xchild")));
            flight.setInfant(Integer.parseInt(request.getParameter("xinfant")));
            String dataResponse = api.searchFlight(flight);
            if (dataResponse != null) {
                result.put("STATUS", "OK");
                result.put("DATA", dataResponse);
            }
        } else {
            result.put("STATUS", "ERROR");
            result.put("DATA", "Username dan Password anda tidak valid !");
        }

        out.println(result);
    }

}
