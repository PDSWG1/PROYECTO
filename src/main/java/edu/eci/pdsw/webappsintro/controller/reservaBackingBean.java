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
    private int semana = 1, semanaRespaldo= 0;
    private Laboratorio laboratorio;
    private ArrayList<ArrayList<ArrayList<String>>> horario;
    private String[] selectedBloques;

    public String[] getSelectedBloques() {
        return selectedBloques;
    }

    public void setSelectedBloques(String[] selectedBloques) {
        this.selectedBloques = selectedBloques;
    }
            
    public ArrayList<String> getBloques(){
        return ServicesFacade.getInstance("applicationconfig.properties").getBloques();
    }

    public ArrayList<ArrayList<ArrayList<String>>> getHorario() throws PersistenceException {
        if (semana != semanaRespaldo){
            horario = ServicesFacade.getInstance("applicationconfig.properties").mostrarInformacionTabla(semana);
            semanaRespaldo = semana;
        }
        return horario;
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
        
    public void setSemana(int semana) throws PersistenceException {
        this.semana = (semana == 0)? 1: semana;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }
    
    public Set<Laboratorio> getLaboratorios() throws PersistenceException {
        return ServicesFacade.getInstance("applicationconfig.properties").getAllLabs();
    }
}