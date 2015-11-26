/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import edu.eci.pdsw.entities.Asignatura;
import edu.eci.pdsw.entities.Laboratorio;
import edu.eci.pdsw.entities.Profesor;
import edu.eci.pdsw.entities.Reserva;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import edu.eci.pdsw.services.ServicesFacade;
import java.sql.Date;
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
    private Set<String> selectedBloques;
    private boolean repetir;
    private int dia = 1;

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public boolean isRepetir() {
        return repetir;
    }

    public void setRepetir(boolean repetir) {
        this.repetir = repetir;
    }

    public Set<String> getSelectedBloques() {
        return selectedBloques;
    }

    public void setSelectedBloques(Set<String> selectedBloques) {
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
    
    public void makeReserva() throws PersistenceException{
        //Prueba de que inserta en la base de datos
        Set<Integer> transformadorBloqueInteger = ServicesFacade.transformadorBloqueInteger(selectedBloques);
        java.util.Date now = new java.util.Date();
        Date fecha = new Date(now.getYear(), now.getMonth(), now.getDay());
        Profesor pr = ServicesFacade.getInstance("applicationconfig.properties").getProfesor(2096121);
        System.out.println("-----> "+pr.getCodigo());
        Asignatura as = new Asignatura("PDSW", "Programacion", 4);
        Reserva rv = new Reserva(1, fecha, pr, laboratorio, semana, dia, transformadorBloqueInteger , as);
        if(repetir){
            ServicesFacade.getInstance("applicationconfig.properties").insertReservaReplay(rv);
        }else{
            ServicesFacade.getInstance("applicationconfig.properties").insertReserva(rv);
        }
    }
}