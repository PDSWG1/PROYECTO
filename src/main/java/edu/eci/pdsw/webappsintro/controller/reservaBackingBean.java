/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import edu.eci.pdsw.entities.Laboratorio;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import edu.eci.pdsw.services.ServicesFacade;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author 2096898
 */
@ManagedBean(name = "beanReserva")
@SessionScoped
public class reservaBackingBean {
    private int semana = 0;
    private Set<Laboratorio> laboratorios;
    private Laboratorio laboratorio;

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public void setSemana(int semana) {
        this.semana = semana;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }
    
    public Set<Laboratorio> getLaboratorios() throws PersistenceException {
        return ServicesFacade.getInstance("applicationconfig.properties").getAllLabs();
    }
}