/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cigist.framework.util;

import java.util.UUID;

/**
 *
 * @author user
 */
public class CigistAutoGenerate {
    public static String genToken(){
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        
        return token;
    }
}
