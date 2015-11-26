/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.persistence.jdbcimpl;

import edu.eci.pdsw.entities.Asignatura;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import edu.eci.pdsw.samples.persistence.DaoProfesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.eci.pdsw.entities.Profesor;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.jsp.jstl.sql.Result;

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
        try{
            Profesor pr = null;
            PreparedStatement ps;
            Set<Asignatura> asis;
            Asignatura asi;
            ps = con.prepareStatement("SELECT codigo, nombre, codigoNombre, email, telefono, cedula "
                    + "FROM PROFESORES "
                    + "WHERE codigo = ?");
            ps.setInt(1, n);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                asis = new HashSet<>();
                /**
                 * codigo --> 1 
                 * nombre --> 2 
                 * codigoNombre --> 3 
                 * email --> 4 
                 * telefono --> 5 
                 * cedula --> 6
                 **/
                ps = con.prepareStatement("SELECT asi.mnemonico, asi.nombre, asi.creditos "
                        + "FROM PROFESORESASIGNATURAS AS pas, ASIGNATURAS AS asi "
                        + "WHERE pas.asignaturas_mnemonico = asi.mnemonico "
                        + "AND pas.profesores_codigo = ?");
                ps.setInt(1,n);
                ResultSet rs1 = ps.executeQuery();
                while(rs1.next()){
                    /**
                     * asi.mnemonico --> 1
                     * asi.nombre --> 2
                     * asi.creditos --> 3
                     */
                    asi = new Asignatura(rs1.getString(1), rs1.getString(2), rs1.getInt(3));
                    asis.add(asi);
                }
                pr = new Profesor(rs.getLong(1),rs.getString(2), rs.getString(3),rs.getString(4), rs.getLong(5), rs.getLong(6), asis);
                
            }
            return pr;
        }catch(SQLException ex){
            throw new PersistenceException("An error ocurred while loading all laboratories.",ex);
        }
    }
        
}
