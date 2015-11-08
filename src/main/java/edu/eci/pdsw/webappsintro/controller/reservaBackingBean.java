/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import edu.eci.pdsw.entities.Laboratorio;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author 2096898
 */
@ManagedBean(name = "beanReserva")
@SessionScoped
public class reservaBackingBean {
    private int semana = 0;
    private String nomLaboratorio;
    private List<Laboratorio> labs;
    private Laboratorio selectLabs;
    
    @PostConstruct
    public void init() {
        labs = new ArrayList<>();
/**
        labs.add(new Laboratorio("Plataformas Computacionales", 20, "Johan Ramirez"));
        labs.add(new Laboratorio("Laboratorio de Software", 24, "Alejandra Blanco"));
        labs.add(new Laboratorio("Redes", 20, "Cesar Vega"));
        labs.add(new Laboratorio("Sala Doble", 40, "Tiffany Estupiñán"));**/
        
        
    }
    
    /*Getter del número de la semana universitaria
     * @return número de la semana universitaria
     */
    public int getSemana(){
        return semana;
    }
    
    /*Setter del número de la semana universitaria a buscar
     * @param número de la semana universitaria a buscar
     */
    public void setSemana(int sem){
        this.semana = sem;
    }
      
 
    public List<Laboratorio> getLaboratorios() {
        return labs;
    }
 
    public void setLaboratorios(List<Laboratorio> labs) {
        this.labs = labs;
    }
 
    public Laboratorio getSelectedLaboratorio() {
        return selectLabs;
    }
 
    public void setSelectedLaboratorio(Laboratorio selectLab) {
        this.selectLabs = selectLab;
    }

}