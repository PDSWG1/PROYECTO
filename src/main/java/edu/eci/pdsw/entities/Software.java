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
public class Software {
    
     private String nombre;
    private String licencia;
    private String version;
    private String urlDown;
    
    public Software(String nombre, String licencia, String version, String urlDown) {
        this.nombre=nombre;
        this.licencia=licencia;
        this.version=version;
        this.urlDown=urlDown;
      
}

    public String getNombre() {
        return nombre;
    }

    public String getLicencia() {
        return licencia;
    }

    public String getVersion() {
        return version;
    }

    public String getUrlDown() {
        return urlDown;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setUrlDown(String urlDown) {
        this.urlDown = urlDown;
    }
    
    
    
    
    
    
}
