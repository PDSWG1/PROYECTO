/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdsw.entities;

import java.sql.Timestamp;



/**
 *
 * @author 2087793
 */
public class Solicitud {
    private int id;
    private String nombrelab; 
    private Timestamp fecha;
    private boolean estado;
    private String comentario;
    private String laboratorio;
    private int codigoprofesor;
    
    public Solicitud(int id,String nombrelab, Timestamp fecha,boolean estado, String comentario, String laboratorio,int codigoprofesor) {
        this.id=id;
        this.fecha=fecha;
        this.nombrelab=nombrelab;
        this.estado=estado;
        this.comentario=comentario;
        this.laboratorio=laboratorio;
        this.codigoprofesor=codigoprofesor;
}

    public int getId() {
        return id;
    }

    public String getNombrelab() {
        return nombrelab;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public boolean isEstado() {
        return estado;
    }

    public String getComentario() {
        return comentario;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public int getCodigoprofesor() {
        return codigoprofesor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombrelab(String nombrelab) {
        this.nombrelab = nombrelab;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public void setCodigoprofesor(int codigoprofesor) {
        this.codigoprofesor = codigoprofesor;
    }
    
    
    
    
    
    
    
}
