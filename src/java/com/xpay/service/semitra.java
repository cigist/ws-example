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
import com.cigist.framework.util.CigistAutoGenerate;
import com.cigist.framework.util.CigistDateTimeNow;
import com.cigist.framework.util.CigistEncrypDecryp;
import com.cigist.framework.util.CigistStringFormat;
import com.xpay.dao.MstMitraDao;
import com.xpay.model.MstMitra;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/api/semitra")
public class semitra extends HttpServlet {

    MstMitraDao mitraDao = new MstMitraDao();
    String result = null;
    Boolean status = false;
    CigistStringFormat autocode = new CigistStringFormat();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Cache-Control", "public");

        String action = request.getParameter("xaction").toLowerCase();
        String username = request.getParameter("username");
        String token = request.getParameter("token");

        if (action.equals("list")) {

        } else {
            MstMitra mitra = new MstMitra();
            mitra.setMitraCode(autocode.autoCode("XPM"));
            mitra.setUsername(request.getParameter("xusername").trim());
            mitra.setPassword(CigistEncrypDecryp.encrypt(request.getParameter("xpassword").trim()));
            mitra.setFlagActive(request.getParameter("xflagactive"));
            mitra.setUserCreate(username);
            mitra.setDateCreate(Date.valueOf(CigistDateTimeNow.getDateTimeNow("yyyy-MM-dd")));
            mitra.setTimeCreate(Time.valueOf(CigistDateTimeNow.getDateTimeNow("hh:mm:ss")));
            mitra.setUserUpdate(username);
            mitra.setDateUpdate(Date.valueOf(CigistDateTimeNow.getDateTimeNow("yyyy-MM-dd")));
            mitra.setTimeUpdate(Time.valueOf(CigistDateTimeNow.getDateTimeNow("hh:mm:ss")));
            status = mitraDao.insertUpdate(mitra);
        }

        if (status) {
            result = "OK";
        } else {
            result = "ERROR";
        }
        out.println(result);
    }

}
