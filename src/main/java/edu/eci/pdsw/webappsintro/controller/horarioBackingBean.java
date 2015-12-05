/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import edu.eci.pdsw.entities.Laboratorio;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import edu.eci.pdsw.services.ServicesFacade;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
    private int semana = 1;
    private List<Laboratorio> labs;
    private Laboratorio laboratorio;
    private String nomLabs;
    
    
    /*Getter del número de la semana universitaria
     * @return número de la semana universitaria
     */
    public int getSemana(){
        System.out.println("Estoy en el get " + this.semana);
        return semana;
    }
    
    /*Setter del número de la semana universitaria a buscar
     * @param número de la semana universitaria a buscar
     */
    public void setSemana(int sem){
        this.semana = sem;
    }
    
    /*Getter del número de la semana universitaria
     * @return número de la semana universitaria
     */
    public String getNombre(){
        System.out.println("Estoy en el getName " + this.nomLabs);
        return nomLabs;
    }
    
    /*Setter del número de la semana universitaria a buscar
     * @param número de la semana universitaria a buscar
     */
    public void setNombre(String nombLab){
        this.nomLabs = nombLab;
    }
    
    /*Setter del nombre de laboratorio que desea consultar
     * @param elección del laboratorio que desea consultar
     */
    
    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }
    
    public Set<Laboratorio> getLaboratorios() throws PersistenceException {
        return ServicesFacade.getInstance("applicationconfig.properties").getAllLabs();
    }
    
}