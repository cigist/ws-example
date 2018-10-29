/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.service;


import com.xpay.api.vendor.FasTravelAPI;
import com.xpay.util.StaticParam;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cigist
 */
@WebServlet(urlPatterns = "/api/seairport")
public class seairport extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Cache-Control", "public");

        FasTravelAPI api = new FasTravelAPI();
        String respone =api.getAirport("PESAWAT", StaticParam.FASFAY_TOKEN);
        
        out.write(respone);

    }
}
