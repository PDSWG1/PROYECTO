/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.entities;

import java.sql.Date;
import java.util.Set;

/**
 *
 * @author 2087793
 */
public class Reserva {
    private int id;
    private Date fecha;
    private Set<Integer> bloques;
    private Asignatura asignatura;
    private Profesor profesor;
    private Laboratorio laboratorio;
    private int semana;
    private int dia;
    private int numcomputadores;

    
    
    public Reserva(int id, Date fecha, Profesor profesor, Laboratorio laboratorio, int semana, int dia, Set<Integer> bloques, Asignatura asignatura,int numcomputadores) {
        this.id=id;
        this.fecha=fecha;
        this.profesor=profesor;
        this.laboratorio=laboratorio;
        this.semana=semana;
        this.dia=dia;
        this.bloques=bloques;
        this.asignatura=asignatura;
        this.numcomputadores=numcomputadores;
}

    public int getSemana() {
        return semana;
    }

    public void setSemana(int semana) {
        this.semana = semana;
    }
    
    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }
    
    public int getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public Set<Integer> getBloques() {
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

    public void setBloques(Set<Integer> bloques) {
        this.bloques = bloques;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }
    public int getNumcomputadores() {
        return numcomputadores;
    }

    public void setNumcomputadores(int numcomputadores) {
        this.numcomputadores = numcomputadores;
    }

    public int getint(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
