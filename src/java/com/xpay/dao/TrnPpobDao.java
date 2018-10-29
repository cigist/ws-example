/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.dao;

import com.xpay.model.TrnPpob;
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
public class TrnPpobDao {

    private static SessionFactory factory;

    public TrnPpobDao() {
        try {
            factory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

    }

    public boolean insert(TrnPpob trnPpob) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            SQLQuery insertQuery = session.createSQLQuery("insert into trn_ppob(trn_no,trn_ref_no,distri_id,product_id,amount,amount_fee,amount_admin,total_amount," +
"                    "+ "trn_status,user_trn,datetime_trn)VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            insertQuery.setParameter(0, trnPpob.getTrnNo());
            insertQuery.setParameter(1, trnPpob.getTrnRefNo());
            insertQuery.setParameter(2, trnPpob.getDistriId());
            insertQuery.setParameter(3, trnPpob.getProductId());
            insertQuery.setParameter(4, trnPpob.getAmount());
            insertQuery.setParameter(5, trnPpob.getAmountFee());
            insertQuery.setParameter(6, trnPpob.getAmountAdmin());
            insertQuery.setParameter(7, trnPpob.getTotalAmount());
            insertQuery.setParameter(8, trnPpob.getTrnStatus());
            insertQuery.setParameter(9, trnPpob.getUserTrn());
            insertQuery.setParameter(10, trnPpob.getDateTirmTrn());
            insertQuery.executeUpdate();
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return false;
    }
}
