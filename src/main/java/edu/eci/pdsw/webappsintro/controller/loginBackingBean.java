/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author 2096898
 */

@ManagedBean(name = "beanLogin")
@SessionScoped
public class loginBackingBean {
    private String password; 
    private String username; 
    
    public String getPassword1() {
        System.out.println("contraseña: " + password);
        return password;
    }
 
    public void setPassword1(String password1) {
        this.password = password1;
    }
    
    public String getUser() {
        System.out.println("user: " + username);
        return username;
    }
 
    public void setUser(String usern) {
        this.username = usern;
    }
    
}
