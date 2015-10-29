/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples;

import edu.eci.pdsw.samples.persistence.jdbcimpl.JDBCDaoFactory;

/**
 *
 * @author 2096129
 */
public abstract class DaoFactory {
    
    protected DaoFactory() {
    }

   private static edu.eci.pdsw.samples.persistence.DaoFactory instance = null;
    
    public static edu.eci.pdsw.samples.persistence.DaoFactory getInstance() {
        instance = new JDBCDaoFactory();
        return instance;
    }
    
    
    public abstract void beginSession() throws PersistenceException;

   
    public abstract void commitTransaction() throws PersistenceException;

    public abstract void rollbackTransaction() throws PersistenceException;

    public abstract void endSession() throws PersistenceException;
}
