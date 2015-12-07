/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import edu.eci.pdsw.services.ServicesFacade;
import java.sql.SQLException;
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
    private boolean confir;

    public boolean isConfir() {
        return confir;
    }

    public void setConfir(boolean confir) {
        this.confir = confir;
    }
    
    /*Getter de la constraseña del profesor
     * @return string de contraseña
     */
    public String getPassword() {
        return password;
    }
    
    /*Setter de la constraseña del profesor
     * 
     */
    public void setPassword(String password) {
        this.password = password;
    }

   
    /*Getter del usuario del profesor
     * @return usuario del profesor
     */
    public String getUser() {
        return username;
    }
 
    /*Setter del usuario del profesor
     * @return usuario del profesor
     */
    public void setUser(String usern) {
        this.username = usern;

    }
    
    public void confirm() throws SQLException{
        ServicesFacade sf = ServicesFacade.getInstance("applicationconfig.properties");
        String passwordDB = sf.getPassword(username);
        confir = password.equals(passwordDB);
    }
}
