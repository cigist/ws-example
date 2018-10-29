/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cigist.framework.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author user
 */
public class CigistDateTimeNow {

    public static String getDateTimeNow(String pattern) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String strDate = formatter.format(date);

        return strDate;
    }

    public static String getDateFormat(String date, String pattern) {
        long datDate = Date.parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String strDate = formatter.format(datDate);

        return strDate;
    }

}
