/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.persistence.jdbcimpl;

import edu.eci.pdsw.samples.persistence.PersistenceException;
import edu.eci.pdsw.samples.persistence.DaoProfesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.eci.pdsw.entities.Profesor;

/**
 *
 * @author Cesar
 */
public class JDBCDaoProfesor implements DaoProfesor{
    
    Connection con;

    public JDBCDaoProfesor(Connection con) {
        this.con = con;
    }
    
    /**
     *
     * @param n
     * @return
     * @throws SQLException
     * @throws PersistenceException
     */
    @Override
    public Profesor getProfesor(int n) throws SQLException, PersistenceException{
        
        return null;
    }
        
}
