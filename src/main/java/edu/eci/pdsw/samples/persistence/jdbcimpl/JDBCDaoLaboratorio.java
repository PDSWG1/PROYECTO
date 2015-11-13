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
import java.util.Iterator;
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
    
    private int pint(String s){return Integer.parseInt(s);}
    /**
     *
     * @param semana recibe como parametro la semana que quiere consultar
     * @return
     * @throws PersistenceException
     */
    @Override
    public Set<Reserva> reservaEsperadar(int semana) throws PersistenceException{
        try{
            PreparedStatement ps;
            Set<Reserva> ans = new LinkedHashSet<>();
            Reserva rv; 
            Profesor pr;
            Laboratorio lb;
            Asignatura asi = null;
            Software soft;

            //Consulta de las reservas que existen en la semana ingresada como parámetro 
            ps = con.prepareStatement("SELECT rv.id, rv.fechaRadicado, rv.dia, rv.semana, rv.asignatura, "
                    + "rv.laboratorio_nombre, rv.profesores_codigo, pr.nombre, pr.codigoNombre, pr.email, "
                    + "pr.telefono, pr.cedula, lb.numComputadores, lb.encargado "
                    + "FROM RESERVAS AS rv, PROFESORES AS pr, LABORATORIOS AS lb "
                    + "WHERE rv.laboratorio_nombre = lb.nombre AND rv.profesores_codigo = pr.codigo "
                    + "AND rv.semana = ?");
            ps.setInt(1, semana);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Set<Asignatura> asis = new LinkedHashSet<>();
                Set<Integer> blo = new LinkedHashSet<>();
                Set<Software> sos = new LinkedHashSet<>();
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

                //Consulta y creación de las asignaturas del profesor 
                ps = con.prepareStatement("SELECT asi.mnemonico, asi.nombre, asi.creditos "
                        + "FROM ASIGNATURAS AS asi,  PROFESORESASIGNATURAS AS pro "
                        + "WHERE asi.mnemonico = pro.asignaturas_mnemonico "
                        + "AND pro.profesores_codigo = ?");
                ps.setLong(1, rs.getLong(7));
                ResultSet rs1 = ps.executeQuery();
                while (rs1.next()){
                    /**
                     * asi.mnemonico --> 1 STRING
                     * asi.nombre --> 2 STRING
                     * asi.creditos --> 3 INT
                     */
                    asi = new Asignatura(rs1.getString(1), rs1.getString(2), rs1.getInt(3));
                    asis.add(asi);
                }

                pr = new Profesor(rs.getLong(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getLong(11), rs.getLong(12), asis);

                //consulta y creación de los software que tiene cada laboratorio 
                ps = con.prepareStatement("SELECT lab.laboratorio_nombre, soft.nombre, soft.licencia, soft.version, soft.urlDown "
                        + "FROM LABSOFT AS lab, SOFTWARES AS soft "
                        + "WHERE lab.softwares_nombre = soft.nombre "
                        + "AND lab.laboratorio_nombre = ?");
                ps.setString(1, rs.getString(6));
                rs1 = ps.executeQuery();
                while (rs1.next()){
                    /**
                     * lab.laboratorio_nombre --> 1 BIGINT
                     * soft.nombre --> 2 STRING
                     * soft.licencia --> 3 STRING
                     * soft.version --> 4 INT
                     * soft.urlDown --> 5 STRING
                     */
                    soft = new Software(rs1.getString(2), rs1.getString(3), rs1.getString(4), rs1.getString(5));
                    sos.add(soft);
                }

                lb = new Laboratorio(rs.getString(6), rs.getInt(13), rs.getString(14), sos);

                //consulta y creación de los horarios(Bloques) de cada reserva 
                ps = con.prepareStatement("SELECT reservas_id, numero "
                        + "FROM BLOQUES "
                        + "WHERE reservas_id = ?");
                ps.setInt(1, pint(rs.getString(1)));
                rs1 = ps.executeQuery();
                while (rs1.next()){
                    /**
                     * reservas_id --> 1 BIGINT
                     * numero --> 2 INT
                     */
                    blo.add(rs1.getInt(2));
                }

                //Consulta y creación de la asignatura de la cual se hizo la reserva del laboratorio 
                ps = con.prepareStatement("SELECT mnemonico, nombre, creditos "
                        + "FROM ASIGNATURAS "
                        + "WHERE mnemonico = ?");
                ps.setString(1, rs.getString(5));
                rs1 = ps.executeQuery();
                while (rs1.next()){
                    /**
                     * mnemonico --> 1 STRING
                     * nombre --> 2 STRING
                     * creditos --> 3 INT
                     */
                    asi = new Asignatura(rs1.getString(1), rs1.getString(2), rs1.getInt(3));
                }

                //creación de la reserva
                rv = new Reserva(pint(rs.getString(1)), rs.getDate(2), pr, lb, semana, rs.getInt(3), blo, asi);
                ans.add(rv);
            }
            return ans;
        }catch(SQLException ex){
            throw new PersistenceException("An error ocurred while loading a reservation.",ex);
        }
        
    }
    
    /**
     *
     * @param nombre
     * @return
     * @throws PersistenceException
     */
    @Override
    public Laboratorio getLaboratorio(String nombre) throws PersistenceException{
        try{
            Laboratorio lb = null;
            Software so;
            PreparedStatement ps;

            ps = con.prepareStatement("SELECT nombre, numComputadores, encargado "
                    + "FROM LABORATORIOS "
                    + "WHERE nombre = ?");
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Set<Software> sos = new LinkedHashSet<>();
                /**
                 * lb.nombre --> 1
                 * lb.numComputadores --> 2
                 * lb.encargado --> 3
                 **/
                ps = con.prepareStatement("SELECT sof.nombre, sof.licencia, sof.version, sof.urlDown "
                    + "FROM LABSOFT AS ls, SOFTWARES AS sof "
                    + "WHERE ls.softwares_nombre = sof.nombre "
                    + "AND ls.laboratorio_nombre = ?");
                ps.setString(1, nombre);
                ResultSet rs1 = ps.executeQuery();
                while (rs1.next()){
                    /**
                     * sof.nombre --> 1
                     * sof.licencia --> 2
                     * sof.version --> 3 
                     * sof.urlDown --> 4
                     */
                    so = new Software(rs1.getString(1), rs1.getString(2), rs1.getString(3), rs1.getString(4));
                    sos.add(so);
                }
                lb = new Laboratorio(rs.getString(1), rs.getInt(2), rs.getString(3), sos);
            }
            return lb;
        }catch(SQLException ex){
            throw new PersistenceException("An error ocurred while loading a laboratory.",ex);
        }
               
    }

    @Override
    public void insertReserva(Reserva rv) throws PersistenceException{
        try{
            PreparedStatement ps;
            ps = con.prepareStatement("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, rv.getId());
            ps.setDate(2, rv.getFecha());
            ps.setInt(3, rv.getSemana());
            ps.setInt(4, rv.getDia());
            ps.setString(5, rv.getAsignatura().getId());
            ps.setString(6, rv.getLaboratorio().getNombreLab());
            ps.setLong(7, rv.getProfesor().getCodigo());
            ps.execute();

            Iterator<Integer> i = rv.getBloques().iterator();

            ps = con.prepareStatement("INSERT INTO BLOQUES (reservas_id, numero) "
                    + "VALUES (?, ?)");
            int key = rv.getId();
            while (i.hasNext()){
                ps.setInt(1, key);
                ps.setInt(2, i.next());
                ps.execute();
            }
        }catch(SQLException ex){
            throw new PersistenceException("An error ocurred while save a reservation.",ex);
        }
    }

    @Override
    public Set<Reserva> reservaLabSemanDia(String laboratorio, int semana, int dia) throws PersistenceException{
        try{
            PreparedStatement ps;
            Set<Reserva> ans = new LinkedHashSet<>();
            Reserva rv; 
            Profesor pr;
            Laboratorio lb;
            Asignatura asi = null;
            Software soft;

            //Consulta de las reservas que existen en la semana ingresada como parámetro 
            ps = con.prepareStatement("SELECT rv.id, rv.fechaRadicado, rv.dia, rv.semana, rv.asignatura, "
                    + "rv.laboratorio_nombre, rv.profesores_codigo, pr.nombre, pr.codigoNombre, pr.email, "
                    + "pr.telefono, pr.cedula, lb.numComputadores, lb.encargado "
                    + "FROM RESERVAS AS rv, PROFESORES AS pr, LABORATORIOS AS lb "
                    + "WHERE rv.laboratorio_nombre = lb.nombre AND rv.profesores_codigo = pr.codigo "
                    + "AND rv.semana = ? AND rv.laboratorio_nombre = ? AND rv.dia = ?");
            ps.setInt(1, semana);
            ps.setString(2, laboratorio);
            ps.setInt(3, dia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Set<Asignatura> asis = new LinkedHashSet<>();
                Set<Integer> blo = new LinkedHashSet<>();
                Set<Software> sos = new LinkedHashSet<>();
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

                //Consulta y creación de las asignaturas del profesor 
                ps = con.prepareStatement("SELECT asi.mnemonico, asi.nombre, asi.creditos "
                        + "FROM ASIGNATURAS AS asi,  PROFESORESASIGNATURAS AS pro "
                        + "WHERE asi.mnemonico = pro.asignaturas_mnemonico "
                        + "AND pro.profesores_codigo = ?");
                ps.setLong(1, rs.getLong(7));
                ResultSet rs1 = ps.executeQuery();
                while (rs1.next()){
                    /**
                     * asi.mnemonico --> 1 STRING
                     * asi.nombre --> 2 STRING
                     * asi.creditos --> 3 INT
                     */
                    asi = new Asignatura(rs1.getString(1), rs1.getString(2), rs1.getInt(3));
                    asis.add(asi);
                }

                pr = new Profesor(rs.getLong(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getLong(11), rs.getLong(12), asis);

                //consulta y creación de los software que tiene cada laboratorio 
                ps = con.prepareStatement("SELECT lab.laboratorio_nombre, soft.nombre, soft.licencia, soft.version, soft.urlDown "
                        + "FROM LABSOFT AS lab, SOFTWARES AS soft "
                        + "WHERE lab.softwares_nombre = soft.nombre "
                        + "AND lab.laboratorio_nombre = ?");
                ps.setString(1, rs.getString(6));
                rs1 = ps.executeQuery();
                while (rs1.next()){
                    /**
                     * lab.laboratorio_nombre --> 1 BIGINT
                     * soft.nombre --> 2 STRING
                     * soft.licencia --> 3 STRING
                     * soft.version --> 4 INT
                     * soft.urlDown --> 5 STRING
                     */
                    soft = new Software(rs1.getString(2), rs1.getString(3), rs1.getString(4), rs1.getString(5));
                    sos.add(soft);
                }

                lb = new Laboratorio(rs.getString(6), rs.getInt(13), rs.getString(14), sos);

                //consulta y creación de los horarios(Bloques) de cada reserva 
                ps = con.prepareStatement("SELECT reservas_id, numero "
                        + "FROM BLOQUES "
                        + "WHERE reservas_id = ?");
                ps.setInt(1, pint(rs.getString(1)));
                rs1 = ps.executeQuery();
                while (rs1.next()){
                    /**
                     * reservas_id --> 1 BIGINT
                     * numero --> 2 INT
                     */
                    blo.add(rs1.getInt(2));
                }

                //Consulta y creación de la asignatura de la cual se hizo la reserva del laboratorio 
                ps = con.prepareStatement("SELECT mnemonico, nombre, creditos "
                        + "FROM ASIGNATURAS "
                        + "WHERE mnemonico = ?");
                ps.setString(1, rs.getString(5));
                rs1 = ps.executeQuery();
                while (rs1.next()){
                    /**
                     * mnemonico --> 1 STRING
                     * nombre --> 2 STRING
                     * creditos --> 3 INT
                     */
                    asi = new Asignatura(rs1.getString(1), rs1.getString(2), rs1.getInt(3));
                }

                //creación de la reserva
                rv = new Reserva(pint(rs.getString(1)), rs.getDate(2), pr, lb, semana, rs.getInt(3), blo, asi);
                ans.add(rv);
            }
            return ans;
        }catch(SQLException ex){
            throw new PersistenceException("An error ocurred while loading a reservation.",ex);
        }
    }

    /**
     *
     * @return
     * @throws edu.eci.pdsw.samples.persistence.PersistenceException
     */
    @Override
    public Set<Laboratorio> getAlllabs() throws PersistenceException{
        try{
            Set<Laboratorio> ans = new LinkedHashSet<>();
            Laboratorio lb;
            Software so;
            PreparedStatement ps;

            ps = con.prepareStatement("SELECT nombre, numComputadores, encargado "
                    + "FROM LABORATORIOS ");
  
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Set<Software> sos = new LinkedHashSet<>();  
                /**
                 * lb.nombre --> 1
                 * lb.numComputadores --> 2
                 * lb.encargado --> 3
                 **/
                ps = con.prepareStatement("SELECT sof.nombre, sof.licencia, sof.version, sof.urlDown "
                    + "FROM LABSOFT AS ls, SOFTWARES AS sof "
                    + "WHERE ls.softwares_nombre = sof.nombre "
                    + "AND ls.laboratorio_nombre = ?"); 
                ps.setString(1, rs.getString(1));
                ResultSet rs1 = ps.executeQuery();
                while (rs1.next()){
                    /**
                     * sof.nombre --> 1
                     * sof.licencia --> 2
                     * sof.version --> 3 
                     * sof.urlDown --> 4
                     */
                    so = new Software(rs1.getString(1), rs1.getString(2), rs1.getString(3), rs1.getString(4));  
                    sos.add(so);
                }
                lb = new Laboratorio(rs.getString(1), rs.getInt(2), rs.getString(3), sos); 
                ans.add(lb); 
            }
            return ans;
        }catch(SQLException ex){
            throw new PersistenceException("An error ocurred while loading all laboratories.",ex);
        }
        
    }

}