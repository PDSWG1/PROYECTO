/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.persistence;

import java.util.Set;
import edu.eci.pdsw.entities.Laboratorio;
import edu.eci.pdsw.entities.Reserva;
import java.sql.SQLException;
import edu.eci.pdsw.entities.Software;
/**
 *
 * @author Cesar
 */
public interface DaoLaboratorio {
    
    public Set<Reserva> reservaEsperadar(int semana) throws PersistenceException;
    
    public Laboratorio getLaboratorio(String nombre) throws PersistenceException;

    public void insertReserva(Reserva rv) throws PersistenceException;

    public Set<Reserva> reservaLabSemanDia(String laboratorio, int semana, int dia) throws PersistenceException;
    
    public Integer[] reservaLabDisponible(Set<Integer> bloques,Laboratorio laboratorio,int semana,int dia,int numcomputadores) throws PersistenceException,SQLException;

    public Set<Laboratorio> getAlllabs() throws PersistenceException;

    public Set<Software> getAllSoftware() throws PersistenceException;
    
    public Software getSoftware(String nombre) throws PersistenceException;

    public int getIndexReserva() throws PersistenceException;

}
