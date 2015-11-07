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
public class Profesor {
    private long codigo;
    private String nombre;
    private String codigoNombre;
    private String email;
    private long telefono;
    private long cedula;
    private Set<Asignatura> asignatura;
    
    
    public Profesor(long codigo, String nombre, String codigoNombre, String email,long telefono,long cedula, Set<Asignatura> asignatura) {
        this.codigo=codigo;
        this.nombre=nombre;
        this.codigoNombre=codigoNombre;
        this.email=email;
        this.telefono=telefono;
        this.cedula=cedula;
        this.asignatura=asignatura;
}

    public long getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigoNombre() {
        return codigoNombre;
    }

    public String getEmail() {
        return email;
    }

    public long getTelefono() {
        return telefono;
    }

    public long getCedula() {
        return cedula;
    }

    public Set<Asignatura> getAsignatura() {
        return asignatura;
    }



    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCodigoNombre(String codigoNombre) {
        this.codigoNombre = codigoNombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public void setCedula(long cedula) {
        this.cedula = cedula;
    }

    public void setAsignatura(Set<Asignatura> asignatura) {
        this.asignatura = asignatura;
    }
    
    
    
    
}
