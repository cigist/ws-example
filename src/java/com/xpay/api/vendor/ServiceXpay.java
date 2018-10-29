/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.api.vendor;

import com.cigist.framework.conn.CigistHttpConn;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Irwan Cigist <cigist.developer@gmail.com>
 */
public class ServiceXpay {

    CigistHttpConn conn = new CigistHttpConn();
    public String postTopUpPulsa(String param) {
        String respone = conn.serviceGet(param);
        if (respone != null) {
            JSONObject root = new JSONObject(respone);
            JSONArray dataArray = root.getJSONArray("DATA");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject ppob = dataArray.getJSONObject(i);

            }

        }
        return null;

    }
}
