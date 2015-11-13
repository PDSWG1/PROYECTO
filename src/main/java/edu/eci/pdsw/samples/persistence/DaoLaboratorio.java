/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.persistence;

import java.util.Set;
import edu.eci.pdsw.entities.Laboratorio;
import edu.eci.pdsw.entities.Reserva;

/**
 *
 * @author Cesar
 */
public interface DaoLaboratorio {
    
    public Set<Reserva> reservaEsperadar(int semana) throws PersistenceException;
    
    public Laboratorio getLaboratorio(String nombre) throws PersistenceException;

    public void insertReserva(Reserva rv) throws PersistenceException;

    public Set<Reserva> reservaLabSemanDia(String laboratorio, int semana, int dia) throws PersistenceException;

    public Set<Laboratorio> getAlllabs()  throws PersistenceException;
}
