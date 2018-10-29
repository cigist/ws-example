/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.dao;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author xmedia
 */
public class AuthenticationMitra extends MstMitraDao {

    private static SessionFactory factory;

    public AuthenticationMitra() {
        try {
            factory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
        } catch (HibernateException ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public boolean validateAccountMitra(String uid, String pass) {
        Session session = factory.openSession();
        Transaction tx = null;
        String sql = "select distri_id from mst_login_distri where distri_id='" + uid + "' AND password = '" + pass + "' and active='Y'";
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            List id = query.list();
            tx.commit();
            if (!id.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return false;
    }

    public Double checkBalance(String uid, String pass) {
        Session session = factory.openSession();
        Transaction tx = null;
        Double saldo = 0.0;
        String sql = "select ending_balance from mst_saldo where distri_id='" + uid + "' AND pin= '" + pass + "' and active='Y'";
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            List balance = query.list();
            tx.commit();
            if (!balance.isEmpty()) {
                saldo = Double.parseDouble(balance.get(0).toString());
                return saldo;
            } else {
                return saldo;
            }
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return saldo;
    }
}
