/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.services;

import edu.eci.pdsw.entities.Laboratorio;
import edu.eci.pdsw.entities.Reserva;
import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.DaoLaboratorio;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Cesar
 */
public class ServicesFacade {
    
    private static ServicesFacade instance=null;
    
    private final Properties properties=new Properties();
    
    private ServicesFacade(String propFileName) throws IOException{        
	InputStream input = null;
        input = this.getClass().getClassLoader().getResourceAsStream(propFileName);        
        properties.load(input);
    }
    
    public static ServicesFacade getInstance(String propertiesFileName) throws RuntimeException{
        if (instance==null){
            try {
                instance=new ServicesFacade(propertiesFileName);
            } catch (IOException ex) {
                throw new RuntimeException("Error on application configuration:",ex);
            }
        }        
        return instance;
    }
    
    /**
     *
     * @param semana es el numero de la semana que se quiere consultar
     * @return
     * @throws ServiceFacadeException
     * @throws PersistenceException
     * @throws SQLException
     */
    public Set<Reserva> reservaEsperadar(int semana) throws ServiceFacadeException, PersistenceException, SQLException{
        DaoFactory df = DaoFactory.getInstance(properties);
        
        df.beginSession();
       
        DaoLaboratorio dpro = df.getDaoLaboratorio();
        
        Set<Reserva> ans = dpro.reservaEsperadar(semana);
        
        df.commitTransaction();
        
        df.endSession();

        return ans;
    }
    /**
     *
     * @param rv todos los datos de la reserva a consultar
     * @return Bool (True) si el bloque es valido, Bool(False) si el bloque no esta en los parametros institucionales
     * @throws ServiceFacadeException
     * @throws PersistenceException
     * @throws SQLException
     */
    public void reservahorariovalido(Reserva rv) throws ServiceFacadeException, PersistenceException, SQLException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     *
     * @param rv todos los datos de la reserva a consultar
     * @return Bool (True) si el bloque es valido, Bool(False) si el bloque no esta en los parametros institucionales
     * @throws ServiceFacadeException
     * @throws PersistenceException
     * @throws SQLException
     */
    public void reservabloquevalido(Reserva rv)  throws SQLException, ServiceFacadeException, PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. 
    }
    public void semanavalido()  throws SQLException, ServiceFacadeException, PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. 
    }

    public Laboratorio infoLaboratorio(String nombre) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void insertReserva(Reserva rv) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
