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
import com.xpay.api.vendor.FastpayAPI;
import com.xpay.dao.AuthenticationMitra;
import com.xpay.vendor.model.InqueryTagihan;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(urlPatterns = "/api/secektagihan")
public class secektagihan extends HttpServlet {

    AuthenticationMitra vMitra = new AuthenticationMitra();
    JSONObject result = new JSONObject();
    JSONArray data = new JSONArray();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Cache-Control", "public");

        String username = request.getParameter("xuid");
        String password = CigistEncrypDecryp.encrypt(request.getParameter("xpass"));
        boolean authMitra = vMitra.validateAccountMitra(username, password);

        if (authMitra && !username.isEmpty() && !password.isEmpty()) {
            String produkcode = request.getParameter("xprodukcode");
            String idpelanggan1 = request.getParameter("xidpelanggan1");
            String idpelanggan2 = request.getParameter("xidpelanggan2");
            String idpelanggan3 = request.getParameter("xidpelanggan3");
            String ref1 = request.getParameter("xref");

            Vector<InqueryTagihan> dataResponse = new Vector<>();
            dataResponse = FastpayAPI.inqueryTagihan(produkcode, idpelanggan1, idpelanggan2, idpelanggan3, ref1);

            if (produkcode.isEmpty() | idpelanggan1.isEmpty() | idpelanggan2.isEmpty() | idpelanggan3.isEmpty() | ref1.isEmpty()) {
                result.put("STATUS", "ERORR");;
                result.put("DATA", "Data tidak dapat diproses!");
            } else {
                if (dataResponse != null) {
                    result.put("STATUS", "OK");;
                    result.put("DATA", dataResponse);
                } else {
                    result.put("STATUS", "ERORR");;
                    result.put("DATA", "Data tidak dapat diproses!");
                }
            }
            System.out.println(produkcode + " " + idpelanggan1 + " " + idpelanggan2 + " " + idpelanggan3 + " " + ref1);

        } else {
            result.put("STATUS", "ERROR");;
            result.put("DATA", "Username dan Password anda tidak valid !");
        }

        out.println(result);
    }

}
