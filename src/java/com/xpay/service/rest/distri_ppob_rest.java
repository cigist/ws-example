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
import com.cigist.framework.util.CigistEncrypDecryp;
import com.xpay.dao.MstDistriPpobDao;
import com.xpay.dao.AuthenticationMitra;
import com.xpay.model.MstDistriPpob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


@Path("/xpay.ppob")
public class distri_ppob_rest {

    MstDistriPpobDao ppobDao = new MstDistriPpobDao();
    AuthenticationMitra auth = new AuthenticationMitra();
    Map result = new HashMap();
    

    @POST
    @Path("/all")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll(@FormParam("xuid") String uid,
            @FormParam("xpass") String xpass,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {

//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(uid, pass);
        try {
            if (valid && !uid.isEmpty() && !xpass.isEmpty()) {
                List<MstDistriPpob> list = new ArrayList<>();
                list = ppobDao.getAll(uid);
                if (!list.isEmpty()) {
                    result.put("DATA", list);
                    result.put("STATUS", "OK");
                } else {
                    result.put("MESSAGE", "Request tidak dapat di prosess");
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

    @POST
    @Path("/provider")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByprovider(@FormParam("xuid") String uid,
            @FormParam("xpass") String xpass, @FormParam("xproviderid") String providerId,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {

        //        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(uid, pass);
        try {
            if (valid && !uid.isEmpty() && !xpass.isEmpty()) {
                List<MstDistriPpob> list = new ArrayList<>();
                list = ppobDao.getByProviderId(uid, providerId);
                if (!list.isEmpty()) {
                    result.put("DATA", list);
                    result.put("STATUS", "OK");
                } else {
                    result.put("MESSAGE", "Request tidak dapat di prosess");
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

    @POST
    @Path("/type")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByProductType(@FormParam("xuid") String uid,
            @FormParam("xpass") String xpass, @FormParam("xproviderid") String providerId, @FormParam("xproducttype") String productType,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
        //        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(uid, pass);
        try {
            if (valid && !uid.isEmpty() && !xpass.isEmpty()) {
                List<MstDistriPpob> list = new ArrayList<>();
                list = ppobDao.getByProductType(uid, providerId, productType);
                if (!list.isEmpty()) {
                    result.put("DATA", list);
                    result.put("STATUS", "OK");
                } else {
                    result.put("MESSAGE", "Request tidak dapat di prosess");
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
      @POST
    @Path("/trntype")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByProductTrnType(@FormParam("xuid") String uid,
            @FormParam("xtrntype") String trnType,
            @FormParam("xpass") String xpass,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
        //        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(uid, pass);
        try {
            if (valid && !uid.isEmpty() && !xpass.isEmpty()) {
                List<MstDistriPpob> list = new ArrayList<>();
                list = ppobDao.getByProductTrnType(uid,trnType);
                if (!list.isEmpty()) {
                    result.put("DATA", list);
                    result.put("STATUS", "OK");
                } else {
                    result.put("MESSAGE", "Request tidak dapat di prosess");
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
