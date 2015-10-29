/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdsw.entities;


import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author 2087793
 */
public class Reserva {
    private int id;
    private Timestamp fecha;
    private Set<String> bloques;
    private int profesor;
    private String laboratorio;
    
    public Reserva(int id, Timestamp fecha, int profesor, String laboratorio) {
        this.id=id;
        this.fecha=fecha;
        this.profesor=profesor;
        this.laboratorio=laboratorio;
         bloques=new LinkedHashSet<>();
}

    public int getId() {
        return id;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public Set<String> getBloques() {
        return bloques;
    }

    public int getProfesor() {
        return profesor;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public void setBloques(Set<String> bloques) {
        this.bloques = bloques;
    }

    public void setProfesor(int profesor) {
        this.profesor = profesor;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }
    
    
    
}
