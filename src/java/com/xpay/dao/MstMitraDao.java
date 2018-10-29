package com.xpay.dao;

import com.cigist.framework.conn.CigistConnection;
import com.cigist.framework.core.CigistDao;
import com.xpay.model.MstMitra;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author xmedia
 */
public class MstMitraDao {

    CigistConnection connect = new CigistConnection();
    CigistDao cigistDao = new CigistDao();
      private static SessionFactory factory;

    public MstMitraDao() {
        try {
            factory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

    }
     public Double checkBalance(String uid) {
        Session session = factory.openSession();
        Transaction tx = null;
        Double saldo = 0.0;
        String sql = "select ending_balance from mst_saldo where distri_id='" + uid + "'";
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
    
      public boolean updateSaldo(String distriId,Double Saldo) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            SQLQuery insertQuery = session.createSQLQuery("update mst_saldo set ending_balance=? where distri_id='"+distriId+"'");
            insertQuery.setParameter(0,Saldo);

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

    synchronized public boolean insertUpdate(MstMitra objMitra) {

        PreparedStatement ps = null;
        try {
            String sql = "select username from mst_mitra where username='" + objMitra.getUsername() + "'";
            ResultSet rs = connect.getConnection().createStatement().executeQuery(sql);
            if (rs.next()) {
                Vector column = cigistDao.getColumn("mst_mitra", connect.getConnection());
                String QueryInsert = cigistDao.cigistQueryUpdate("mst_mitra", column,"mitra_code");
                ps = connect.getConnection().prepareStatement(QueryInsert);
                ps.setString(10, objMitra.getMitraCode());
                
                ps.setString(1, objMitra.getUsername());
                ps.setString(2, objMitra.getPassword());
                ps.setString(3, objMitra.getFlagActive());
                ps.setString(4, objMitra.getUserCreate());
                ps.setDate(5, objMitra.getDateCreate());
                ps.setTime(6, objMitra.getTimeCreate());
                ps.setString(7, objMitra.getUserUpdate());
                ps.setDate(8, objMitra.getDateUpdate());
                ps.setTime(9, objMitra.getTimeUpdate());

            } else {
                Vector column = cigistDao.getColumn("mst_mitra", connect.getConnection());
                String QueryInsert = cigistDao.cigistQueryInsert("mst_mitra", column);
                ps = connect.getConnection().prepareStatement(QueryInsert);
                ps.setString(1, objMitra.getMitraCode());
                ps.setString(2, objMitra.getUsername());
                ps.setString(3, objMitra.getPassword());
                ps.setString(4, objMitra.getFlagActive());
                ps.setString(5, objMitra.getUserCreate());
                ps.setDate(6, objMitra.getDateCreate());
                ps.setTime(7, objMitra.getTimeCreate());
                ps.setString(8, objMitra.getUserUpdate());
                ps.setDate(9, objMitra.getDateUpdate());
                ps.setTime(10, objMitra.getTimeUpdate());
                
                ps.executeUpdate();
            }
            ps.close();
            connect.closeConnection();
            return true;
        } catch (IOException | SQLException ex) {
            Logger.getLogger(MstMitraDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

}
