/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.entities;



/**
 *
 * @author 2087793
 */
public class Asignatura {
   
    private String id;
    private String nombre;
    private int creditos;
    
    
    public Asignatura(String id, String nombre, int creditos) {
        this.id=id;
        this.nombre=nombre;
        this.creditos=creditos;
       
}

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
    
    
    
    
    
    
}
