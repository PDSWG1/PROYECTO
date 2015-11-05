/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.entities;


import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author 2087793
 */
public class Reserva {
    private int id;
    private Date fecha;
    private Set<String> bloques;
    private Profesor profesor;
    private Laboratorio laboratorio;
    private int Semana;
    
    public Reserva(int id, Date fecha, Profesor profesor, Laboratorio laboratorio, int semana) {
        this.id=id;
        this.fecha=fecha;
        this.profesor=profesor;
        this.laboratorio=laboratorio;
        this.Semana=semana;
         bloques=new LinkedHashSet<>();
}

    public int getSemana() {
        return Semana;
    }

    public void setSemana(int Semana) {
        this.Semana = Semana;
    }
    
    
    
    public int getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public Set<String> getBloques() {
        return bloques;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setBloques(Set<String> bloques) {
        this.bloques = bloques;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }
    
    
    
}
