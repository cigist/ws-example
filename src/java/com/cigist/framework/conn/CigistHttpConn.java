/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cigist.framework.conn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.catalina.WebResource;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author irwan cigist /irwancigist@gmail.com
 */
public class CigistHttpConn {

    static String BASE_URL = "http://api-sandbox.tiket.com/";
    public String serviceGet(String url) {
        String respon = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader(new BasicHeader("Prama", "no-cache"));
            httpGet.setHeader(new BasicHeader("Cache-Control", "no-cache"));
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            respon = EntityUtils.toString(httpEntity);
        } catch (IOException ix) {

        }

        return respon;
    }

    public String servicePost(String url, ArrayList<NameValuePair> nameValuePairs) {
        System.out.println(BASE_URL + url+nameValuePairs);
        HashMap<String, String> headers = new HashMap<>();
        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(BASE_URL + url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            for (Entry<String, String> s : headers.entrySet()) {
                httppost.setHeader(s.getKey(), s.getValue());
            }
            HttpResponse res = httpclient.execute(httppost);
            InputStream ips = res.getEntity().getContent();
            BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
            if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new Exception(res.getStatusLine().getReasonPhrase());
            }
            StringBuilder sb = new StringBuilder();
            String s;
            while (true) {
                s = buf.readLine();
                if (s == null || s.length() == 0) {
                    break;
                }
                sb.append(s);

            }
            buf.close();
            ips.close();
            return sb.toString();

        } catch (Exception ex) {
            Logger.getLogger(CigistHttpConn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String Post(String url, String param) {
        HashMap<String, String> headers = new HashMap<>();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            request.setURI(new URI(BASE_URL + url));
            request.setEntity(new StringEntity(param));
            for (Entry<String, String> s : headers.entrySet()) {
                request.setHeader(s.getKey(), s.getValue());
            }
            HttpResponse response = client.execute(request);

            InputStream ips = response.getEntity().getContent();
            BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new Exception(response.getStatusLine().getReasonPhrase());
            }
            StringBuilder sb = new StringBuilder();
            String s;
            while (true) {
                s = buf.readLine();
                if (s == null || s.length() == 0) {
                    break;
                }
                sb.append(s);

            }
            buf.close();
            ips.close();
            return sb.toString();
        } catch (URISyntaxException ex) {
            Logger.getLogger(CigistHttpConn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CigistHttpConn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CigistHttpConn.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CigistHttpConn.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
