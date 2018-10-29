/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cigist.framework.core;

import java.util.Map;
import java.util.Vector;



/**
 *
 * @author user
 */
public interface CigistDaoInterface<T> {

    public String isertUpdate(T obj);

    public String delete(T obj);
    
    public Vector<T> getAllData();

    public Vector<T> getDataBy(String fieldName, String value);

    public Vector<T> getIndexKey(String ...args);

}
