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
import com.xpay.dao.MstPpobDao;
import com.xpay.dao.AuthenticationMitra;
import com.xpay.dao.MstDistriPpobDao;
import com.xpay.model.MstDistriPpob;
import com.xpay.model.MstPpobPrice;
import com.xpay.vendor.model.TopUpPulsa;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(urlPatterns = "/api/semstppob")
public class semstppob extends HttpServlet {

    AuthenticationMitra vMitra = new AuthenticationMitra();
    MstDistriPpobDao ppobDao = new MstDistriPpobDao();
    JSONObject result = new JSONObject();
    JSONArray data = new JSONArray();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Cache-Control", "public");

        String uid = request.getParameter("xuid");
        String password = CigistEncrypDecryp.encrypt(request.getParameter("xpass"));
        boolean authMitra = vMitra.validateAccountMitra(uid, password);

        if (authMitra && !uid.isEmpty() && !password.isEmpty()) {
            List<MstDistriPpob> list = new ArrayList<>();
            list = ppobDao.getAll(uid);

            result.put("STATUS", "OK");;
            result.put("DATA", list);
        } else {
            result.put("STATUS", "ERROR");;
            result.put("DATA", "UID or Password not valid !");
        }

        out.println(result);
    }

}
