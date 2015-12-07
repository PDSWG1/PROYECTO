/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.persistence;

import java.sql.SQLException;
import edu.eci.pdsw.entities.Profesor;

/**
 *
 * @author Cesar
 */
public interface DaoProfesor {
    
    public Profesor getProfesor(int n) throws SQLException, PersistenceException;

    public String getPassword(String username) throws SQLException, PersistenceException;
}
