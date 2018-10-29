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
import com.xpay.dao.MstPpobDao;
import com.xpay.model.MstPpobPrice;
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

@Path("/ppob")
public class ppob_rest {

    MstPpobDao ppobDao = new MstPpobDao();
    Map result = new HashMap();


    @POST
    @Path("/all")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll(@FormParam("xuid") String uid,
            @FormParam("xpass") String xpass,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
        
        try {
            if (uid.toLowerCase().equals("xpay") && xpass.toLowerCase().equals("123")) {
                List<MstPpobPrice> list = new ArrayList<>();
                list = ppobDao.getAll();

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
