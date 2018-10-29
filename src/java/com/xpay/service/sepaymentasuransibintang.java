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
import com.xpay.vendor.model.PaymentTagihan;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(urlPatterns = "/api/sepaymentasuransibintang")
public class sepaymentasuransibintang extends HttpServlet {

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
            String idPelanggan1 = request.getParameter("xidpelanggan1");
            String idPelanggan2 ="0";
            String idPelanggan3 = "0";
            String nominal = request.getParameter("xnominal");
            String ref1 = request.getParameter("xref1");
            String ref2 = request.getParameter("xref2");
            String ref3 = request.getParameter("xref3");
            Vector<PaymentTagihan> dataResponse = new Vector<>();
            dataResponse = FastpayAPI.paymentAsuransiBintang(produkcode, idPelanggan1, idPelanggan2, idPelanggan3, nominal, ref1, ref2, ref3);

            result.put("STATUS", "OK");;
            result.put("DATA", dataResponse);
        } else {
            result.put("STATUS", "ERROR");;
            result.put("DATA", "Username dan Password anda tidak valid !");
        }

        out.println(result);
    }

}
