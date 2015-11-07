/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.entities;


import java.util.LinkedHashSet;
import java.util.Set;


/**
 *
 * @author 2087793
 */
public class Laboratorio {
    private String nombreLab;
    private int numerocomputadores;
    private String encargado;
    private Set<Software> labsoftware;
    
    public Laboratorio(String nombreLab, int numerocomputadores, String encargado, Set<Software> labsoftware) {
        this.nombreLab=nombreLab;
        this.numerocomputadores=numerocomputadores;
        this.encargado=encargado;
        this.labsoftware=labsoftware;
        
    }    

    public String getNombreLab() {
        return nombreLab;
    }

    public int getNumerocomputadores() {
        return numerocomputadores;
    }

    public String getEncargado() {
        return encargado;
    }

    public Set<Software> getLabsoftware() {
        return labsoftware;
    }
    

    public void setNombreLab(String nombreLab) {
        this.nombreLab = nombreLab;
    }

    public void setNumerocomputadores(int numerocomputadores) {
        this.numerocomputadores = numerocomputadores;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public void setLabsoftware(Set<Software> labsoftware) {
        this.labsoftware = labsoftware;
    }
    
    
    
    
    
}
