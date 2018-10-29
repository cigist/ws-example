/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cigist.framework.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class CigistStringFormat {

    Random rand = new Random();
    StringBuilder sBuildz = new StringBuilder();
    int number;
    private static final String CUSTOMER_CODE = null;
    StringBuilder sb = new StringBuilder();

    public static String formatApostrophe(String value) {
        return null;
    }

    public String autoCode(String formatKey) {
        number = rand.nextInt(999999) + 999999;
        sBuildz.setLength(0);
        String code = sBuildz.append(formatKey).append(number).toString();

        return code;

    }

    public String activCode() {
        number = rand.nextInt(9999) + 9999;
        sBuildz.setLength(0);
        String code = sBuildz.append(number).toString();

        return code;

    }

    public static Date stringToDate(String dateString, String format){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date date = (Date) dateFormat.parse(format);
            
            return date;
        } catch (ParseException ex) {
            Logger.getLogger(CigistStringFormat.class.getName()).log(Level.SEVERE, null, ex);
             
            return null;
        }
    }
}
