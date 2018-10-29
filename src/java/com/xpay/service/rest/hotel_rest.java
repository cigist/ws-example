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
import com.xpay.api.vendor.TiketDotComAPI;
import com.xpay.dao.MstDistriPpobDao;
import com.xpay.dao.AuthenticationMitra;
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

@Path("/xpay.hotel")
public class hotel_rest {

    MstDistriPpobDao ppobDao = new MstDistriPpobDao();
    AuthenticationMitra auth = new AuthenticationMitra();
    Map result = new HashMap();
    CigistStringFormat st = new CigistStringFormat();
    String dateCode = CigistDateTimeNow.getDateTimeNow("yyMMdd");
    String trnCode = st.autoCode("");
    CigistHttpConn httpConn = new CigistHttpConn();
    TiketDotComAPI tiket = new TiketDotComAPI();

    @POST
    @Path("/search")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response searchHotel(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xlocation") String xlocation,
            @FormParam("xstartdate") String xstartdate,
            @FormParam("xnight") String xnigth,
            @FormParam("xenddate") String xendate,
            @FormParam("xroom") String xroom,
            @FormParam("xadult") String xadult,
            @FormParam("xchild") String xchild,
            @FormParam("xsort") String xsort,
            @FormParam("xminprice") String xminprice,
            @FormParam("xmaxprice") String xmaxprice,
            @FormParam("xminstart") String xminstart,
            @FormParam("xmaxstart") String xmaxstart,
            @FormParam("xlatitude") String xlatitude,
            @FormParam("xlongitude") String xlongitude,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if ( valid &&!xuid.isEmpty() && !xpass.isEmpty()) {
                String tiketRespone = tiket.searchHotel(xlocation, xstartdate, xnigth, xendate, xroom, xadult, xchild, xsort, xminprice, xmaxprice, xminstart, xmaxprice, xlatitude, xlongitude);
                if (tiketRespone != null) {
                    result.put("STATUS", "OK");
                    result.put("DATA", tiketRespone);
                } else {
                    result.put("STATUS", "ERROR");
                    result.put("DATA", "Data tidak dapat diproses!");
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

    @POST
    @Path("/searchByArea")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response searchByArea(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xcitycode") String xcitycode,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                Vector<TopUpPulsa> dataResponse = new Vector<>();
                String tiketRespone = tiket.searchHotelByArea(xcitycode);
                if (tiketRespone != null) {
                    result.put("STATUS", "OK");
                    result.put("DATA", tiketRespone);
                } else {
                    result.put("STATUS", "ERROR");
                    result.put("DATA", "Data tidak dapat diproses!");
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

    @POST
    @Path("/hotelPromo")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response hotelPromo(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xcitycode") String xcitycode,
            @Context HttpServletResponse servletResponse,
            @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                Vector<TopUpPulsa> dataResponse = new Vector<>();
                String tiketRespone = tiket.searchHotelPromo();
                if (tiketRespone != null) {
                    result.put("STATUS", "OK");
                    result.put("DATA", tiketRespone);
                } else {
                    result.put("STATUS", "ERROR");
                    result.put("DATA", "Data tidak dapat diproses!");
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

    @POST
    @Path("/searchAutocomplete")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response searchAutocomplete(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xname") String xname,
            @Context HttpServletResponse servletResponse,
            @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                String tiketRespone = tiket.searchHotelByName(xname);
                if (tiketRespone != null) {
                    result.put("STATUS", "OK");
                    result.put("DATA", tiketRespone);
                } else {
                    result.put("STATUS", "ERROR");
                    result.put("DATA", "Data tidak dapat diproses!");
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

    @POST
    @Path("/order")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response order(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xstartdate") String xstartdate,
            @FormParam("xnight") String xnigth,
            @FormParam("xenddate") String xendate,
            @FormParam("xroom") String xroom,
            @FormParam("xadult") String xadult,
            @FormParam("xsort") String xsort,
            @FormParam("xminprice") String xminprice,
            @FormParam("xmaxprice") String xmaxprice,
            @FormParam("xminstart") String xminstart,
            @FormParam("xmaxstart") String xmaxstart,
            @FormParam("xhotelname") String xhotelname,
            @FormParam("xroomid") String xroomid,
            @FormParam("xhaspromo") String xhaspromo,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                String tiketRespone = tiket.searchHotelAddOrder(xstartdate,xendate,xnigth, xroom, xadult, xuid, xminprice, xmaxprice, xminstart, xmaxstart, xhotelname, xroomid, xhaspromo);
                if (tiketRespone != null) {
                    result.put("STATUS", "OK");
                    result.put("DATA", tiketRespone);
                } else {
                    result.put("STATUS", "ERROR");
                    result.put("DATA", "Data tidak dapat diproses!");
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

    @POST
    @Path("/myOrder")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response myOrder(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (!xuid.isEmpty() && !xpass.isEmpty()) {
                String tiketRespone = tiket.searchHotelOrder();
                if (tiketRespone != null) {
                    result.put("STATUS", "OK");
                    result.put("DATA", tiketRespone);
                } else {
                    result.put("STATUS", "ERROR");
                    result.put("DATA", "Data tidak dapat diproses!");
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
