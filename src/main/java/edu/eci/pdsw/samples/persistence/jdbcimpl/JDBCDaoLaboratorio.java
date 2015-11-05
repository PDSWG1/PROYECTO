/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.persistence.jdbcimpl;

import com.mysql.fabric.xmlrpc.base.Data;
import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import edu.eci.pdsw.samples.persistence.DaoLaboratorio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import edu.eci.pdsw.entities.Laboratorio;
import edu.eci.pdsw.entities.Profesor;
import edu.eci.pdsw.entities.Reserva;

/**
 *
 * @author Cesar
 */
public class JDBCDaoLaboratorio implements DaoLaboratorio{
    
    Connection con;

    public JDBCDaoLaboratorio(Connection con) {
        this.con = con;
    }
    
    /**
     *
     * @param semana
     * @return
     * @throws SQLException
     * @throws PersistenceException
     */
    @Override
    public Set<Reserva> reservaEspe(int semana) throws SQLException, PersistenceException{
        PreparedStatement ps;
        Set<Reserva> ans = null;
        Reserva rv;
        Profesor pr;
        Laboratorio lb;
        /**
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            pr = DaoFactory.getInstance(null).getDaoProfesor().getProfesor(rs.getInt(6));
            lb = DaoFactory.getInstance(null).getDaoLaboratorio().getLaboratorio(rs.getString(5));
            //rv = new Reserva(rs.getInt(1), rs.getDate(2), pr, lb);
        }
        * */
        return ans;
        
    }
    
    /**
     *
     * @param nombre
     * @return
     * @throws SQLException
     */
    @Override
    public Laboratorio getLaboratorio(String nombre) throws SQLException{
        Laboratorio lb = null;
        PreparedStatement ps;
        ps = con.prepareStatement("SELECT nombre, numComputadores, encargado FROM laboratorio WHERE  nombre = ?");
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            lb = new Laboratorio(rs.getString(1), rs.getInt(2), rs.getString(3));
        }
        return lb;
    }
    
    
}
