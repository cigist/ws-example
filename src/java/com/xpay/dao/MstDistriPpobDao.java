/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpay.dao;

import com.xpay.model.MstDistriPpob;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author irwan cigist /irwancigist@gmail.com
 */
public class MstDistriPpobDao {

    private static SessionFactory factory;

    public MstDistriPpobDao() {
        try {
            factory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public List<MstDistriPpob> getAll(String distriId) {
        Session session = factory.openSession();
        Transaction tx = null;
        List<MstDistriPpob> listData = new ArrayList<>();
        String SQl_ALL = "SELECT TRN_TYPE,PROVIDER_ID,PRODUCT_TYPE,PRODUCT_ID,DISTRI_PRICE,DISTRI_ADMIN_FEE,DISTRI_FEE,DESCRIPTION,RESPONSE  FROM mst_distri_ppob \n"
                + "where DISTRI_ID='" + distriId + "'";
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(SQl_ALL);
            List<Object[]> rows = query.list();
            for (Object[] row : rows) {
               MstDistriPpob ppob = new MstDistriPpob();
                ppob.setTrnType(row[0].toString());
                ppob.setProviderId(row[1].toString());
                ppob.setProductType(row[2].toString());
                ppob.setProductId(row[3].toString());
                ppob.setDistriPrice(Double.parseDouble(row[4].toString()));
                ppob.setDistriAdminFee(Double.parseDouble(row[5].toString()));
                ppob.setDistriFee(Double.parseDouble(row[6].toString()));
                ppob.setDescription(row[7].toString());
                ppob.setResponese(row[8].toString());
                listData.add(ppob);
            }
            tx.commit();
            return listData;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }

        return listData;
    }

    public List<MstDistriPpob> getByProviderId(String distriId, String providerId) {
        Session session = factory.openSession();
        Transaction tx = null;
        List<MstDistriPpob> listData = new ArrayList<>();
        String SQl_ALL = "SELECT TRN_TYPE,PROVIDER_ID,PRODUCT_TYPE,PRODUCT_ID,DISTRI_PRICE,DISTRI_ADMIN_FEE,DISTRI_FEE,DESCRIPTION,RESPONSE  FROM xpaydb.mst_distri_ppob \n"
                + "where DISTRI_ID='" + distriId + "' AND PROVIDER_ID='" + providerId + "'";
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(SQl_ALL);
            List<Object[]> rows = query.list();
            for (Object[] row : rows) {
               MstDistriPpob ppob = new MstDistriPpob();
                ppob.setTrnType(row[0].toString());
                ppob.setProviderId(row[1].toString());
                ppob.setProductType(row[2].toString());
                ppob.setProductId(row[3].toString());
                ppob.setDistriPrice(Double.parseDouble(row[4].toString()));
                ppob.setDistriAdminFee(Double.parseDouble(row[5].toString()));
                ppob.setDistriFee(Double.parseDouble(row[6].toString()));
                ppob.setDescription(row[7].toString());
                ppob.setResponese(row[8].toString());
                listData.add(ppob);
            }
            tx.commit();
            return listData;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }

        return listData;
    }

    public List<MstDistriPpob> getByProductType(String distriId, String providerId, String poductType) {
        Session session = factory.openSession();
        Transaction tx = null;
        List<MstDistriPpob> listData = new ArrayList<>();
        String SQl_ALL = "SELECT TRN_TYPE,PROVIDER_ID,PRODUCT_TYPE,PRODUCT_ID,DISTRI_PRICE,DISTRI_ADMIN_FEE,DISTRI_FEE,DESCRIPTION,RESPONSE  FROM xpaydb.mst_distri_ppob \n"
                + "where DISTRI_ID='" + distriId + "' AND PROVIDER_ID='" + providerId + "' AND PRODUCT_TYPE='" + poductType + "'";
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(SQl_ALL);
            List<Object[]> rows = query.list();
            for (Object[] row : rows) {
                MstDistriPpob ppob = new MstDistriPpob();
                ppob.setTrnType(row[0].toString());
                ppob.setProviderId(row[1].toString());
                ppob.setProductType(row[2].toString());
                ppob.setProductId(row[3].toString());
                ppob.setDistriPrice(Double.parseDouble(row[4].toString()));
                ppob.setDistriAdminFee(Double.parseDouble(row[5].toString()));
                ppob.setDistriFee(Double.parseDouble(row[6].toString()));
                ppob.setDescription(row[7].toString());
                ppob.setResponese(row[8].toString());
                listData.add(ppob);
            }
            tx.commit();
            return listData;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }

        return listData;
    }
     public List<MstDistriPpob> getByProductTrnType(String distriId,String trnType) {
        Session session = factory.openSession();
        Transaction tx = null;
        List<MstDistriPpob> listData = new ArrayList<>();
        String SQl_ALL = "SELECT TRN_TYPE,PROVIDER_ID,PRODUCT_TYPE,PRODUCT_ID,DISTRI_PRICE,DISTRI_ADMIN_FEE,DISTRI_FEE,DESCRIPTION,RESPONSE  FROM xpaydb.mst_distri_ppob \n"
                + "where DISTRI_ID='" + distriId + "' AND TRN_TYPE='" + trnType + "'";
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(SQl_ALL);
            List<Object[]> rows = query.list();
            for (Object[] row : rows) {
                MstDistriPpob ppob = new MstDistriPpob();
                ppob.setTrnType(row[0].toString());
                ppob.setProviderId(row[1].toString());
                ppob.setProductType(row[2].toString());
                ppob.setProductId(row[3].toString());
                ppob.setDistriPrice(Double.parseDouble(row[4].toString()));
                ppob.setDistriAdminFee(Double.parseDouble(row[5].toString()));
                ppob.setDistriFee(Double.parseDouble(row[6].toString()));
                ppob.setDescription(row[7].toString());
                ppob.setResponese(row[8].toString());
                listData.add(ppob);
            }
            tx.commit();
            return listData;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }

        return listData;
    }

    public Double checkPrice(String distriId,String productCode) {
        Session session = factory.openSession();
        Transaction tx = null;
        Double saldo = 0.0;
        String sql = "select distri_price from mst_distri_ppob where distri_id='" + distriId + "' and product_id='" + productCode + "'";
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            List balance = query.list();
            tx.commit();
            if (!balance.isEmpty()) {
                System.out.println(balance.toString());
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
