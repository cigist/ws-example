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
import com.xpay.vendor.model.InqueryTagihan;
import com.xpay.vendor.model.PaymentBpjs;
import com.xpay.vendor.model.PaymentTagihan;
import com.xpay.vendor.model.ResAsuransi;
import com.xpay.vendor.model.ResMultiFinance;
import com.xpay.vendor.model.ResPascabayar;
import com.xpay.vendor.model.ResPaymentPLNNonTaglist;
import com.xpay.vendor.model.ResPaymentPLNPascabayar;
import com.xpay.vendor.model.ResPaymentPLNPrabayar;
import com.xpay.vendor.model.ResPaymentProductTelkom;
import com.xpay.vendor.model.ResPaymentProductTv;
import com.xpay.vendor.model.ResPdam;
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

@Path("/xpay.payment")
public class payment_rest {

    MstDistriPpobDao ppobDao = new MstDistriPpobDao();
    AuthenticationMitra auth = new AuthenticationMitra();
    Map result = new HashMap();
    CigistStringFormat st = new CigistStringFormat();
    String dateCode = CigistDateTimeNow.getDateTimeNow("yyMMdd");
    String trnCode = st.autoCode("");
    CigistHttpConn httpConn = new CigistHttpConn();

    @POST
    @Path("/inquery")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response inquery(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xproductcode") String xproductcode,
            @FormParam("xidpelanggan1") String xidpelanggan1,
            @FormParam("xidpelanggan2") String xidpelanggan2,
            @FormParam("xidpelanggan3") String xidpelanggan3,
            @FormParam("xresponecode") String xresponecode,
            @Context HttpServletResponse servletResponse, @Context HttpServletRequest request) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        Double balance = auth.checkBalance(xuid, pass);
        int lengthProductCode = xproductcode.length();
        Double price = ppobDao.checkPrice(xuid, xproductcode);
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                if (xresponecode.equals("PLNPASCABAYAR".toUpperCase())) {
                    Vector<ResPaymentPLNPascabayar> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryPLNPascabayar(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, "INQ" + dateCode + trnCode);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
                        result.put("DATA", dataResponse);
                    } else {
                        result.put("STATUS", "ERROR");
                        result.put("DATA", "Data tidak dapat diproses!");
                    }
                } else if (xresponecode.equals("PLNPRABAYAR".toUpperCase())) {
                    Vector<ResPaymentPLNPrabayar> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryPLNPrabayar(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, "INQ" + dateCode + trnCode);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
                        result.put("DATA", dataResponse);
                    } else {
                        result.put("STATUS", "ERROR");
                        result.put("DATA", "Data tidak dapat diproses!");
                    }
                } else if (xresponecode.equals("NONTAGLIST".toUpperCase())) {
                    Vector<ResPaymentPLNNonTaglist> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryPlnNorTaglist(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, "INQ" + dateCode + trnCode);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
                        result.put("DATA", dataResponse);
                    } else {
                        result.put("STATUS", "ERROR");
                        result.put("DATA", "Data tidak dapat diproses!");
                    }
                } else if (xresponecode.equals("TELEPON".toUpperCase())) {
                    Vector<ResPaymentProductTelkom> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryProductTelkom(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, "INQ" + dateCode + trnCode);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
                        result.put("DATA", dataResponse);
                    } else {
                        result.put("STATUS", "ERROR");
                        result.put("DATA", "Data tidak dapat diproses!");
                    }
                } else if (xresponecode.equals("PASCABAYAR".toUpperCase())) {
                    Vector<ResPascabayar> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryPascabayar(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, "INQ" + dateCode + trnCode);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
                        result.put("DATA", dataResponse);
                    } else {
                        result.put("STATUS", "ERROR");
                        result.put("DATA", "Data tidak dapat diproses!");
                    }
                } else if (xresponecode.equals("TV".toUpperCase())) {
                    Vector<ResPaymentProductTv> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryProductTv(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, "INQ" + dateCode + trnCode);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
                        result.put("DATA", dataResponse);
                    } else {
                        result.put("STATUS", "ERROR");
                        result.put("DATA", "Data tidak dapat diproses!");
                    }
                } else if (xresponecode.equals("FINANCE".toUpperCase())) {
                    Vector<ResMultiFinance> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryMultifinance(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, "INQ" + dateCode + trnCode);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
                        result.put("DATA", dataResponse);
                    } else {
                        result.put("STATUS", "ERROR");
                        result.put("DATA", "Data tidak dapat diproses!");
                    }
                } else if (xresponecode.equals("PDAM".toUpperCase())) {
                    Vector<ResPdam> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryPdam(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, "INQ" + dateCode + trnCode);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
                        result.put("DATA", dataResponse);
                    } else {
                        result.put("STATUS", "ERROR");
                        result.put("DATA", "Data tidak dapat diproses!");
                    }
                } else if (xresponecode.equals("POLIS".toUpperCase())) {
                    Vector<ResAsuransi> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryAsuransi(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, "INQ" + dateCode + trnCode);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
                        result.put("DATA", dataResponse);
                    } else {
                        result.put("STATUS", "ERROR");
                        result.put("DATA", "Data tidak dapat diproses!");
                    }
                } else if (xresponecode.equals("PAKET".toUpperCase())) {
                    Vector<ResAsuransi> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryAsuransi(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, "INQ" + dateCode + trnCode);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
                        result.put("DATA", dataResponse);
                    } else {
                        result.put("STATUS", "ERROR");
                        result.put("DATA", "Data tidak dapat diproses!");
                    }
                } else {
                    Vector<InqueryTagihan> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryTagihan(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, "INQ" + dateCode + trnCode);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
                        result.put("DATA", dataResponse);
                    } else {
                        result.put("STATUS", "ERROR");
                        result.put("DATA", "Data tidak dapat diproses!");
                    }
                }
//                if (balance > price) {

//                } else {
//                    result.put("MESSAGE", "Saldo anda tidak mencukupi, silahkan isi ulang saldo anda");
//                    result.put("STATUS", "ERROR");
//                }
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
    @Path("/payment")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response payment(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xproductcode") String xproductcode,
            @FormParam("xidpelanggan1") String xidpelanggan1,
            @FormParam("xidpelanggan2") String xidpelanggan2,
            @FormParam("xidpelanggan3") String xidpelanggan3,
            @FormParam("xnominal") String xnominal,
            @FormParam("xref1") String xref1,
            @FormParam("xref2") String xref2,
            @FormParam("xref3") String xref3,
            @FormParam("xresponecode") String xresponecode,
            @Context HttpServletResponse servletResponse,
            @Context HttpServletRequest request
    ) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        Double balance = auth.checkBalance(xuid, pass);
        int lengthProductCode = xproductcode.length();
        Double price = ppobDao.checkPrice(xuid, xproductcode.substring(3, lengthProductCode));
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                if (balance > price) {
                    if (xresponecode.equals("PLNPASCABAYAR".toUpperCase())) {
                        Vector<ResPaymentPLNPascabayar> dataResponse = new Vector<>();
                        dataResponse = FastpayAPI.paymentPLNPascabayar(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                        if (dataResponse != null) {
                            result.put("STATUS", "OK");
                            result.put("DATA", dataResponse);
                        } else {
                            result.put("STATUS", "ERROR");
                            result.put("DATA", "Data tidak dapat diproses!");
                        }
                    } else if (xresponecode.equals("PLNPRABAYAR".toUpperCase())) {
                        Vector<ResPaymentPLNPrabayar> dataResponse = new Vector<>();
                        dataResponse = FastpayAPI.paymentPLNPrabayar(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                        if (dataResponse != null) {
                            result.put("STATUS", "OK");
                            result.put("DATA", dataResponse);
                        } else {
                            result.put("STATUS", "ERROR");
                            result.put("DATA", "Data tidak dapat diproses!");
                        }
                    } else if (xresponecode.equals("NONTAGLIST".toUpperCase())) {
                        Vector<ResPaymentPLNNonTaglist> dataResponse = new Vector<>();
                        dataResponse = FastpayAPI.paymentPLNNonTaglist(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                        if (dataResponse != null) {
                            result.put("STATUS", "OK");
                            result.put("DATA", dataResponse);
                        } else {
                            result.put("STATUS", "ERROR");
                            result.put("DATA", "Data tidak dapat diproses!");
                        }
                    } else if (xresponecode.equals("TELEPON".toUpperCase())) {
                        Vector<ResPaymentProductTelkom> dataResponse = new Vector<>();
                        dataResponse = FastpayAPI.paymentProductTelkom(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                        if (dataResponse != null) {
                            result.put("STATUS", "OK");
                            result.put("DATA", dataResponse);
                        } else {
                            result.put("STATUS", "ERROR");
                            result.put("DATA", "Data tidak dapat diproses!");
                        }
                    } else if (xresponecode.equals("PASCABAYAR".toUpperCase())) {
                        Vector<ResPascabayar> dataResponse = new Vector<>();
                        dataResponse = FastpayAPI.paymentPascabayar(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                        if (dataResponse != null) {
                            result.put("STATUS", "OK");
                            result.put("DATA", dataResponse);
                        } else {
                            result.put("STATUS", "ERROR");
                            result.put("DATA", "Data tidak dapat diproses!");
                        }
                    } else if (xresponecode.equals("TV".toUpperCase())) {
                        Vector<ResPaymentProductTv> dataResponse = new Vector<>();
                        dataResponse = FastpayAPI.paymentProductTv(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                        if (dataResponse != null) {
                            result.put("STATUS", "OK");
                            result.put("DATA", dataResponse);
                        } else {
                            result.put("STATUS", "ERROR");
                            result.put("DATA", "Data tidak dapat diproses!");
                        }
                    } else if (xresponecode.equals("FINANCE".toUpperCase())) {
                        Vector<ResMultiFinance> dataResponse = new Vector<>();
                        dataResponse = FastpayAPI.paymentMutliFinance(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                        if (dataResponse != null) {
                            result.put("STATUS", "OK");
                            result.put("DATA", dataResponse);
                        } else {
                            result.put("STATUS", "ERROR");
                            result.put("DATA", "Data tidak dapat diproses!");
                        }
                    } else if (xresponecode.equals("PDAM".toUpperCase())) {
                        Vector<ResPdam> dataResponse = new Vector<>();
                        dataResponse = FastpayAPI.paymentPdam(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                        if (dataResponse != null) {
                            result.put("STATUS", "OK");
                            result.put("DATA", dataResponse);
                        } else {
                            result.put("STATUS", "ERROR");
                            result.put("DATA", "Data tidak dapat diproses!");
                        }
                    } else if (xresponecode.equals("POLIS".toUpperCase())) {
                        Vector<ResAsuransi> dataResponse = new Vector<>();
                        dataResponse = FastpayAPI.paymentAsuransi(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                        if (dataResponse != null) {
                            result.put("STATUS", "OK");
                            result.put("DATA", dataResponse);
                        } else {
                            result.put("STATUS", "ERROR");
                            result.put("DATA", "Data tidak dapat diproses!");
                        }
                    } else if (xresponecode.equals("PAKET".toUpperCase())) {
                        Vector<ResAsuransi> dataResponse = new Vector<>();
                        dataResponse = FastpayAPI.paymentAsuransi(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                        if (dataResponse != null) {
                            result.put("STATUS", "OK");
                            result.put("DATA", dataResponse);
                        } else {
                            result.put("STATUS", "ERROR");
                            result.put("DATA", "Data tidak dapat diproses!");
                        }
                    } else {
                        Vector<PaymentTagihan> dataResponse = new Vector<>();
                        dataResponse = FastpayAPI.paymentTagihan(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                        if (dataResponse != null) {
                            result.put("STATUS", "OK");
                            result.put("DATA", dataResponse);
                        } else {
                            result.put("STATUS", "ERROR");
                            result.put("DATA", "Data tidak dapat diproses!");
                        }
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

    @POST
    @Path("/paybintang")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response paymentAsuransiBintang(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xproductcode") String xproductcode,
            @FormParam("xidpelanggan1") String xidpelanggan1,
            @FormParam("xidpelanggan2") String xidpelanggan2,
            @FormParam("xidpelanggan3") String xidpelanggan3,
            @FormParam("xnominal") String xnominal,
            @FormParam("xref1") String xref1,
            @FormParam("xref2") String xref2,
            @FormParam("xref3") String xref3,
            @Context HttpServletResponse servletResponse,
            @Context HttpServletRequest request
    ) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        Double balance = auth.checkBalance(xuid, pass);
        int lengthProductCode = xproductcode.length();
        Double price = ppobDao.checkPrice(xuid, xproductcode.substring(3, lengthProductCode));
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                if (balance > price) {
                    Vector<PaymentTagihan> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.paymentAsuransiBintang(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
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

    @POST
    @Path("/bintanginq")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response inqueryAsuransiBintang(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xproductcode") String xproductcode,
            @FormParam("xidpelanggan1") String xidpelanggan1,
            @FormParam("xidpelanggan2") String xidpelanggan2,
            @FormParam("xidpelanggan3") String xidpelanggan3,
            @FormParam("xnominal") String xnominal,
            @FormParam("xref1") String xref1,
            @FormParam("xref2") String xref2,
            @FormParam("xref3") String xref3,
            @Context HttpServletResponse servletResponse,
            @Context HttpServletRequest request
    ) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        Double balance = auth.checkBalance(xuid, pass);
        int lengthProductCode = xproductcode.length();
        Double price = ppobDao.checkPrice(xuid, xproductcode.substring(3, lengthProductCode));
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                if (balance > price) {
                    Vector<InqueryTagihan> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryTagihanAsuransiBintang(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xidpelanggan2, xidpelanggan3, xnominal, xref1, xref2, xref3);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
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

    @POST
    @Path("/equityinq")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response inqueryAsuransEquety(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xproductcode") String xproductcode,
            @FormParam("xidpelanggan1") String xidpelanggan1,
            @FormParam("xnamatertanggung") String xnamatertanggung,
            @FormParam("xnominal") String xnominal,
            @FormParam("xtanggallahirtertanggung") String xtanggallahirtertanggung,
            @FormParam("xtanggalberangkat") String xtanggalberangkat,
            @FormParam("xtelepontertanggung") String xtelepontertanggung,
            @FormParam("xteleponahliwaris") String xteleponahliwaris,
            @FormParam("xref1") String xref1,
            @Context HttpServletResponse servletResponse,
            @Context HttpServletRequest request
    ) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        Double balance = auth.checkBalance(xuid, pass);
        int lengthProductCode = xproductcode.length();
        Double price = ppobDao.checkPrice(xuid, xproductcode.substring(3, lengthProductCode));
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                if (balance > price) {
                    Vector<InqueryTagihan> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryTagihanEquity(xproductcode.substring(3, lengthProductCode), xidpelanggan1, xnamatertanggung, xnominal, xtanggallahirtertanggung, xtanggalberangkat, xtelepontertanggung, xteleponahliwaris,
                            xref1);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
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

    @POST
    @Path("/bpjsinq")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response bpjsInq(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xproductcode") String xproductcode,
            @FormParam("xidpelanggan") String xidpelanggan,
            @FormParam("xperiodebulan") String xperiodebulan,
            @FormParam("xref") String xref,
            @Context HttpServletResponse servletResponse,
            @Context HttpServletRequest request
    ) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        Double balance = auth.checkBalance(xuid, pass);
        int lengthProductCode = xproductcode.length();
        Double price = ppobDao.checkPrice(xuid, xproductcode.substring(3, lengthProductCode));
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                if (balance > price) {
                    Vector<PaymentBpjs> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.inqueryTagihanBPJSKesehatan(xproductcode.substring(3, lengthProductCode), xidpelanggan, xperiodebulan, xref);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
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

    @POST
    @Path("/paybpjs")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response bpjsPayment(@FormParam("xuid") String xuid,
            @FormParam("xpass") String xpass,
            @FormParam("xproductcode") String xproductcode,
            @FormParam("xidpelanggan") String xidpelanggan,
            @FormParam("xperiodebulan") String xperiodebulan,
            @FormParam("xnominal") String xnominal,
            @FormParam("xphonenumber") String xphonenumber,
            @FormParam("xref1") String xref1,
            @FormParam("xref2") String xref2,
            @Context HttpServletResponse servletResponse,
            @Context HttpServletRequest request
    ) {
//        validate mitra account
        String pass = CigistEncrypDecryp.encrypt(xpass);
        Boolean valid = auth.validateAccountMitra(xuid, pass);
        Double balance = auth.checkBalance(xuid, pass);
        int lengthProductCode = xproductcode.length();
        Double price = ppobDao.checkPrice(xuid, xproductcode.substring(3, lengthProductCode));
        try {
            if (valid && !xuid.isEmpty() && !xpass.isEmpty()) {
                if (balance > price) {
                    Vector<PaymentBpjs> dataResponse = new Vector<>();
                    dataResponse = FastpayAPI.paymentTagihanBPJSKesehatan(xproductcode.substring(3, lengthProductCode), xidpelanggan, xperiodebulan, xnominal, xphonenumber, xref1, xref2);
                    if (dataResponse != null) {
                        result.put("STATUS", "OK");
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
