/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.service.rest;

/**
 *
 * @author Irwan Cigist <cigist.developer@gmail.com>
 */
import com.cigist.framework.conn.CigistHttpConn;
import com.cigist.framework.util.CigistDateTimeNow;
import com.cigist.framework.util.CigistEncrypDecryp;
import com.cigist.framework.util.CigistStringFormat;
import com.xpay.api.vendor.FastpayAPI;
import com.xpay.dao.MstDistriPpobDao;
import com.xpay.dao.AuthenticationMitra;
import com.xpay.model.MstDistriPpob;
import com.xpay.vendor.model.TopUpPulsa;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.http.HTTPException;

@Path("/xpay.pulsa")
public class pulsa_rest {

    MstDistriPpobDao ppobDao = new MstDistriPpobDao();
    AuthenticationMitra auth = new AuthenticationMitra();
    Map result = new HashMap();
    CigistStringFormat st = new CigistStringFormat();
    String dateCode = CigistDateTimeNow.getDateTimeNow("yyMMdd");
    String trnCode = st.autoCode("");
    CigistHttpConn httpConn = new CigistHttpConn();
    FastpayAPI fastFay = new FastpayAPI();

    @POST
    @Path("/topup")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response topupPulsa(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xproductcode") String xproductcode,
            @FormParam("xnomertelepon") String xnomertelepon,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        Double balance = null;
        Double price=null;
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        balance = auth.checkBalance(xuid, pass);
        int lengthProductCode = xproductcode.length();
        price = ppobDao.checkPrice(xuid, xproductcode);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                if (balance > price && price <balance) {
                    Vector<TopUpPulsa> dataResponse = new Vector<>();
                    String TrnNumber = "T04" + dateCode + trnCode;
                    dataResponse = fastFay.toUpPulsa(xproductcode.substring(3, lengthProductCode), xnomertelepon, TrnNumber, TrnNumber, xproductcode, xuid);
                    if (dataResponse != null) {
                        result.put("STATUS", "Ok");
                        result.put("DATA", dataResponse);
                    } else {
                        result.put("STATUS", "ERROR");
                        result.put("DATA", "Data tidak dapat diproses!");
                    }
                } else {
                    result.put("MESSAGE", "Saldo anda tidak mencukupi, silahkan isi ulang saldo anda");
                    result.put("STATUS", "ERROR");
                }
            } else {
                result.put("MESSAGE", "Invalid uid or password !");
                result.put("STATUS", "ERROR");
            }

            return Response.status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .header("Access-Control-Max-Age", "1209600")
                    .entity(result).build();
        } catch (EntityNotFoundException e) { // No data found
            throw new HTTPException(404);
        } catch (Exception e) { // Other exceptions
            throw new HTTPException(500);
        }
    }

}
