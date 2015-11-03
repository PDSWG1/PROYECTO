/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.persistence.jdbcimpl;

import edu.eci.pdsw.samples.PersistenceException;
import edu.eci.pdsw.samples.DaoFactory;
import edu.eci.pdsw.samples.persistence.DaoLaboratorio;
import edu.eci.pdsw.samples.persistence.DaoProfesor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author 2096129
 */
public class JDBCDaoFactory extends DaoFactory{
    private static final ThreadLocal<Connection> connectionInstance = new ThreadLocal<Connection>() {
    };
 
    private static Connection openConnection() throws PersistenceException{
        //url de nuestra base de datos
            String url="jdbc:mysql://desarrollo.is.escuelaing.edu.co:3306/pdswg1";
            String driver="com.mysql.jdbc.Driver";
            String user="pdswg1";
            String pwd="pdswg01";
                        
        try {
            Class.forName(driver);
            Connection _con=DriverManager.getConnection(url,user,pwd);
            _con.setAutoCommit(false);
            return _con;
        } catch (ClassNotFoundException | SQLException ex) {
            throw new PersistenceException("Error on connection opening.",ex);
        }

    }
    
    @Override
    public void beginSession() throws PersistenceException {
        try {
            if (connectionInstance.get()==null || connectionInstance.get().isClosed()){            
                connectionInstance.set(openConnection());
            }
            else{
                throw new PersistenceException("Session was already opened.");
            }
        } catch (SQLException ex) {
            throw new PersistenceException("An error ocurred while opening a JDBC connection.",ex);
        }
        
    }

    /**
     *
     * @throws PersistenceException
     */
    @Override
    public void endSession() throws PersistenceException {
        try {
            if (connectionInstance.get()==null || connectionInstance.get().isClosed()){
                throw new PersistenceException("Conection is null or is already closed.");
            }
            else{
                connectionInstance.get().close();
            }            
        } catch (SQLException ex) {
            throw new PersistenceException("Error on connection closing.",ex);
        }
    }

    @Override
    public void commitTransaction() throws PersistenceException {
        try {
            if (connectionInstance.get()==null || connectionInstance.get().isClosed()){
                throw new PersistenceException("Conection is null or is already closed.");
            }
            else{
                connectionInstance.get().commit();
            }            
        } catch (SQLException ex) {
            throw new PersistenceException("Error on connection closing.",ex);
        }        
    }
    
    @Override
    public void rollbackTransaction() throws PersistenceException {
        try {
            if (connectionInstance.get()==null || connectionInstance.get().isClosed()){
                throw new PersistenceException("Conection is null or is already closed.");
            }
            else{
                connectionInstance.get().rollback();
            }            
        } catch (SQLException ex) {
            throw new PersistenceException("Error on connection closing.",ex);
        }
    }
    
    /**
     *
     * @return
     * @throws PersistenceException
     */
    @Override
    public DaoProfesor getDaoProfesor() throws PersistenceException{
        return new JDBCDaoProfesor(connectionInstance.get());
    }
    
    @Override
    public DaoLaboratorio getDaoLaboratorio() throws PersistenceException{
        return new JDBCDaoLaboratorio(connectionInstance.get());
    }
}

    

