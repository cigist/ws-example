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

@WebServlet(urlPatterns = "/api/seceksaldo")
public class seceksaldo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Cache-Control", "public");

        Vector result = FastpayAPI.checkBalance();

        out.println(result);
    }

}
