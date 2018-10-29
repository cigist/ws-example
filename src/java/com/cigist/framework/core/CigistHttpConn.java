/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cigist.framework.core;

import com.xpay.util.StaticParam;
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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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

    public void servicePost(String url, ArrayList<NameValuePair> nameValuePairs) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(StaticParam.URL_FASTRAVEL + url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse res = httpclient.execute(httppost);
            HttpEntity entity = res.getEntity();
            entity.getContent();
            System.out.println(" RESPONE " + res.getEntity().getContent());

        } catch (IOException | IllegalStateException e) {
        }
    }

    public static String Post(String url, String param) {
        HashMap<String, String> headers = new HashMap<>();

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));
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

    public static String doPost(String serviceName) {
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(StaticParam.URL_FASTPAY + serviceName);
            StringEntity input = new StringEntity("{\"product\":100,\"name\":\"iPad 4\"}");
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
        return null;

    }

}
