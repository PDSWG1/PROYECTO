/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.persistence.jdbcimpl;

import edu.eci.pdsw.samples.PersistenceException;
import edu.eci.pdsw.samples.persistence.DaoProfesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pdsw.entities.Profesor;

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
        PreparedStatement ps;
        Profesor pro = null;
        ps = con.prepareStatement("SELECT codigo, nombre, codigoNombre, email, telefono, cedula FROM profesores WHERE codigo = ?");
        ps.setInt(1, n);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            pro = new Profesor(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6));
        }
        return pro;
    }
        
}