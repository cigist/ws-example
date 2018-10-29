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
import com.xpay.api.vendor.FastpayAPI;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/api/secekharga")
public class secekharga extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Cache-Control", "public");
        
        String namaOperator= request.getParameter("operator");

        Vector result=new Vector();
        result = FastpayAPI.checkPrice(namaOperator);

        out.println(result);
    }

}
