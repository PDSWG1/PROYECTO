/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdsw.entities;


import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author 2087793
 */
public class Profesor {
     private int codigo;
    private String nombre;
    private String codigoNombre;
    private String email;
    private int telefono;
    private int cedula;
    private Set<String> asignatura;
    
    
    public Profesor(int codigo, String nombre, String codigoNombre, String email,int telefono,int cedula) {
        this.codigo=codigo;
        this.nombre=nombre;
        this.codigoNombre=codigoNombre;
        this.email=email;
        this.telefono=telefono;
        this.cedula=cedula;
         asignatura=new LinkedHashSet<>();
}

    public int getCodigo() {
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

    public int getTelefono() {
        return telefono;
    }

    public int getCedula() {
        return cedula;
    }

    public Set<String> getAsignatura() {
        return asignatura;
    }

    public void setCodigo(int codigo) {
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

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public void setAsignatura(Set<String> asignatura) {
        this.asignatura = asignatura;
    }
    
    
    
    
}
