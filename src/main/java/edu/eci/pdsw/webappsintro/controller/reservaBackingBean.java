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
import edu.eci.pdsw.entities.Software;
import edu.eci.pdsw.entities.booString;
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
    private int semana = 1;
    private Laboratorio laboratorio;
    private Set<String> selectedBloques;
    private boolean repetir;
    private boolean booFiltro = false;
    private int dia = 1;
    private int numComputadores = 0;
    private int numComputadoresReserva;
    private int index;
    private Set<String> softs;
    private ArrayList<ArrayList<ArrayList<booString>>> horario;
    private ArrayList<ArrayList<ArrayList<booString>>> horarioDisponible;
    private String asis;
    private Set<Asignatura> asignaturas;

    public String getAsis() {
        return asis;
    }

    public void setAsis(String asis) {
        this.asis = asis;
    }

    public Set<Asignatura> getAsignaturas() throws PersistenceException {
        asignaturas = ServicesFacade.getInstance("applicationconfig.properties").getProfesor(Integer.parseInt(loginBackingBean.username)).getAsignatura();
        return asignaturas;
    }
    
    public ArrayList<ArrayList<ArrayList<booString>>> getHorarioDisponible() {
        return horarioDisponible;
    }

    public void setHorarioDisponible(ArrayList<ArrayList<ArrayList<booString>>> horarioDisponible) {
        this.horarioDisponible = horarioDisponible;
    }

    public boolean isBooFiltro() {
        return booFiltro;
    }

    public void setBooFiltro(boolean booFiltro) {
        this.booFiltro = booFiltro;
    }

    public int getIndex() {
        return index;
    }
    
    public int getNumComputadoresReserva() {
        return numComputadoresReserva;
    }

    public void setNumComputadoresReserva(int numComputadoresReserva) {
        this.numComputadoresReserva = numComputadoresReserva;
    }
    
    public int getNumComputadores() {
        return numComputadores;
    }

    public void setNumComputadores(int numComputadores) {
        this.numComputadores = numComputadores;
    }

    public Set<String> getSofts() {
        return softs;
    }

    public void setSofts(Set<String> softs) {
        this.softs = softs;
    }

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
            
    public ArrayList<booString> getBloques(){
        return ServicesFacade.getInstance("applicationconfig.properties").getBloques();
    }

    public ArrayList<ArrayList<ArrayList<booString>>> getHorario() throws PersistenceException {
        this.horario = ServicesFacade.getInstance("applicationconfig.properties").mostrarInformacionTabla(semana);
        return horario;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public int getSemana() {
        return this.semana;
    }
        
    public void setSemana(int semana) throws PersistenceException {
        this.semana = semana;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }
    
    public Set<Laboratorio> getLaboratorios() throws PersistenceException {
        return ServicesFacade.getInstance("applicationconfig.properties").getAllLabs();
    }
    
    public void makeReserva() throws PersistenceException{
        //Prueba de que inserta en la base de datos
        ServicesFacade sf = ServicesFacade.getInstance("applicationconfig.properties");
        Set<Integer> transformadorBloqueInteger = ServicesFacade.transformadorBloqueInteger(selectedBloques);
        java.util.Date now = new java.util.Date();
        Date fecha = new Date(now.getYear(), now.getMonth(), now.getDay());
        Profesor pr = sf.getProfesor(Integer.parseInt(loginBackingBean.username));
        index = sf.getIndexReserva();
        Asignatura asiss = null;
        for (Asignatura a : asignaturas){
            if(a.getId().equals(asis)){
                asiss = a;
            }
        }
        Reserva rv = new Reserva(index, fecha, pr, laboratorio, semana, dia, transformadorBloqueInteger , asiss, numComputadoresReserva);
        if(repetir){
            sf.insertReservaReplay(rv);
        }else{
            sf.insertReserva(rv);
        }
        booFiltro = false;
    }
    
    public void makeFiltro() throws PersistenceException{
        booFiltro = true;
        this.horarioDisponible = ServicesFacade.getInstance("applicationconfig.properties").getDispFiltroSoftwareNumCompu(numComputadores, softs, semana);
    }
    
    public Set<Software> getAllSoftwares(){
        return ServicesFacade.getInstance("applicationconfig.properties").getAllSoftware();
    }

}