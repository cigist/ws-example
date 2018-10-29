/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cigist.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author user
 */
@WebListener
public class CigistSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
        System.out.println("Session Created:: ID=" + hse.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
        System.out.println("Session Destroyed:: ID=" + hse.getSession().getId());
    }

}
