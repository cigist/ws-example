package com.cigist.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
@WebFilter
public class CigistFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter Di inisiasi");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Mencegah request");
        //Get the client’s Locale

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session == null) {
            res.sendRedirect("login.jsp"); // No logged-in user found, so redirect to login page.
        } else {
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
        System.out.println("Filter di hancurkan");
    }

}
