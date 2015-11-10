/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import edu.eci.pdsw.entities.Laboratorio;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author 2096898
 */

@ManagedBean(name = "beanPruebas")
@SessionScoped
public class pruebaBackingBean {
    
    private List<Laboratorio> labs;
    private Laboratorio selectLabs;
    
    @PostConstruct
    public void init() {
        /*
        labs = new ArrayList<>();

        labs.add(new Laboratorio("Plataformas Computacionales", 20, "Johan Ramirez"));
        labs.add(new Laboratorio("Laboratorio de Software", 24, "Alejandra Blanco"));
        labs.add(new Laboratorio("Redes de Computadores", 20, "Cesar Vega"));
        labs.add(new Laboratorio("Multimedia y Móviles", 18, "Luis Felipe Díaz"));
        labs.add(new Laboratorio("Sala Inteligente", 30, "Tiffany Estupiñán"));   */
    }

    public Laboratorio getSelectedLaboratorio() {
        return selectLabs;
    }
 
    public void setSelectedLaboratorio(Laboratorio selectLab) {
        this.selectLabs = selectLab;
    }
    
    public List<Laboratorio> getLaboratorios() {
        return labs;
    }
 
    public void setLaboratorios(List<Laboratorio> labs) {
        this.labs = labs;
    }
}
