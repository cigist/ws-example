/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cigist.listener;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author user
 */
@WebListener
public class CigistContextAttributeListener implements ServletContextAttributeListener{

    @Override
    public void attributeAdded(ServletContextAttributeEvent scae) {
       System.out.println("ServletContext attribute added::{"+scae.getName()+","+scae.getValue()+"}");
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent scae) {
      System.out.println("ServletContext attribute replaced::{"+scae.getName()+","+scae.getValue()+"}");
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent scae) {
       System.out.println("ServletContext attribute removed::{"+scae.getName()+","+scae.getValue()+"}");
    }
    
}
