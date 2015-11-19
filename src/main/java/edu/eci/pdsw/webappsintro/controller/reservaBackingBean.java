/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import edu.eci.pdsw.entities.Laboratorio;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import edu.eci.pdsw.services.ServicesFacade;
import java.util.ArrayList;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author 2096898
 */
@ManagedBean(name = "beanReserva")
@SessionScoped
public class reservaBackingBean {
    private int semana = 1;
    private Set<Laboratorio> laboratorios;
    private Laboratorio laboratorio;
    private ArrayList<ArrayList<ArrayList<String>>> horario;

    public ArrayList<ArrayList<ArrayList<String>>> getHorario() throws PersistenceException {
        return ServicesFacade.getInstance("applicationconfig.properties").mostrarInformacionTabla(semana);
    }

    public void setHorario(ArrayList<ArrayList<ArrayList<String>>> horario) {
        this.horario = horario;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public int getSemana() {
        return this.semana;
    }
        
    public void setSemana(int semana) {
        this.semana = semana;
        System.out.println(semana);
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
        System.out.println(laboratorio.getNombreLab());
    }
    
    public Set<Laboratorio> getLaboratorios() throws PersistenceException {
        return ServicesFacade.getInstance("applicationconfig.properties").getAllLabs();
    }
}