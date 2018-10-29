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
import com.xpay.vendor.model.ResSearchFlight;
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

@Path("/xpay.flight")
public class flight_rest {

    MstDistriPpobDao ppobDao = new MstDistriPpobDao();
    AuthenticationMitra auth = new AuthenticationMitra();
    Map result = new HashMap();
    CigistStringFormat st = new CigistStringFormat();
    String dateCode = CigistDateTimeNow.getDateTimeNow("yyMMdd");
    String trnCode = st.autoCode("");
    CigistHttpConn httpConn = new CigistHttpConn();
    FastpayAPI fastFay = new FastpayAPI();
    TiketDotComAPI tiket = new TiketDotComAPI();

    @POST
    @Path("/search")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response searchFlight(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xdepartureairportcode") String xdepartureairportcode,
            @FormParam("xarrivalairportcode") String xarrivalairportcode,
            @FormParam("xdatedeparture") String xdatedeparture,
            @FormParam("xdatereturn") String xdatereturn,
            @FormParam("xadult") String xadult,
            @FormParam("xchild") String xchild,
            @FormParam("xinfant") String xinfant,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                Vector<ResSearchFlight> dataResponse = new Vector<>();
                dataResponse= tiket.searchFlight(xdepartureairportcode, xarrivalairportcode, xdatedeparture, xdatereturn, xadult, xchild, xinfant);
                if (dataResponse != null) {
                    result.put("STATUS", "OK");
                    result.put("DATA", dataResponse);
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
    @Path("/getNearestAirport")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getNearestAirport(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                Vector<TopUpPulsa> dataResponse = new Vector<>();
                String tiketRespone = tiket.getNearestAirport(request.getRemoteHost());
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
    @Path("/searchAirport")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAirport(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                String tiketRespone = tiket.searchAirport();
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
    @Path("/popularAirport")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPopularAirport(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xdepart") String xdepart,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                String tiketRespone = tiket.getPopularAirport(xdepart);
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
    @Path("/checkUpdate")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response chechkUpdate(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xdepartureairportcode") String xdepartureairportcode,
            @FormParam("xarrivalairportcode") String xarrivalairportcode,
            @FormParam("xdatedeparture") String xdatedeparture,
            @FormParam("xadult") String xadult,
            @FormParam("xchild") String xchild,
            @FormParam("xinfant") String xinfant,
            @FormParam("xtimestamp") String xtimestamp,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                String tiketRespone = tiket.checkUpdate(xdepartureairportcode, xarrivalairportcode, xdatedeparture, xadult, xchild, xinfant, xtimestamp);
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
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                String tiketRespone = tiket.flightOrder();
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
    @Path("/delete")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteOrder(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xorderid") String xorderid,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                String tiketRespone = tiket.deleteOrder(xorderid);
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
    @Path("/checkOut")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response checkOut(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                String tiketRespone = tiket.checkOut();
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
    @Path("/checkOutPayment")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response checkOutPayment(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                String tiketRespone = tiket.checkOutPayment();
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
    @Path("/addOrder")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response flightAddOrder(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xdepart") String xdepart,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                String tiketRespone = tiket.getPopularAirport(xdepart);
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
