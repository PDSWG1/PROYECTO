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

@ManagedBean(name = "beanHorario")
@SessionScoped
public class horarioBackingBean {
    private int semana = 0;
    private String nomLaboratorio;
    private List<Laboratorio> labs;
    private Laboratorio selectLabs;
    
    @PostConstruct
    public void init() {
//        /*
        labs = new ArrayList<>();

        labs.add(new Laboratorio("Plataformas Computacionales", 20, "Johan Ramirez", null));
        labs.add(new Laboratorio("Laboratorio de Software", 24, "Alejandra Blanco", null));
        labs.add(new Laboratorio("Redes de Computadores", 20, "Cesar Vega", null));
        labs.add(new Laboratorio("Multimedia y Móviles", 18, "Luis Felipe Díaz", null));
        labs.add(new Laboratorio("Sala Inteligente", 30, "Tiffany Estupiñán", null));   
        //*/
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
    
    /*Getter del nombre de laboratorio que desea consultar
     * @param elección del laboratorio que desea consultar
     */
    public String getNombLaboratorio() {
        return nomLaboratorio;
    }
     
    /*Setter del nombre de laboratorio que desea consultar
     * @param elección del laboratorio que desea consultar
     */
    
    public void setNombreLaboratorio(String nomLab){
        this.nomLaboratorio = nomLab;
    }
    
    public List<Laboratorio> getLaboratorios() {
        return labs;
    }
    
}