/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cigist.framework.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class CigistDao {

    StringBuilder sBuild = new StringBuilder();
    private String setQuery = "";
    private String setValuez = null;
    private String setData = null;
    private String setColumn = "";
    private HashMap<String, String> getResultz = new HashMap();
    private String whereClause = "";
    private String paramz = "";
    private String paramzCond = "";

    public Vector getColumn(String tableName, Connection conn) {
        sBuild.setLength(0);
        try {
            Vector column = new Vector<>();
            setQuery = sBuild.append("SELECT * FROM ").append(tableName).toString();
            ResultSet rs = conn.createStatement().executeQuery(setQuery);
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                column.add(rsmd.getColumnName(i));
            }
            return column;
        } catch (SQLException ex) {
            Logger.getLogger(CigistDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Vector<Vector> getAllContent(String tableName, Connection conn) {
        sBuild.setLength(0);
        Vector<Vector> vectorList = new Vector<>();
        try {
            Vector data = new Vector<>();
            setQuery = sBuild.append("SELECT * FROM ").append(tableName).toString();
            ResultSet rs = conn.createStatement().executeQuery(setQuery);
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                data = new Vector();
                for (int j = 0; j < rsmd.getColumnCount(); j++) {
                    data.add(rs.getString(j));
                }
                vectorList.add(data);
            }
            return vectorList;
        } catch (SQLException ex) {
            Logger.getLogger(CigistDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String insertData(Connection conn, String table, Vector column, Object obj) {
        sBuild.setLength(0);
        PreparedStatement ps = null;
        Vector value = new Vector();
        for (int i = 0; i < column.size(); i++) {
            value.add("?");
        }
        setColumn = column.toString().replace("[", "(").replace("]", ")");
        setValuez = value.toString().replace("[", "(").replace("]", ")");
        String Query = sBuild.append("INSERT INTO ").append(table).append(setColumn).append("VALUES").append(setValuez).toString();
        System.out.println(Query);

        try {
            ps = conn.prepareStatement(Query);

            ps.execute();

            return "1";//true;
        } catch (SQLException ex) {
            Logger.getLogger(CigistDao.class.getName()).log(Level.SEVERE, null, ex);
            return Query;//ex.getMessage();//false;
        }
    }

    public String cigistQueryInsert(String table, Vector column) {
        sBuild.setLength(0);
        PreparedStatement ps = null;
        Vector value = new Vector();
        for (int i = 0; i < column.size(); i++) {
            value.add("?");
        }
        setColumn = column.toString().replace("[", "(").replace("]", ")");
        setValuez = value.toString().replace("[", "(").replace("]", ")");
        String queryInsert = sBuild.append("INSERT INTO ").append(table).append(setColumn).append("VALUES").append(setValuez).toString();

        return queryInsert;
    }

    public String cigistQueryUpdate(String table, Vector column, String... args) {
        sBuild.setLength(0);
        String whereValue;
        Vector whereColumn = new Vector<>();
        Vector data = new Vector<>();
        data = new Vector();
        for (int a = 0; a < args.length; a++) {
            data.add(args[a]);
        }
        whereColumn.add(data);
        whereValue = whereColumn.toString().replace("[", "").replace("]", "");
        sBuild.setLength(0);
        String whereParam = sBuild.append(whereValue).append(",").toString();
        String setParamnData = whereParam.replace(",", "=? AND");
    
        sBuild.setLength(0);
        setColumn = column.toString().replace("[", "").replace("]", "").replace(whereValue, "");
        String setData = setColumn.substring(2, setColumn.length());
        String paramData = sBuild.append(setData).append(",").toString();
        String setWhere = paramData.replace(",", "=?,");
        
        sBuild.setLength(0);
        String whereData = sBuild.append("UPDATE ").append(table).append(" SET ").append(setWhere).append("WHERE").toString();
        String reData = whereData.replace(",WHERE", " WHERE ");

        sBuild.setLength(0);
        String query = sBuild.append(reData).append(setParamnData).toString();
        int queryLeng=query.length()-3;
        String queryUpdate=query.substring(0,queryLeng);
        
        return queryUpdate;
    }
}
