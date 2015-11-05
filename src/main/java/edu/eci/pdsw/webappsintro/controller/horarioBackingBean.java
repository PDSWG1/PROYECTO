/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    
    /*Getter del día de la semana
     * @param fecha de la reserva
     */
    public int getDayOfTheWeek(Date d){
	GregorianCalendar cal = new GregorianCalendar();
	cal.setTime(d);
	return cal.get(Calendar.DAY_OF_WEEK);		
    }
    
}