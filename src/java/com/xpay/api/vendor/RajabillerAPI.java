/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.api.vendor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.Serializer;

/**
 *
 * @author cigist
 */
public class RajabillerAPI {

    public static String checkBalance() {
        //inisiasi awal methodcall
        Element root = new Element("MethodCall");
        //pembentukan String MethodName
        Element mname = new Element("MethodName");
        //Inisiasi nama method balance/cek saldo
        mname.appendChild("rajabiller.balance");
        //pembentukan String Params
        Element params = new Element("params");
        //pembentukan String Param 2x
        Element param1 = new Element("param");
        Element param2 = new Element("param");

        //membungkus methodname dengan methodcall
        root.appendChild(mname);
        //membungkus params dengan methodcall
        root.appendChild(params);

        //membuat string value pertama
        Element val1 = new Element("value");
        //membuat string string pertama
        Element str1 = new Element("string");

        //membuat string value kedua
        Element val2 = new Element("value");
        //membuat string string kedua
        Element str2 = new Element("string");

        //membungkus param yang pertama dengan params
        params.appendChild(param1);
        //membungkus param yang kedua dengan params
        params.appendChild(param2);

        //membungkus value yang pertama dengan param
        param1.appendChild(val1);
        //membungkus value yang kedua dengan param
        param2.appendChild(val2);

        //membungkus string yang pertama dengan value
        val1.appendChild(str1);
        //membungkus string yang kedua dengan value
        val2.appendChild(str2);

        //mengisi atau membungkus UID dengan string yang pertama
        str1.appendChild("SP100036");
        //mengisi atau membungkus pass dengan string yang kedua
        str2.appendChild("128779");

        Document doc = new Document(root);
        String hasil = doc.toXML();

        //hasil request/generate xml
        System.out.println("Hasil Request Generate XML Rajabiller: ");
        serialize(doc);

        String strURL = "https://202.43.173.234/transaksi/";

        try {

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{
                new DefaultTrustManager()
            }, new SecureRandom());
            SSLContext.setDefault(ctx);

            URL url = new URL(strURL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "text/xml; charset=ISO-8859-1");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(3500);

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
                writer.write(hasil);
            }

            if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
//                BufferedReader input = new BufferedReader(new InputStreamReader(
//                                conn.getInputStream()), 8192);
//                String strLine = null;
//                StringBuilder response = new StringBuilder();
//                while ((strLine = input.readLine()) != null) {
//                    response.append(strLine);
//                }
//                input.close();

                try {
                    Document build = new Builder().build(conn.getInputStream());

                    //hasil respon xml rajabiller
                    System.out.println("Hasil Response Rajabiller: ");
                    serialize(build);

                    //pecah tiap2 node pada xml rajabiller
                    Element el = build.getRootElement();
                    Element data = el.getFirstChildElement("params") //pecah node params
                            .getFirstChildElement("param") //pecah node param
                            .getFirstChildElement("value") //pecah node value
                            .getFirstChildElement("array") //pecah node array
                            .getFirstChildElement("data"); //pecah node data

                    Elements values = data.getChildElements(); //ambil element value(s) dari node/element data

                    //ambil string dari node/element value
                    String hasilSaldo = values.get(4).getValue(); //get(4)->ambil array ke 4 dari value string yang menunjukkan keterangan pada rajabiller
                    //keluarkan  hasil string ke 4
                    System.out.println("Keterangan: " + hasilSaldo);
                    
                    return hasilSaldo;

                } catch (ParsingException ex) {
                    Logger.getLogger(RajabillerAPI.class.getName()).log(Level.SEVERE, null, ex);
                }

                //System.out.println("Response body: " + response.toString());
            } else {
                System.out.println("Response status code: " + conn.getResponseCode());
                System.out.println("Response body: " + conn.getResponseMessage());
            }

            conn.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(RajabillerAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException ex) {
            Logger.getLogger(RajabillerAPI.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }

        return hasil;

    }

    //fungsi untuk indent XML
    private static void serialize(Document doc) {
        try {
            Serializer serializer = new Serializer(System.out);
            serializer.setIndent(4);
            serializer.setMaxLength(64);
            serializer.write(doc);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}
