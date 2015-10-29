/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import java.util.LinkedList;
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
    private LinkedList<String> labs = new LinkedList<>();
    
    /*
     * Creación de lista con nombres de laboratorios en la decanatura de Ingeniería de Sistemas
     */
    
    public void creacionLaboratorios(){
        labs.add("Plataformas Computacionales");
        labs.add("Laboratorio de Informática");
        labs.add("Redes");
        labs.add("Sala Doble Plataformas y Redes");
        
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
    
    /*Getter del nombre de laboratorio
     * @return nombre del laboratorio
     */
    public LinkedList<String> getLaboratorios(){
        return labs;
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
       
    
}
