/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.entities;

import java.util.ArrayList;

/**
 *
 * @author Cesar
 */
public class booString {
    
    public ArrayList<String> lista;
    public boolean disponible = false;
    
    public booString(String s){
        lista = new ArrayList<>();
        lista.add(s);
    }

    public ArrayList<String> getLista() {
        return lista;
    }
        
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int ban = 0;
        for (String s : this.lista){
            str.append(s);
            if(ban + 1 != lista.size())str.append("; ");
            ban++;
        }
        return str.toString();
    }

    public void setLista(String s) {
        this.lista.add(s);
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }    
}
