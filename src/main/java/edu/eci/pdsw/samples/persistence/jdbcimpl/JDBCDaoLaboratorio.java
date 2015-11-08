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
import java.math.BigInteger;
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
    
    private BigInteger pBig(long s){return BigInteger.valueOf(s);}
    private int pint(String s){return Integer.parseInt(s);}
    /**
     *
     * @param semana recibe como parametro la semana que quiere consultar
     * @return
     * @throws SQLException
     * @throws PersistenceException
     */
    @Override
    public Set<Reserva> reservaEsperadar(int semana) throws SQLException, PersistenceException{
        System.out.println("  ");
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
            System.out.println(rs.toString());
            /**
             * rv.id --> 1 BIGINT
             * rv.fechaRadicado --> 2 DATE
             * rv.dia --> 3 INT
             * rv.semana --> 4 INT
             * rv.asignatura --> 5 STRING
             * rv.laboratorio_nombre --> 6 STRING
             * rv.profesores_codigo --> 7 BIGINT
             * pr.nombre --> 8 STRING
             * pr.codigoNombre --> 9 STRING
             * pr.email --> 10 STRING
             * pr.telefono --> 11 BIGINT
             * pr.cedula --> 12 BIGINT
             * lb.numComputadores --> 13 INT
             * lb.encargado --> 14 STRING
             */
            ps = con.prepareStatement("SELECT pro.profesores_codigo, asi.mnemonico, asi.nombre, asi.creditos "
                    + "FROM ASIGNATURAS AS asi,  PROFESORESASIGNATURAS AS pro"
                    + "WHERE asi.mnemonico = pro.asignaturas_mnemonico "
                    + "AND pro.profesores_codigo = ?");
            ps.setLong(1, rs.getLong(7));
            ResultSet rs1 = ps.executeQuery();
            while (rs1.next()){
                System.out.println(rs1.toString());
                /**
                 * pro.profesores_codigo --> 1 BIGINT
                 * asi.mnemonico --> 2 STRING
                 * asi.nombre --> 3 STRING
                 * asi.creditos --> 4 INT
                 */
                asi = new Asignatura(rs1.getString(2), rs1.getString(3), rs1.getInt(4));
                asis.add(asi);
            }
            
            pr = new Profesor(rs.getLong(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getLong(11), rs.getLong(12), asis);
            
            ps = con.prepareStatement("SELECT lab.laboratorio_nombre, soft.nombre, soft.licencia, soft.version, soft.urlDown "
                    + "FROM LABSOFT AS lab, SOFTWARES AS soft "
                    + "WHERE lab.softwares_nombre = soft.nombre "
                    + "AND lab.laboratorio_nombre = ?");
            ps.setString(1, rs.getString(6));
            rs1 = ps.executeQuery();
            while (rs1.next()){
                System.out.println(rs1.toString());
                /**
                 * lab.laboratorio_nombre --> 1 BIGINT
                 * soft.nombre --> 2 STRING
                 * soft.licencia --> 3 STRING
                 * soft.version --> 4 INT
                 * soft.urlDown --> 5 STRING
                 */
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
                System.out.println(rs1.toString());
                /**
                 * reservas_id --> 1 BIGINT
                 * numero --> 2 INT
                 */
                blo.add(rs1.getInt(2));
            }
            
            ps = con.prepareStatement("SELECT mnemonico, nombre, creditos "
                    + "FROM ASIGNATURAS "
                    + "WHERE mnemonico = ?");
            ps.setString(1, rs.getString(5));
            rs1 = ps.executeQuery();
            while (rs1.next()){
                System.out.println(rs1.toString());
                /**
                 * mnemonico --> 1 STRING
                 * nombre --> 2 STRING
                 * creditos --> 3 INT
                 */
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
