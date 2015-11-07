/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.persistence.jdbcimpl;

import edu.eci.pdsw.entities.Asignatura;
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
import edu.eci.pdsw.entities.Software;
import java.util.LinkedHashSet;

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
     * @param semana recibe como parametro la semana que quiere consultar
     * @return
     * @throws SQLException
     * @throws PersistenceException
     */
    @Override
    public Set<Reserva> reservaEsperadar(int semana) throws SQLException, PersistenceException{
        PreparedStatement ps;
        Set<Reserva> ans = new LinkedHashSet<>();
        Set<Asignatura> asis = new LinkedHashSet<>();
        Set<Integer> blo = new LinkedHashSet<>();
        Set<Software> sos = new LinkedHashSet<>();
        Reserva rv; 
        Profesor pr;
        Laboratorio lb;
        Asignatura asi = null;
        Software soft;
        
        ps = con.prepareStatement("SELECT rv.id, rv.fechaRadicado, rv.dia, rv.semana, rv.asignatura, "
                + "rv.laboratorio_nombre, rv.profesores_codigo, pr.nombre, pr.codigoNombre, pr.email, "
                + "pr.telefono, pr.cedula, lb.numComputadores, lb.encargado "
                + "FROM RESERVAS AS rv, PROFESORES AS pr, LABORATORIOS AS lb "
                + "WHERE rv.laboratorio_nombre = lb.nombre AND rv.profesores_codigo = pr.codigo "
                + "AND rv.semana = ?");
        ps.setInt(1, semana);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            /**
             * rv.id --> 1
             * rv.fechaRadicado --> 2
             * rv.dia --> 3
             * rv.semana --> 4
             * rv.asignatura --> 5
             * rv.laboratorio_nombre --> 6
             * rv.profesores_codigo --> 7
             * pr.nombre --> 8
             * pr.codigoNombre --> 9
             * pr.email --> 10
             * pr.telefono --> 11
             * pr.cedula --> 12
             * lb.numComputadores --> 13
             * lb.encargado --> 14
             */
            ps = con.prepareStatement("SELECT pro.profesores_codigo, asi.mnemonico, asi.nombre, asi.creditos "
                    + "FROM ASIGNATURA AS asi,  profesoresAsignaturas AS pro"
                    + "WHERE asi.mnemonico = pro.asignaturas_mnemonico "
                    + "AND pro.profesores_codigo = ?");
            ps.setLong(1, rs.getLong(7));
            ResultSet rs1 = ps.executeQuery();
            while (rs1.next()){
                asi = new Asignatura(rs1.getString(2), rs1.getString(3), rs1.getInt(4));
                asis.add(asi);
            }
            
            pr = new Profesor(rs.getLong(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getLong(11), rs.getLong(12), asis);
            
            ps = con.prepareStatement("SELECT lab.laboratorio_nombre, soft.nombre, soft.licencia, soft.version, soft.urlDown "
                    + "FROM LOBSOFT AS lab, SOFTWARE AS soft "
                    + "WHERE lab.softwares_nombre = soft.nombre "
                    + "AND lab.laboratorio_nombre = ?");
            ps.setString(1, rs.getString(6));
            rs1 = ps.executeQuery();
            while (rs1.next()){
                soft = new Software(rs1.getString(2), rs1.getString(3), rs1.getString(3), rs1.getString(5));
                sos.add(soft);
            }
            
            lb = new Laboratorio(rs.getString(6), rs.getInt(13), rs.getString(14), sos);
            
            ps = con.prepareStatement("SELECT reservas_id, numero "
                    + "FROM BLOQUES "
                    + "WHERE reservas_id = ?");
            ps.setInt(1, rs.getInt(1));
            rs1 = ps.executeQuery();
            while (rs1.next()){
                blo.add(rs1.getInt(2));
            }
            
            ps = con.prepareStatement("SELECT mnemonico, nombre, creditos "
                    + "FROM ASIGNATURA "
                    + "WHERE mnemonico = ?");
            ps.setString(1, rs.getString(5));
            rs1 = ps.executeQuery();
            while (rs1.next()){
                asi = new Asignatura(rs1.getString(2), rs1.getString(3), rs1.getInt(4));
            }
            
            rv = new Reserva(rs.getInt(1), rs.getDate(2), pr, lb, semana, rs.getInt(2), blo, asi);
        }
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
        /**
        Laboratorio lb = null;
        PreparedStatement ps;
        ps = con.prepareStatement("SELECT nombre, numComputadores, encargado FROM laboratorio WHERE  nombre = ?");
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            lb = new Laboratorio(rs.getString(1), rs.getInt(2), rs.getString(3));
        }
        return lb;
        **/
        return null;
    }
    
    
}
