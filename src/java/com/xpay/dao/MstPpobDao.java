/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.dao;

import com.cigist.framework.conn.CigistConnection;
import com.cigist.framework.core.CigistDao;
import com.xpay.model.MstPpobPrice;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

/**
 *
 * @author cigist
 */
public class MstPpobDao {

    CigistConnection connect = new CigistConnection();
    CigistDao cigistDao = new CigistDao();
    private static SessionFactory factory;

    public MstPpobDao() {
        try {
            factory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

    }

    public List<MstPpobPrice> getAll() {
        Session session = factory.openSession();
        Transaction tx = null;
        List<MstPpobPrice> listData = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery("select * from mst_ppob");
            List<Object[]> rows = query.list();
            for (Object[] row : rows) {
                MstPpobPrice ppob = new MstPpobPrice();
                ppob.setDistriId(row[0].toString());
                ppob.setProviderId(row[1].toString());
                ppob.setProductId(row[2].toString());
                listData.add(ppob);
            }
            tx.commit();
            return listData;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return listData;
    }

    public Vector<MstPpobPrice> getDataPPOB(String ditriId) {
        Vector<MstPpobPrice> listPricePPOB = new Vector<>();
        String SELECT_PPOB = "SELECT a.distri_id,a.provider_id,a.product_id,b.description,a.distri_price,a.distri_admin_fee,a.distri_fee FROM MST_PRICE_PPOB a"
                + " join MST_PRODUCT_PPOB b on a.product_id=b.product_id where distri_id='" + ditriId + "'";
        try {
            ResultSet rs = connect.getConnection().createStatement().executeQuery(SELECT_PPOB);
            while (rs.next()) {
                MstPpobPrice ppob = new MstPpobPrice();
                ppob.setDistriId(rs.getString(1));
                ppob.setProviderId(rs.getString(2));
                ppob.setProductId(rs.getString(3));
                ppob.setDescription(rs.getString(4));
                ppob.setDistriPrice(rs.getDouble(5));
                ppob.setDistriAdminFee(rs.getDouble(6));
                ppob.setDistriFee(rs.getDouble(7));
                listPricePPOB.add(ppob);
            }

            rs.close();
            connect.closeConnection();
            return listPricePPOB;
        } catch (IOException ex) {
            Logger.getLogger(MstPpobDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MstPpobDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
