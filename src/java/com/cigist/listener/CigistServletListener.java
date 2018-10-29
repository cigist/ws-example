/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cigist.listener;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author user
 */
@WebListener
public class CigistServletListener implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        ServletRequest servletRequest = sre.getServletRequest();
        System.out.println("ServletRequest destroyed. Remote IP=" + servletRequest.getRemoteAddr());
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        ServletRequest servletRequest = sre.getServletRequest();
        System.out.println("ServletRequest initialized. Remote IP=" + servletRequest.getRemoteAddr());
    }

}
