/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import edu.eci.pdsw.samples.persistence.PersistenceException;
import edu.eci.pdsw.services.ServicesFacade;
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
    private String nomLabs = "B0";
    private String[][] horario;
    
    
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
    
    /*Getter del número de la semana universitaria
     * @return número de la semana universitaria
     */
    public String getNombre(){
        return nomLabs;
    }
    
    /*Setter del número de la semana universitaria a buscar
     * @param número de la semana universitaria a buscar
     */
    public void setNombre(String nombLab){
        this.nomLabs = nombLab;
    }
    
    /*Getter del horario bajo semana y laboratorio
     * @return número de la semana universitaria
     */        
    public String[][] getHorario() throws PersistenceException {
        return ServicesFacade.getInstance("applicationconfig.properties").getReservasSemanaYLaboratorio(semana, nomLabs);
    }
    
    /*Setter del horario bajo semana y laboratorio
     * @param número de la semana universitaria a buscar
     */
    public void setHorario(String[][] horario) {
        this.horario = horario;
    }
       
}