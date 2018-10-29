/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.service.rest;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Irwan Cigist <cigist.developer@gmail.com>
 */
@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.xpay.service.rest.balance_rest.class);
        resources.add(com.xpay.service.rest.distri_ppob_rest.class);
        resources.add(com.xpay.service.rest.flight_rest.class);
        resources.add(com.xpay.service.rest.game_rest.class);
        resources.add(com.xpay.service.rest.hotel_rest.class);
        resources.add(com.xpay.service.rest.paketdata_rest.class);
        resources.add(com.xpay.service.rest.payment_rest.class);
        resources.add(com.xpay.service.rest.pelni_rest.class);
        resources.add(com.xpay.service.rest.ppob_rest.class);
        resources.add(com.xpay.service.rest.pulsa_rest.class);
    }
    
}
