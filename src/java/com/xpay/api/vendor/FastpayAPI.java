/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.api.vendor;

import com.cigist.framework.util.CigistDateTimeNow;
import com.xpay.dao.MstDistriPpobDao;
import com.xpay.dao.MstMitraDao;
import com.xpay.dao.TrnPpobDao;
import com.xpay.model.TrnPpob;
import com.xpay.vendor.model.InqueryJawaPos;
import com.xpay.vendor.model.PaymentJawaPos;
import com.xpay.util.StaticParam;
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
import com.xpay.vendor.model.TopUpPulsa;
import com.xpay.vendor.model.VocuherGame;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

/**
 *
 * @author xmedia
 */
public class FastpayAPI {

    TrnPpobDao trnPpobDao = new TrnPpobDao();
    MstDistriPpobDao ppobDao = new MstDistriPpobDao();
    MstMitraDao mitraDao = new MstMitraDao();
    String dateCode = CigistDateTimeNow.getDateTimeNow("yyyy-MM-dd hh:mm:ss");

    public static Vector checkBalance() {
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();

            params.addElement(new String(UID));
            params.addElement(new String(PASSWORD));

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.balance", params);

            System.out.println(result.get(4));

            return result;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector checkPrice(String productCode) {
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(new String(productCode));
            params.addElement(new String(UID));
            params.addElement(new String(PASSWORD));

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.harga", params);
            result.iterator();
            result.get(4);
            System.out.println(result.get(4));

            return result;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public Vector<TopUpPulsa> toUpPulsa(String pulsaCode, String phoneNumber, String ref1, String trn_no, String producId, String distriId) {
        Vector<TopUpPulsa> returnTopUp = new Vector<>();
        Double price = null;
        Double saldo = null;
        Double endingBalance = null;
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(pulsaCode);
            params.addElement(phoneNumber);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.pulsa", params);
            if (result != null) {
                TopUpPulsa topUpPulsa = new TopUpPulsa();
                topUpPulsa.setProdukKode(result.get(0).toString());
                topUpPulsa.setWaktu(result.get(1).toString());
                topUpPulsa.setNoHP(result.get(2).toString());
                topUpPulsa.setSN(result.get(5).toString());
                topUpPulsa.setRef1(result.get(6).toString());
                topUpPulsa.setRef2(result.get(7).toString());
                topUpPulsa.setStatus(result.get(8).toString());
//                topUpPulsa.setKeterangan(result.get(9).toString());
                returnTopUp.add(topUpPulsa);

                price = ppobDao.checkPrice(distriId, producId);
//                CEK SALDO MITRA
                saldo = mitraDao.checkBalance(distriId);
                endingBalance = saldo - price;
                if (!result.get(8).toString().isEmpty()) {
//                    INSERT TRANSAKI
                    TrnPpob ppob = new TrnPpob();
                    ppob.setTrnNo(trn_no);
                    ppob.setTrnRefNo(result.get(7).toString());
                    ppob.setDistriId(distriId);
                    ppob.setProductId(producId);
                    ppob.setUserTrn(distriId);
                    ppob.setAmount(price);
                    ppob.setTotalAmount(price);
                    ppob.setTrnStatus(result.get(8).toString());
                    ppob.setDateTirmTrn(dateCode);
                    trnPpobDao.insert(ppob);

//                    UDPATE SALDO MITRA
                    mitraDao.updateSaldo(distriId, endingBalance);
                }
            } else {
                result = null;
            }
            return returnTopUp;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public Vector<VocuherGame> voucherGame(String gameCode, String phoneNumber, String ref1, String trn_no, String producId, String distriId) {
        Double price = null;
        Double saldo = null;
        Double endingBalance = null;
        Vector<VocuherGame> dataVoucherGame = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(gameCode);
            params.addElement(phoneNumber);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.game", params);
            if (result != null) {
                VocuherGame voucherGame = new VocuherGame();
                voucherGame.setProdukKode(result.get(0).toString());
                voucherGame.setWaktu(result.get(1).toString());
                voucherGame.setNoHP(result.get(2).toString());
                voucherGame.setSN(result.get(5).toString());
                voucherGame.setRef1(result.get(6).toString());
                voucherGame.setRef2(result.get(7).toString());
                voucherGame.setStatus(result.get(8).toString());
                voucherGame.setKeterangan(result.get(9).toString());
                dataVoucherGame.add(voucherGame);

//                TRANSAKSI
                price = ppobDao.checkPrice(distriId, producId);
//                CEK SALDO MITRA
                saldo = mitraDao.checkBalance(distriId);
                endingBalance = saldo - price;
                if (!result.get(8).toString().isEmpty()) {
//                    INSERT TRANSAKI
                    TrnPpob ppob = new TrnPpob();
                    ppob.setTrnNo(trn_no);
                    ppob.setTrnRefNo(result.get(7).toString());
                    ppob.setDistriId(distriId);
                    ppob.setProductId(producId);
                    ppob.setUserTrn(distriId);
                    ppob.setAmount(price);
                    ppob.setTotalAmount(price);
                    ppob.setTrnStatus(result.get(8).toString());
                    ppob.setDateTirmTrn(dateCode);
                    trnPpobDao.insert(ppob);

//                    UDPATE SALDO MITRA
                    mitraDao.updateSaldo(distriId, endingBalance);
                }
            } else {
                result = null;
            }

            return dataVoucherGame;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<InqueryTagihan> inqueryTagihan(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String ref1) {
        Vector<InqueryTagihan> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.inq", params);
            System.out.println(result);
            if (result != null) {
                InqueryTagihan inquery = new InqueryTagihan();
                inquery.setKodeProduk(result.get(0).toString());
                inquery.setWaktu(result.get(1).toString());
                inquery.setIdpelanggan1(result.get(2).toString());
                inquery.setIdpelanggan2(result.get(3).toString());
                inquery.setIdpelanggan3(result.get(4).toString());
                inquery.setNominal(result.get(5).toString());
                inquery.setBiayaAdmin(result.get(6).toString());
                inquery.setRef1(result.get(9).toString());
                inquery.setRef2(result.get(10).toString());
                inquery.setRef3(result.get(11).toString());
                inquery.setStatus(result.get(12).toString());
                inquery.setKeterangan(result.get(13).toString());
                inquery.setBillstatus(result.get(16).toString());
                inquery.setPaymentStatus(result.get(17).toString());
                inquery.setTotalOutstandingBill(result.get(18).toString());
                inquery.setSwreferenceNumber(result.get(19).toString());
                inquery.setSubcriberName(result.get(20).toString());
                inquery.setServiceUnit(result.get(21).toString());
                inquery.setServiceUnitPhone(result.get(22).toString());
                inquery.setSubcriberSegmentation(result.get(23).toString());
                inquery.setPowerConsumingCategory(result.get(24).toString());
                inquery.setTotalAdminChange(result.get(25).toString());
                inquery.setBth1(result.get(26).toString());
//                inquery.setDuedate1(result.get(27).toString());
//                inquery.setMeterReadDate1(result.get(28).toString());
//                inquery.setRptag1(result.get(29).toString());
//                inquery.setIncentive1(result.get(30).toString());
//                inquery.setValueaddedtax1(result.get(31).toString());
//                inquery.setSlalwbp1(result.get(33).toString());
//                inquery.setSahwbp1(result.get(35).toString());

                dataInquery.add(inquery);
            } else {

            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPaymentPLNPascabayar> inqueryPLNPascabayar(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String ref1) {
        Vector<ResPaymentPLNPascabayar> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.inq", params);
            System.out.println(result);
            if (result != null) {
                ResPaymentPLNPascabayar respone = new ResPaymentPLNPascabayar();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setSubscriberid(result.get(15).toString());
                respone.setBillstatus(result.get(16).toString());
                respone.setPaymentStatus(result.get(17).toString());
                respone.setTotalOutstandingBill(result.get(18).toString());
                respone.setSwreferenceNumber(result.get(19).toString());
                respone.setSubcriberName(result.get(20).toString());
                dataInquery.add(respone);
            } else {

            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPaymentPLNPrabayar> inqueryPLNPrabayar(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String ref1) {
        Vector<ResPaymentPLNPrabayar> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.inq", params);
            System.out.println(result);
            if (result != null) {
                ResPaymentPLNPrabayar respone = new ResPaymentPLNPrabayar();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setNoMeter(result.get(15).toString());
                respone.setIdPelanggan(result.get(16).toString());

                respone.setFlag(result.get(17).toString());
                respone.setNoRef1(result.get(18).toString());
                respone.setNoRef2(result.get(19).toString());
                respone.setVendingReceiptNumber(result.get(20).toString());
                respone.setNamaPelanggan(result.get(21).toString());
                respone.setSubcriberSegmentation(result.get(22).toString());
                respone.setPowerConsumeCategory(result.get(23).toString());
                respone.setMinorunitofAdminCharge(result.get(24).toString());
                respone.setAdminCharge(result.get(25).toString());
                respone.setBuyingoption(result.get(26).toString());
                respone.setDistributionCode(result.get(27).toString());
                respone.setServiceUnit(result.get(28).toString());
                respone.setServiceUnitPhone(result.get(29).toString());
                respone.setMaxKwhLimit(result.get(30).toString());
                respone.setTotalRepeatunsoldToken(result.get(31).toString());
                respone.setUnsold1(result.get(32).toString());
                respone.setUnsold2(result.get(33).toString());
                respone.setTokenPln(result.get(34).toString());
                respone.setMiNorUnitStampDuty(result.get(35).toString());
                respone.setStampDuty(result.get(36).toString());
                respone.setMiNorUnitPpn(result.get(37).toString());
                respone.setPpn(result.get(38).toString());
                respone.setMiNprUnitPpj(result.get(39).toString());
                respone.setPpj(result.get(40).toString());
                respone.setMiNorUnitCutomerPayableInstallment(result.get(41).toString());
                respone.setCutomerpayableInstallment(result.get(42).toString());
                respone.setMinOrunitOfPurchaseDkwhUnit(result.get(43).toString());
                respone.setPowerPurchase(result.get(44).toString());
                respone.setMinOrunitOfPurchaseDkwhUnit(result.get(45).toString());
                respone.setPurchaseDkwhUnit(result.get(46).toString());
                respone.setInfotext(result.get(47).toString());
                dataInquery.add(respone);
            } else {

            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPaymentPLNNonTaglist> inqueryPlnNorTaglist(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String ref1) {
        Vector<ResPaymentPLNNonTaglist> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.inq", params);
            System.out.println(result);
            if (result != null) {
                ResPaymentPLNNonTaglist respone = new ResPaymentPLNNonTaglist();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setRegistrationNumber(result.get(15).toString());
                respone.setAreaCode(result.get(16).toString());
                respone.setTransactionCode(result.get(17).toString());
                respone.setTransactionName(result.get(18).toString());
                respone.setExpireDate(result.get(20).toString());
                respone.setSubscriberid(result.get(21).toString());
                respone.setSubcriberName(result.get(22).toString());
                respone.setPlnRefNumber(result.get(23).toString());
                respone.setSwrefNumber(result.get(24).toString());
                respone.setServiceUnit(result.get(25).toString());
                respone.setServiceUnitAddress(result.get(26).toString());
                respone.setServiceUnitPhone(result.get(27).toString());
                respone.setTotalTransactionAmountMinor(result.get(28).toString());
                respone.setTotalTransactionAmount(result.get(29).toString());
                respone.setPlnBillMinNorUnit(result.get(30).toString());
                respone.setPlnBillValue(result.get(31).toString());
                respone.setAdminChargeMiNorUnit(result.get(32).toString());
                respone.setAdminCharge(result.get(33).toString());
                respone.setMutationNumber(result.get(34).toString());
                respone.setSubcriberSegmentation(result.get(35).toString());
                respone.setPowerConsumingCategory(result.get(36).toString());
                respone.setInqueryReferenceNumber(result.get(37).toString());
                respone.setTotalRepeat(result.get(38).toString());
                respone.setCustomerDetailCode1(result.get(39).toString());
                respone.setCustomerdetailMiNorUnit2(result.get(40).toString());
                respone.setCustomerDetailValueAmount2(result.get(41).toString());
                respone.setCustomerDetailCode1(result.get(42).toString());
                respone.setCustomerDetailCode2(result.get(43).toString());
                respone.setCustomerDetailValueAmount2(result.get(44).toString());
                respone.setInfotext(result.get(45).toString());
                dataInquery.add(respone);
            } else {

            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPaymentProductTv> inqueryProductTv(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String ref1) {
        Vector<ResPaymentProductTv> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.inq", params);
            if (result != null) {
                ResPaymentProductTv respone = new ResPaymentProductTv();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setBillerCode(result.get(15).toString());
                respone.setCustomerId(result.get(16).toString());
                respone.setBillQuantity(result.get(17).toString());
                respone.setNoRef1(result.get(18).toString());
                respone.setNoRef2(result.get(19).toString());
                respone.setCustomerName(result.get(20).toString());
                respone.setCustomerAddress(result.get(21).toString());
                respone.setProductCategory(result.get(22).toString());
                respone.setBillAmount(result.get(23).toString());
                respone.setPenalty(result.get(24).toString());
                respone.setStampDuty(result.get(25).toString());
                respone.setPpn(result.get(26).toString());
                respone.setAdminCharge(result.get(27).toString());
                respone.setBillRefNumber(result.get(28).toString());
                respone.setPtName(result.get(29).toString());
                respone.setBilladminFee(result.get(30).toString());
                respone.setMiscFee(result.get(31).toString());
                respone.setMiscNumber(result.get(32).toString());
                respone.setPeriode(result.get(33).toString());
                respone.setDuedate(result.get(34).toString());
                respone.setCustomInfo1(result.get(35).toString());
                dataInquery.add(respone);
            } else {

            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPascabayar> inqueryPascabayar(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String ref1) {
        Vector<ResPascabayar> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.inq", params);
            if (result != null) {
                ResPascabayar respone = new ResPascabayar();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setBillerCode(result.get(15).toString());
                respone.setCustomerId(result.get(18).toString());
                respone.setBillQuantity(result.get(19).toString());
//                respone.setNoRef1(result.get(22).toString());
//                respone.setNoRef2(result.get(21).toString());
                respone.setCustomerName(result.get(20).toString());
                respone.setCustomerAddress(result.get(21).toString());
                respone.setBillerAdminCharge(result.get(22).toString());
                respone.setTotalBillAmount(result.get(23).toString());
                respone.setProviderName(result.get(24).toString());
                respone.setMonthPeriode1(result.get(25).toString());
                respone.setYearPeriode1(result.get(26).toString());
                respone.setPenalty1(result.get(27).toString());
                respone.setBillAmount1(result.get(28).toString());
                respone.setMichAmount1(result.get(29).toString());
                respone.setMonthPeriode2(result.get(30).toString());
                respone.setYearPeriode2(result.get(31).toString());
                respone.setPenalty2(result.get(32).toString());
                respone.setBillAmount2(result.get(32).toString());
                respone.setMichAmount2(result.get(34).toString());
                respone.setMonthPeriode3(result.get(35).toString());
                respone.setYearPeriode3(result.get(36).toString());
                respone.setPenalty3(result.get(37).toString());
                respone.setBillAmount3(result.get(38).toString());
                respone.setMichAmount3(result.get(39).toString());
                dataInquery.add(respone);
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPaymentProductTelkom> inqueryProductTelkom(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String ref1) {
        Vector<ResPaymentProductTelkom> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.inq", params);
            System.out.println(result);
            if (result != null) {
                ResPaymentProductTelkom respone = new ResPaymentProductTelkom();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setKodeArea(result.get(14).toString());
                respone.setNomerTelepon(result.get(15).toString());
                respone.setKodedivre(result.get(16).toString());
                respone.setKodedatel(result.get(17).toString());
                respone.setJumlahbill(result.get(18).toString());
                respone.setNomerReferensi3(result.get(19).toString());
                respone.setNilaiTagihan3(result.get(20).toString());
                respone.setNomerReferensi2(result.get(21).toString());
                respone.setNilaiTagihan2(result.get(22).toString());
                respone.setNomerReferensi1(result.get(23).toString());
                respone.setNilaiTagihan1(result.get(24).toString());
                respone.setNamaPelanggan(result.get(25).toString());
                respone.setNpwp(result.get(26).toString());
                dataInquery.add(respone);

                dataInquery.add(respone);
            } else {

            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Vector<ResMultiFinance> inqueryMultifinance(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String ref1) {
        Vector<ResMultiFinance> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.inq", params);
            if (result != null) {
                ResMultiFinance respone = new ResMultiFinance();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setBillerCode(result.get(15).toString());
                respone.setCustomerId(result.get(16).toString());
                respone.setBillQuantity(result.get(17).toString());
                respone.setNoRef1(result.get(18).toString());
                respone.setNoRef2(result.get(19).toString());
                respone.setCustomerName(result.get(20).toString());
                respone.setProductCategory(result.get(21).toString());
                respone.setMinorUnit(result.get(22).toString());
                respone.setBillAmount(result.get(23).toString());
                respone.setStampDuty(result.get(24).toString());
                respone.setPpn(result.get(25).toString());
                respone.setAdminCharge(result.get(26).toString());
                respone.setBillRefNumber(result.get(27).toString());
                respone.setPtName(result.get(28).toString());
                respone.setBranchName(result.get(29).toString());
                respone.setItemMerkType(result.get(30).toString());
                respone.setChasisNumber(result.get(31).toString());
                respone.setCarNumber(result.get(32).toString());
                respone.setTenor(result.get(33).toString());
                respone.setLastPaidPeriode(result.get(34).toString());
                respone.setLastPaidDuedate(result.get(35).toString());
                respone.setOsInstallmentAmount(result.get(36).toString());
                respone.setOdInstallmentPeriode(result.get(37).toString());
                respone.setOdIsntallmentAmount(result.get(38).toString());
                respone.setOdPenaltyFee(result.get(39).toString());
                respone.setBilladminFee(result.get(40).toString());
                respone.setMiscFee(result.get(41).toString());
                respone.setMinimumPayment(result.get(42).toString());
                respone.setMaximuPaymnet(result.get(43).toString());
                dataInquery.add(respone);

                dataInquery.add(respone);
            } else {

            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPdam> inqueryPdam(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String ref1) {
        Vector<ResPdam> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.inq", params);
            if (result != null) {
                ResPdam respone = new ResPdam();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setBillerCode(result.get(15).toString());
                respone.setCustomerId1(result.get(16).toString());
                respone.setCustomerId2(result.get(17).toString());
                respone.setCustomerId3(result.get(18).toString());
                respone.setBillQuantity(result.get(19).toString());
                respone.setNoRef1(result.get(20).toString());
                respone.setNoRef2(result.get(21).toString());
                respone.setCustomerName(result.get(22).toString());
                respone.setCustomerAddress(result.get(23).toString());
                respone.setCustomerDetailInformation(result.get(24).toString());
                respone.setBillerAdminCharge(result.get(25).toString());
                respone.setTotalBillAmount(result.get(26).toString());
                respone.setPdamName(result.get(27).toString());
                respone.setMothPeriode1(result.get(28).toString());
                respone.setYearPeriode1(result.get(29).toString());
                respone.setFirstMeterRead1(result.get(30).toString());
                respone.setLastMeterRead1(result.get(31).toString());
                respone.setPenalty1(result.get(32).toString());
                respone.setBillAmount1(result.get(33).toString());
                respone.setMiscAmount1(result.get(34).toString());

                respone.setMothPeriode2(result.get(35).toString());
                respone.setYearPeriode2(result.get(36).toString());
                respone.setFirstMeterRead2(result.get(37).toString());
                respone.setLastMeterRead2(result.get(38).toString());
                respone.setPenalty2(result.get(39).toString());
                respone.setBillAmount2(result.get(40).toString());
                respone.setMiscAmount2(result.get(41).toString());

                respone.setMothPeriode3(result.get(42).toString());
                respone.setYearPeriode3(result.get(43).toString());
                respone.setFirstMeterRead3(result.get(44).toString());
                respone.setLastMeterRead3(result.get(45).toString());
                respone.setPenalty3(result.get(46).toString());
                respone.setBillAmount3(result.get(47).toString());
                respone.setMiscAmount3(result.get(48).toString());

                respone.setMothPeriode4(result.get(49).toString());
                respone.setYearPeriode4(result.get(50).toString());
                respone.setFirstMeterRead4(result.get(51).toString());
                respone.setLastMeterRead4(result.get(52).toString());
                respone.setPenalty4(result.get(53).toString());
                respone.setBillAmount4(result.get(54).toString());
                respone.setMiscAmount4(result.get(55).toString());

                respone.setMothPeriode5(result.get(56).toString());
                respone.setYearPeriode5(result.get(57).toString());
                respone.setFirstMeterRead5(result.get(58).toString());
                respone.setLastMeterRead5(result.get(59).toString());
                respone.setPenalty5(result.get(60).toString());
                respone.setBillAmount5(result.get(61).toString());
                respone.setMiscAmount5(result.get(62).toString());

                respone.setMothPeriode6(result.get(63).toString());
                respone.setYearPeriode6(result.get(64).toString());
                respone.setFirstMeterRead6(result.get(65).toString());
                respone.setLastMeterRead6(result.get(66).toString());
                respone.setPenalty6(result.get(67).toString());
                respone.setBillAmount6(result.get(68).toString());
                respone.setMiscAmount6(result.get(69).toString());
                dataInquery.add(respone);
            } else {

            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResAsuransi> inqueryAsuransi(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String ref1) {
        Vector<ResAsuransi> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.inq", params);
            if (result != null) {
                ResAsuransi respone = new ResAsuransi();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setBillerCode(result.get(15).toString());
                respone.setCustomerId1(result.get(16).toString());
                respone.setBillQuantity(result.get(17).toString());
                respone.setNoRef1(result.get(18).toString());
                respone.setNoRef2(result.get(19).toString());
                respone.setCustomerName(result.get(20).toString());
                respone.setProductCategory(result.get(21).toString());
                respone.setBillAmount(result.get(22).toString());
                respone.setPenalty(result.get(23).toString());
                respone.setStampDuty(result.get(24).toString());
                respone.setPpn(result.get(25).toString());
                respone.setAdminCharge(result.get(26).toString());
                respone.setPpn(result.get(27).toString());
                respone.setAdminCharge(result.get(28).toString());
                respone.setClaimAmount(result.get(29).toString());
                respone.setBillerrRefNumber(result.get(30).toString());
                respone.setPtName(result.get(31).toString());
                respone.setLastPaidPeriode(result.get(32).toString());
                respone.setLastPaidDuedate(result.get(33).toString());
                respone.setBillerAdminFee(result.get(34).toString());
                respone.setMiscFee(result.get(35).toString());
                respone.setMiscNumber(result.get(36).toString());
                respone.setCustomerPhoneNumber(result.get(37).toString());
                respone.setCustomerAddresss(result.get(38).toString());
                dataInquery.add(respone);

                dataInquery.add(respone);
            } else {

            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

//       PAYMENT SERVICE
    public static Vector<PaymentTagihan> paymentTagihan(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String nominal, String ref1, String ref2, String ref3) {
        Vector<PaymentTagihan> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(nominal);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.pay", params);
            if (result != null) {
                PaymentTagihan inquery = new PaymentTagihan();
                inquery.setKodeProduk(result.get(0).toString());
                inquery.setWaktu(result.get(1).toString());
                inquery.setIdpelanggan1(result.get(2).toString());
                inquery.setIdpelanggan2(result.get(3).toString());
                inquery.setIdpelanggan3(result.get(4).toString());
                inquery.setNominal(result.get(5).toString());
                inquery.setBiayaAdmin(result.get(6).toString());
                inquery.setRef1(result.get(9).toString());
                inquery.setRef2(result.get(10).toString());
                inquery.setRef3(result.get(11).toString());
                inquery.setStatus(result.get(12).toString());
//                inquery.setKeterangan(result.get(13).toString());
//                inquery.setBillstatus(result.get(16).toString());
//                inquery.setPaymentStatus(result.get(17).toString());
//                inquery.setTotalOutstandingBill(result.get(18).toString());
//                inquery.setSwreferenceNumber(result.get(19).toString());
//                inquery.setSubcriberName(result.get(20).toString());
//                inquery.setServiceUnit(result.get(21).toString());
//                inquery.setServiceUnitPhone(result.get(22).toString());
//                inquery.setSubcriberSegmentation(result.get(23).toString());
//                inquery.setPowerConsumingCategory(result.get(24).toString());
//                inquery.setTotalAdminChange(result.get(25).toString());
//                inquery.setBth1(result.get(26).toString());
                dataInquery.add(inquery);
            } else {

            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPaymentPLNPascabayar> paymentPLNPascabayar(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String nominal, String ref1, String ref2, String ref3) {
        Vector<ResPaymentPLNPascabayar> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(nominal);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.pay", params);
            if (result != null) {
                ResPaymentPLNPascabayar respone = new ResPaymentPLNPascabayar();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setSubscriberid(result.get(15).toString());
                respone.setBillstatus(result.get(16).toString());
                respone.setPaymentStatus(result.get(17).toString());
                respone.setTotalOutstandingBill(result.get(18).toString());
                respone.setSwreferenceNumber(result.get(19).toString());
                respone.setSubcriberName(result.get(20).toString());
                //add 2018/01/23
                respone.setServiceUnit(result.get(21).toString());
                respone.setServiceUnitPhone(result.get(22).toString());
                respone.setSubcriberSegmentation(result.get(23).toString());
                respone.setPowerConsumingCategory(result.get(24).toString());
                respone.setTotalAdminChange(result.get(25).toString());
                respone.setBth1(result.get(26).toString());
                respone.setDuedate1(result.get(27).toString());
                respone.setMeterReadDate1(result.get(28).toString());
                respone.setRptag1(result.get(29).toString());
                respone.setIncentive1(result.get(30).toString());
                respone.setValueaddedtax1(result.get(31).toString());
//                respone.setSubcriberName(result.get(32).toString());
                respone.setSlalwbp1(result.get(33).toString());
                respone.setSahlwbp1(result.get(34).toString());
                respone.setSlawbp1(result.get(35).toString());
                respone.setSahwbp1(result.get(36).toString());
                respone.setSlakvarh1(result.get(37).toString());
                respone.setSahkvarh1(result.get(38).toString());
                //*              
                respone.setBth2(result.get(39).toString());
                respone.setDuedate2(result.get(40).toString());
                respone.setMeterReadDate2(result.get(41).toString());
                respone.setRptag2(result.get(42).toString());
                respone.setIncentive2(result.get(43).toString());
                respone.setValueaddedtax2(result.get(44).toString());
//                respone.setSubcriberName(result.get(45).toString());
                respone.setSlalwbp2(result.get(46).toString());
                respone.setSahlwbp2(result.get(47).toString());
                respone.setSlawbp2(result.get(48).toString());
                respone.setSahwbp2(result.get(49).toString());
                respone.setSlakvarh2(result.get(50).toString());
                respone.setSahkvarh2(result.get(51).toString());

                respone.setBth3(result.get(52).toString());
                respone.setDuedate3(result.get(53).toString());
                respone.setMeterReadDate3(result.get(54).toString());
                respone.setRptag3(result.get(55).toString());
                respone.setIncentive3(result.get(56).toString());
                respone.setValueaddedtax3(result.get(57).toString());
//                respone.setSubcriberName(result.get(58).toString());
                respone.setSlalwbp3(result.get(59).toString());
                respone.setSahlwbp3(result.get(60).toString());
                respone.setSlawbp3(result.get(61).toString());
                respone.setSahwbp3(result.get(62).toString());
                respone.setSlakvarh3(result.get(63).toString());
                respone.setSahkvarh3(result.get(64).toString());

                respone.setBth4(result.get(65).toString());
                respone.setDuedate4(result.get(66).toString());
                respone.setMeterReadDate4(result.get(67).toString());
                respone.setRptag4(result.get(68).toString());
                respone.setIncentive4(result.get(69).toString());
                respone.setValueaddedtax4(result.get(70).toString());
//                respone.setSubcriberName(result.get(71).toString());
                respone.setSlalwbp4(result.get(72).toString());
                respone.setSahlwbp4(result.get(73).toString());
                respone.setSlawbp4(result.get(74).toString());
                respone.setSahwbp4(result.get(75).toString());
                respone.setSlakvarh4(result.get(76).toString());
                respone.setSahkvarh4(result.get(77).toString());
                respone.setSahkvarh4(result.get(78).toString());

                respone.setAlamat(result.get(79).toString());
                respone.setPlnnpwp(result.get(80).toString());
                respone.setSubcribernpwp(result.get(81).toString());
                respone.setTotalrptag(result.get(81).toString());
                respone.setInfoteks(result.get(82).toString());

                dataInquery.add(respone);
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPaymentPLNPrabayar> paymentPLNPrabayar(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String nominal, String ref1, String ref2, String ref3) {
        Vector<ResPaymentPLNPrabayar> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(nominal);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.pay", params);
            if (result != null) {
                ResPaymentPLNPrabayar respone = new ResPaymentPLNPrabayar();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setNoMeter(result.get(15).toString());
                respone.setIdPelanggan(result.get(16).toString());

                respone.setFlag(result.get(17).toString());
                respone.setNoRef1(result.get(18).toString());
                respone.setNoRef2(result.get(19).toString());
                respone.setVendingReceiptNumber(result.get(20).toString());
                respone.setNamaPelanggan(result.get(21).toString());
                respone.setSubcriberSegmentation(result.get(22).toString());
                respone.setPowerConsumeCategory(result.get(23).toString());
                respone.setMinorunitofAdminCharge(result.get(24).toString());
                respone.setAdminCharge(result.get(25).toString());
                respone.setBuyingoption(result.get(26).toString());
                respone.setDistributionCode(result.get(27).toString());
                respone.setServiceUnit(result.get(28).toString());
                respone.setServiceUnitPhone(result.get(29).toString());
                respone.setMaxKwhLimit(result.get(30).toString());
                respone.setTotalRepeatunsoldToken(result.get(31).toString());
                respone.setUnsold1(result.get(32).toString());
                respone.setUnsold2(result.get(33).toString());
                respone.setTokenPln(result.get(34).toString());
                respone.setMiNorUnitStampDuty(result.get(35).toString());
                respone.setStampDuty(result.get(36).toString());
                respone.setMiNorUnitPpn(result.get(37).toString());
                respone.setPpn(result.get(38).toString());
                respone.setMiNprUnitPpj(result.get(39).toString());
                respone.setPpj(result.get(40).toString());
                respone.setMiNorUnitCutomerPayableInstallment(result.get(41).toString());
                respone.setCutomerpayableInstallment(result.get(42).toString());
                respone.setMinOrunitOfPurchaseDkwhUnit(result.get(43).toString());
                respone.setPowerPurchase(result.get(44).toString());
                respone.setMinOrunitOfPurchaseDkwhUnit(result.get(45).toString());
                respone.setPurchaseDkwhUnit(result.get(46).toString());
                respone.setInfotext(result.get(47).toString());
                dataInquery.add(respone);
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPaymentPLNNonTaglist> paymentPLNNonTaglist(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String nominal, String ref1, String ref2, String ref3) {
        Vector<ResPaymentPLNNonTaglist> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(nominal);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.pay", params);
            if (result != null) {
                ResPaymentPLNNonTaglist respone = new ResPaymentPLNNonTaglist();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setRegistrationNumber(result.get(15).toString());
                respone.setAreaCode(result.get(16).toString());
                respone.setTransactionCode(result.get(17).toString());
                respone.setTransactionName(result.get(18).toString());
                respone.setExpireDate(result.get(20).toString());
                respone.setSubscriberid(result.get(21).toString());
                respone.setSubcriberName(result.get(22).toString());

                respone.setPlnRefNumber(result.get(23).toString());
                respone.setSwrefNumber(result.get(24).toString());
                respone.setServiceUnit(result.get(25).toString());
                respone.setServiceUnitAddress(result.get(26).toString());
                respone.setServiceUnitPhone(result.get(27).toString());
                respone.setTotalTransactionAmountMinor(result.get(28).toString());
                respone.setTotalTransactionAmount(result.get(29).toString());
                respone.setPlnBillMinNorUnit(result.get(30).toString());
                respone.setPlnBillValue(result.get(31).toString());
                respone.setAdminChargeMiNorUnit(result.get(32).toString());
                respone.setAdminCharge(result.get(33).toString());
                respone.setMutationNumber(result.get(34).toString());
                respone.setSubcriberSegmentation(result.get(35).toString());
                respone.setPowerConsumingCategory(result.get(36).toString());
                respone.setInqueryReferenceNumber(result.get(37).toString());
                respone.setTotalRepeat(result.get(38).toString());
                respone.setCustomerDetailCode1(result.get(39).toString());
                respone.setCustomerdetailMiNorUnit2(result.get(40).toString());
                respone.setCustomerDetailValueAmount2(result.get(41).toString());
                respone.setCustomerDetailCode1(result.get(42).toString());
                respone.setCustomerDetailCode2(result.get(43).toString());
                respone.setCustomerDetailValueAmount2(result.get(44).toString());
                respone.setInfotext(result.get(45).toString());

                dataInquery.add(respone);
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPaymentProductTelkom> paymentProductTelkom(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String nominal, String ref1, String ref2, String ref3) {
        Vector<ResPaymentProductTelkom> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(nominal);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.pay", params);
            if (result != null) {
                ResPaymentProductTelkom respone = new ResPaymentProductTelkom();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setKodeArea(result.get(14).toString());
                respone.setNomerTelepon(result.get(15).toString());
                respone.setKodedivre(result.get(16).toString());
                respone.setKodedatel(result.get(17).toString());
                respone.setJumlahbill(result.get(18).toString());
                respone.setNomerReferensi3(result.get(19).toString());
                respone.setNilaiTagihan3(result.get(20).toString());
                respone.setNomerReferensi2(result.get(21).toString());
                respone.setNilaiTagihan2(result.get(22).toString());
                respone.setNomerReferensi1(result.get(23).toString());
                respone.setNilaiTagihan1(result.get(24).toString());
                respone.setNamaPelanggan(result.get(25).toString());
                respone.setNpwp(result.get(26).toString());
                dataInquery.add(respone);
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPascabayar> paymentPascabayar(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String nominal, String ref1, String ref2, String ref3) {
        Vector<ResPascabayar> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(nominal);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.pay", params);
            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    System.out.println("No = " + i + " VALUE PASCA = " + result.get(i));
                }
                ResPascabayar respone = new ResPascabayar();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setBillerCode(result.get(15).toString());
                respone.setCustomerId(result.get(18).toString());
                respone.setBillQuantity(result.get(19).toString());
//                respone.setNoRef1(result.get(22).toString());
//                respone.setNoRef2(result.get(21).toString());
                respone.setCustomerName(result.get(20).toString());
                respone.setCustomerAddress(result.get(21).toString());
                respone.setBillerAdminCharge(result.get(22).toString());
                respone.setTotalBillAmount(result.get(23).toString());
                respone.setProviderName(result.get(24).toString());
                respone.setMonthPeriode1(result.get(25).toString());
                respone.setYearPeriode1(result.get(26).toString());
                respone.setPenalty1(result.get(27).toString());
                respone.setBillAmount1(result.get(28).toString());
                respone.setMichAmount1(result.get(29).toString());
                respone.setMonthPeriode2(result.get(30).toString());
                respone.setYearPeriode2(result.get(31).toString());
                respone.setPenalty2(result.get(32).toString());
                respone.setBillAmount2(result.get(32).toString());
                respone.setMichAmount2(result.get(34).toString());
                respone.setMonthPeriode3(result.get(35).toString());
                respone.setYearPeriode3(result.get(36).toString());
                respone.setPenalty3(result.get(37).toString());
                respone.setBillAmount3(result.get(38).toString());
                respone.setMichAmount3(result.get(39).toString());
                dataInquery.add(respone);
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPaymentProductTv> paymentProductTv(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String nominal, String ref1, String ref2, String ref3) {
        Vector<ResPaymentProductTv> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(nominal);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.pay", params);
            if (result != null) {
                ResPaymentProductTv respone = new ResPaymentProductTv();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setBillerCode(result.get(15).toString());
                respone.setCustomerId(result.get(16).toString());
                respone.setBillQuantity(result.get(17).toString());
                respone.setNoRef1(result.get(18).toString());
                respone.setNoRef2(result.get(19).toString());
                respone.setCustomerName(result.get(20).toString());
                respone.setCustomerAddress(result.get(21).toString());
                respone.setProductCategory(result.get(22).toString());
                respone.setBillAmount(result.get(23).toString());
                respone.setPenalty(result.get(24).toString());
                respone.setStampDuty(result.get(25).toString());
                respone.setPpn(result.get(26).toString());
                respone.setAdminCharge(result.get(27).toString());
                respone.setBillRefNumber(result.get(28).toString());
                respone.setPtName(result.get(29).toString());
                respone.setBilladminFee(result.get(30).toString());
                respone.setMiscFee(result.get(31).toString());
                respone.setMiscNumber(result.get(32).toString());
                respone.setPeriode(result.get(33).toString());
                respone.setDuedate(result.get(34).toString());
                respone.setCustomInfo1(result.get(35).toString());
                dataInquery.add(respone);
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResMultiFinance> paymentMutliFinance(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String nominal, String ref1, String ref2, String ref3) {
        Vector<ResMultiFinance> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(nominal);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.pay", params);
            if (result != null) {
                ResMultiFinance respone = new ResMultiFinance();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setBillerCode(result.get(15).toString());
                respone.setCustomerId(result.get(16).toString());
                respone.setBillQuantity(result.get(17).toString());
                respone.setNoRef1(result.get(18).toString());
                respone.setNoRef2(result.get(19).toString());
                respone.setCustomerName(result.get(20).toString());
                respone.setProductCategory(result.get(21).toString());
                respone.setMinorUnit(result.get(22).toString());
                respone.setBillAmount(result.get(23).toString());
                respone.setStampDuty(result.get(24).toString());
                respone.setPpn(result.get(25).toString());
                respone.setAdminCharge(result.get(26).toString());
                respone.setBillRefNumber(result.get(27).toString());
                respone.setPtName(result.get(28).toString());
                respone.setBranchName(result.get(29).toString());
                respone.setItemMerkType(result.get(30).toString());
                respone.setChasisNumber(result.get(31).toString());
                respone.setCarNumber(result.get(32).toString());
                respone.setTenor(result.get(33).toString());
                respone.setLastPaidPeriode(result.get(34).toString());
                respone.setLastPaidDuedate(result.get(35).toString());
                respone.setOsInstallmentAmount(result.get(36).toString());
                respone.setOdInstallmentPeriode(result.get(37).toString());
                respone.setOdIsntallmentAmount(result.get(38).toString());
                respone.setOdPenaltyFee(result.get(39).toString());
                respone.setBilladminFee(result.get(40).toString());
                respone.setMiscFee(result.get(41).toString());
                respone.setMinimumPayment(result.get(42).toString());
                respone.setMaximuPaymnet(result.get(43).toString());
                dataInquery.add(respone);
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResPdam> paymentPdam(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String nominal, String ref1, String ref2, String ref3) {
        Vector<ResPdam> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(nominal);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.pay", params);
            if (result != null) {
                ResPdam respone = new ResPdam();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setBillerCode(result.get(15).toString());
                respone.setCustomerId1(result.get(16).toString());
                respone.setCustomerId2(result.get(17).toString());
                respone.setCustomerId3(result.get(18).toString());
                respone.setBillQuantity(result.get(19).toString());
                respone.setNoRef1(result.get(20).toString());
                respone.setNoRef2(result.get(21).toString());
                respone.setCustomerName(result.get(22).toString());
                respone.setCustomerAddress(result.get(23).toString());
                respone.setCustomerDetailInformation(result.get(24).toString());
                respone.setBillerAdminCharge(result.get(25).toString());
                respone.setTotalBillAmount(result.get(26).toString());
                respone.setPdamName(result.get(27).toString());
                respone.setMothPeriode1(result.get(28).toString());
                respone.setYearPeriode1(result.get(29).toString());
                respone.setFirstMeterRead1(result.get(30).toString());
                respone.setLastMeterRead1(result.get(31).toString());
                respone.setPenalty1(result.get(32).toString());
                respone.setBillAmount1(result.get(33).toString());
                respone.setMiscAmount1(result.get(34).toString());

                respone.setMothPeriode2(result.get(35).toString());
                respone.setYearPeriode2(result.get(36).toString());
                respone.setFirstMeterRead2(result.get(37).toString());
                respone.setLastMeterRead2(result.get(38).toString());
                respone.setPenalty2(result.get(39).toString());
                respone.setBillAmount2(result.get(40).toString());
                respone.setMiscAmount2(result.get(41).toString());

                respone.setMothPeriode3(result.get(42).toString());
                respone.setYearPeriode3(result.get(43).toString());
                respone.setFirstMeterRead3(result.get(44).toString());
                respone.setLastMeterRead3(result.get(45).toString());
                respone.setPenalty3(result.get(46).toString());
                respone.setBillAmount3(result.get(47).toString());
                respone.setMiscAmount3(result.get(48).toString());

                respone.setMothPeriode4(result.get(49).toString());
                respone.setYearPeriode4(result.get(50).toString());
                respone.setFirstMeterRead4(result.get(51).toString());
                respone.setLastMeterRead4(result.get(52).toString());
                respone.setPenalty4(result.get(53).toString());
                respone.setBillAmount4(result.get(54).toString());
                respone.setMiscAmount4(result.get(55).toString());

                respone.setMothPeriode5(result.get(56).toString());
                respone.setYearPeriode5(result.get(57).toString());
                respone.setFirstMeterRead5(result.get(58).toString());
                respone.setLastMeterRead5(result.get(59).toString());
                respone.setPenalty5(result.get(60).toString());
                respone.setBillAmount5(result.get(61).toString());
                respone.setMiscAmount5(result.get(62).toString());

                respone.setMothPeriode6(result.get(63).toString());
                respone.setYearPeriode6(result.get(64).toString());
                respone.setFirstMeterRead6(result.get(65).toString());
                respone.setLastMeterRead6(result.get(66).toString());
                respone.setPenalty6(result.get(67).toString());
                respone.setBillAmount6(result.get(68).toString());
                respone.setMiscAmount6(result.get(69).toString());
                dataInquery.add(respone);
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<ResAsuransi> paymentAsuransi(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String nominal, String ref1, String ref2, String ref3) {
        Vector<ResAsuransi> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(nominal);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.pay", params);
            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    System.out.println("No = " + i + " VALUE = " + result.get(i));
                }
                ResAsuransi respone = new ResAsuransi();
                respone.setKodeProduk(result.get(0).toString());
                respone.setWaktu(result.get(1).toString());
                respone.setIdpelanggan1(result.get(2).toString());
                respone.setIdpelanggan2(result.get(3).toString());
                respone.setIdpelanggan3(result.get(4).toString());
                respone.setNominal(result.get(5).toString());
                respone.setBiayaAdmin(result.get(6).toString());
                respone.setRef1(result.get(9).toString());
                respone.setRef2(result.get(10).toString());
                respone.setRef3(result.get(11).toString());
                respone.setStatus(result.get(12).toString());
                respone.setKeterangan(result.get(13).toString());
                respone.setSwitcherid(result.get(14).toString());
                respone.setBillerCode(result.get(15).toString());
                respone.setCustomerId1(result.get(16).toString());
                respone.setBillQuantity(result.get(17).toString());
                respone.setNoRef1(result.get(18).toString());
                respone.setNoRef2(result.get(19).toString());
                respone.setCustomerName(result.get(20).toString());
                respone.setProductCategory(result.get(21).toString());
                respone.setBillAmount(result.get(22).toString());
                respone.setPenalty(result.get(23).toString());
                respone.setStampDuty(result.get(24).toString());
                respone.setPpn(result.get(25).toString());
                respone.setAdminCharge(result.get(26).toString());
                respone.setPpn(result.get(27).toString());
                respone.setAdminCharge(result.get(28).toString());
                respone.setClaimAmount(result.get(29).toString());
                respone.setBillerrRefNumber(result.get(30).toString());
                respone.setPtName(result.get(31).toString());
                respone.setLastPaidPeriode(result.get(32).toString());
                respone.setLastPaidDuedate(result.get(33).toString());
                respone.setBillerAdminFee(result.get(34).toString());
                respone.setMiscFee(result.get(35).toString());
                respone.setMiscNumber(result.get(36).toString());
                respone.setCustomerPhoneNumber(result.get(37).toString());
                respone.setCustomerAddresss(result.get(38).toString());
//                respone.setAhliWarisPhoneNumber(result.get(39).toString());
//                respone.setAhliWarisAddress(result.get(40).toString());

                dataInquery.add(respone);
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<PaymentTagihan> paymentAsuransiBintang(String productCode, String idPelanggan1, String idPelanggan2, String idPelanggan3, String nominal, String ref1, String ref2, String ref3) {
        Vector<PaymentTagihan> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(nominal);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.paybintang", params);

            if (result != null) {
                PaymentTagihan inquery = new PaymentTagihan();

                inquery.setKodeProduk(result.get(1).toString());
                inquery.setNominal(result.get(6).toString());
                inquery.setBiayaAdmin(result.get(7).toString());
                inquery.setStatus(result.get(13).toString());
                dataInquery.add(inquery);
                System.out.println(result);
            } else {
                return null;
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<InqueryTagihan> inqueryTagihanAsuransiBintang(String productCode, String idPelanggan1, String nominal, String nama, String telepon, String ref1, String ref2, String ref3) {
        Vector<InqueryTagihan> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(idPelanggan1);
            params.addElement(nominal);
            params.addElement(nama);
            params.addElement(telepon);
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.paybintang", params);

            if (result != null) {
                InqueryTagihan inquery = new InqueryTagihan();
                inquery.setRef1(result.get(6).toString());
                inquery.setRef2(result.get(7).toString());
                inquery.setStatus(result.get(8).toString());
                inquery.setKeterangan(result.get(9).toString());
                dataInquery.add(inquery);
            } else {
                return null;
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<InqueryTagihan> inqueryTagihanEquity(String productCode, String idPelanggan1, String nominal, String namaTertanggung, String tanggalLahirTertanggung, String tanggalBerangkat, String teleponTertanggung, String teleponAhliWaris, String ref1) {
        Vector<InqueryTagihan> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(idPelanggan1);
            params.addElement(nominal);
            params.addElement(namaTertanggung);
            params.addElement(tanggalLahirTertanggung);
            params.addElement(tanggalBerangkat);
            params.addElement(teleponTertanggung);
            params.addElement(teleponAhliWaris);
            params.addElement(ref1);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.payequity", params);
            if (result != null) {
                InqueryTagihan inquery = new InqueryTagihan();
                inquery.setRef1(result.get(6).toString());
                inquery.setRef2(result.get(7).toString());
                inquery.setStatus(result.get(8).toString());
                inquery.setKeterangan(result.get(9).toString());
                dataInquery.add(inquery);
            } else {
                return null;
            }
            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<PaymentBpjs> inqueryTagihanBPJSKesehatan(String productCode, String idPelanggan, String periodeBulan, String ref1) {
        Vector<PaymentBpjs> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan);
            params.addElement(periodeBulan);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.bpjsinq", params);
            result.iterator();
            if (result != null) {
                PaymentBpjs inquery = new PaymentBpjs();

                inquery.setTanggal(result.get(1).toString());
                inquery.setNoResi(result.get(10).toString());
                inquery.setNoBpjsPeserta(result.get(2).toString());
                inquery.setNamaPeserta(result.get(20).toString());
                inquery.setJmlPeserta(result.get(17).toString());
                inquery.setNoRef(result.get(9).toString());
                inquery.setNoHandPhone(result.get(35).toString());
                inquery.setNoRefrensi(result.get(15).toString());
                inquery.setJumlahPremi(result.get(17).toString());
                inquery.setJumlahTagihan(result.get(5).toString());
                inquery.setBiayaAdmin(result.get(6).toString());
                inquery.setTotalBayar(result.get(13).toString());
                dataInquery.add(inquery);
                System.out.println(result);
            } else {
                return null;
            }

            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<PaymentBpjs> paymentTagihanBPJSKesehatan(String productCode, String idPelanggan, String periodeBulan, String phoneNumber, String nominal, String ref1, String ref2) {
        Vector<PaymentBpjs> dataInquery = new Vector<>();
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(productCode);
            params.addElement(idPelanggan);
            params.addElement(periodeBulan);
            params.addElement(phoneNumber);
            params.addElement(nominal);
            params.addElement(UID);
            params.addElement(PASSWORD);
            params.addElement(ref1);
            params.addElement(ref2);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.bpjspay", params);
            result.iterator();
            if (result != null) {
                PaymentBpjs inquery = new PaymentBpjs();
                inquery.setTanggal(result.get(1).toString());
                inquery.setNoResi(result.get(10).toString());
                inquery.setNoBpjsPeserta(result.get(2).toString());
                inquery.setNamaPeserta(result.get(20).toString());
                inquery.setJmlPeserta(result.get(17).toString());
                inquery.setNoRef(result.get(9).toString());
                inquery.setNoHandPhone(result.get(35).toString());
                inquery.setNoRefrensi(result.get(15).toString());
                inquery.setJumlahPremi(result.get(17).toString());
                inquery.setJumlahTagihan(result.get(5).toString());
                inquery.setBiayaAdmin(result.get(6).toString());
                inquery.setTotalBayar(result.get(13).toString());
                dataInquery.add(inquery);
                System.out.println(result);
            } else {
                return null;
            }

            return dataInquery;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector inqueryRegistrasiJawaPOS(InqueryJawaPos inqueryJawaPOs) {
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(inqueryJawaPOs.getKodeProduct());
            params.addElement(inqueryJawaPOs.getIdPelanggan1());
            params.addElement(inqueryJawaPOs.getIdPelanggan2());
            params.addElement(inqueryJawaPOs.getIdPelanggan3());
            params.addElement(new String(UID));
            params.addElement(new String(PASSWORD));
            params.addElement(inqueryJawaPOs.getRef1());
            params.addElement(inqueryJawaPOs.getNama());
            params.addElement(inqueryJawaPOs.getAlamat());
            params.addElement(inqueryJawaPOs.getKodeKecamatan());
            params.addElement(inqueryJawaPOs.getKodeKelurahan());
            params.addElement(inqueryJawaPOs.getRt());
            params.addElement(inqueryJawaPOs.getRw());
            params.addElement(inqueryJawaPOs.getNoHp());
            params.addElement(inqueryJawaPOs.getTempatLahir());
            params.addElement(inqueryJawaPOs.getTanggalLahir());
            params.addElement(inqueryJawaPOs.getNoIdentitas());
            params.addElement(inqueryJawaPOs.getTanggalAwalPengiriman());

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.jawaposreg_iq", params);
            result.iterator();
            System.out.println(result.get(4));

            return result;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector paymentRegistrasiJawaPOS(PaymentJawaPos paymentJawaPos) {
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(paymentJawaPos.getKodeProduct());
            params.addElement(paymentJawaPos.getIdPelanggan1());
            params.addElement(paymentJawaPos.getIdPelanggan2());
            params.addElement(paymentJawaPos.getIdPelanggan3());
            params.addElement(paymentJawaPos.getNominal());
            params.addElement(new String(UID));
            params.addElement(new String(PASSWORD));
            params.addElement(paymentJawaPos.getRef1());
            params.addElement(paymentJawaPos.getRef2());
            params.addElement(paymentJawaPos.getRef3());
            params.addElement(paymentJawaPos.getNama());
            params.addElement(paymentJawaPos.getAlamat());
            params.addElement(paymentJawaPos.getKodeKecamatan());
            params.addElement(paymentJawaPos.getKodeKelurahan());
            params.addElement(paymentJawaPos.getRt());
            params.addElement(paymentJawaPos.getRw());
            params.addElement(paymentJawaPos.getNoHp());
            params.addElement(paymentJawaPos.getTempatLahir());
            params.addElement(paymentJawaPos.getTanggalLahir());
            params.addElement(paymentJawaPos.getNoIdentitas());
            params.addElement(paymentJawaPos.getTanggalAwalPengiriman());

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.jawaposreg_pay", params);
            result.iterator();
            System.out.println(result.get(4));

            return result;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector<InqueryTagihan> inqueryTagihanJawaPOS(String kodeProduct, String idPelanggan1, String idPelanggan2, String idPelanggan3, String ref1, String jumlahBulan) {
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(kodeProduct);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(new String(UID));
            params.addElement(new String(PASSWORD));
            params.addElement(ref1);
            params.addElement(jumlahBulan);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.jawapos_inq", params);
            result.iterator();
            System.out.println(result.get(4));

            return result;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector paymentTagihanJawaPOS(String kodeProduct, String idPelanggan1, String idPelanggan2, String idPelanggan3, String nominal, String ref1, String ref2, String ref3, String jumlahBulan) {
        try {
            String UID = StaticParam.UID;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(kodeProduct);
            params.addElement(idPelanggan1);
            params.addElement(idPelanggan2);
            params.addElement(idPelanggan3);
            params.addElement(nominal);
            params.addElement(new String(UID));
            params.addElement(new String(PASSWORD));
            params.addElement(ref1);
            params.addElement(ref2);
            params.addElement(ref3);
            params.addElement(jumlahBulan);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.jawaposreg_pay", params);
            result.iterator();
            System.out.println(result.get(4));

            return result;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector cekDataTransaksi(String tanggalFrom, String tanggalTo, String idTransaksi, String kodeProduct, String idPelanggan, String limit) {
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();
            params.addElement(tanggalFrom);
            params.addElement(tanggalTo);
            params.addElement(idTransaksi);
            params.addElement(kodeProduct);
            params.addElement(idPelanggan);
            params.addElement(limit);
            params.addElement(UID);
            params.addElement(PASSWORD);

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.datatransaksi", params);
            result.iterator();
            System.out.println(result.get(4));

            return result;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector gantiPin(String passBaru) {
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();

            params.addElement(new String(UID));
            params.addElement(new String(PASSWORD));
            params.addElement(new String(passBaru));

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.gantipin", params);
            result.iterator();
            System.out.println(result.get(4));

            return result;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector cetakPin(String ref1, String ref2) {
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();

            params.addElement(new String(UID));
            params.addElement(new String(PASSWORD));
            params.addElement(new String(ref1));
            params.addElement(new String(ref2));

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.cu", params);
            result.iterator();
            System.out.println(result.get(4));

            return result;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static Vector cekSaldo() {
        try {
            String UID = StaticParam.UID_BM;
            String PASSWORD = StaticParam.PASSWORD;
            String URL = StaticParam.URL_FASTPAY;
            XmlRpcClient client = new XmlRpcClient(URL);
            Vector params = new Vector();

            params.addElement(new String(UID));
            params.addElement(new String(PASSWORD));

            Vector result = new Vector();
            result = (Vector) client.execute("fastpay.balance", params);
            result.iterator();
            System.out.println(result.get(4));

            return result;

        } catch (XmlRpcException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(FastpayAPI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
