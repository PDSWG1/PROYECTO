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
import static java.lang.reflect.Array.set;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.Iterator;
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
        Set<Reserva> ans = null;
        if (0 < semana && semana < 17) {
            DaoFactory df = DaoFactory.getInstance(properties);
        
            df.beginSession();
       
            DaoLaboratorio dpro = df.getDaoLaboratorio();
        
            ans = dpro.reservaEsperadar(semana);
        
            df.commitTransaction();
        
            df.endSession();
        }
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
    private boolean reservaHorarioValido(Reserva rv) throws ServiceFacadeException, PersistenceException, SQLException{
        //iter variable que contiene el set de bloques
        Set<Integer> iter=rv.getBloques();
        //variable de salida tipo booleano
        boolean bol = true;
        for(Integer r:iter) {      
            if (!(r>0 && r<9)){
                bol=false;
                break;
            }
        }
        return bol;
        
    }
    /**
     *
     * @param rv todos los datos de la reserva a consultar
     * @return Bool (True) si el bloque no esta caducado, Bool(False) si el bloque  esta caducado
     * @throws ServiceFacadeException
     * @throws PersistenceException
     * @throws SQLException
     */
    /**
    public boolean reservabloquevalido(Reserva rv)  throws SQLException, ServiceFacadeException, PersistenceException {
        //iter variable que contiene el set de bloques
        Set<Integer> iter=rv.getBloques();
        //variable de salida tipo booleano
        boolean bol = true;
        Calendar calendario = new GregorianCalendar();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minutos = calendario.get(Calendar.MINUTE);
        for(Integer r:iter) {      
            if (!(r>0 && r<9)){
                bol=false;
                break;
            }
        }
        return bol;

        
    }
    **/
    public Set<String> transformadorBloque(int bloque){
        Set<String> bloques=new LinkedHashSet();
        if (bloque==1){
            bloques.add("7:00am-8:30am");   
        }
        else if (bloque==2){
            bloques.add("8:30am-10:00am");   
        }
        else if (bloque==3){
            bloques.add("10:00am-11:30pm");   
        }
        else if (bloque==4){
            bloques.add("11:30pm-1:00pm");   
        }
        else if (bloque==5){
            bloques.add("1:00pm-2:30pm");   
        }
        else if (bloque==6){
            bloques.add("2:30pm-4:00pm");   
        }
        else if (bloque==7){
            bloques.add("4:00pm-5:30pm");   
        }
        else if (bloque==8){
            bloques.add("5:30pm-7:00pm");   
        }
        return bloques;
    }
    public void semanavalido()  throws SQLException, ServiceFacadeException, PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates. 
    }

    public Laboratorio infoLaboratorio(String nombre) throws PersistenceException, SQLException {
        DaoFactory df = DaoFactory.getInstance(properties);

        df.beginSession();

        DaoLaboratorio dpro = df.getDaoLaboratorio();

        Laboratorio ans = dpro.getLaboratorio(nombre);

        df.commitTransaction();

        df.endSession();
        return ans;
    }

    public void insertReserva(Reserva rv) throws PersistenceException, SQLException, ServiceFacadeException {
        if (reservaHorarioValido(rv) && reservaSemanaDiaValido(rv) && reservaNumBloquesValido(rv)){
            DaoFactory df = DaoFactory.getInstance(properties);

            df.beginSession();

            DaoLaboratorio dpro = df.getDaoLaboratorio();

            dpro.insertReserva(rv);

            df.commitTransaction();

            df.endSession();
        }
        
    }

    private boolean reservaSemanaDiaValido(Reserva rv) throws PersistenceException, SQLException{
        boolean boo = true;
        DaoFactory df = DaoFactory.getInstance(properties);

        df.beginSession();

        DaoLaboratorio dpro = df.getDaoLaboratorio();

        Set<Reserva> ans = dpro.reservaLabSemanDia(rv.getLaboratorio().getNombreLab(), rv.getSemana(), rv.getDia());
        
        Iterator<Reserva> i = ans.iterator();
        
        while (i.hasNext()){
            Reserva r = i.next();
            boo = true;
            Iterator<Integer> j = r.getBloques().iterator();
            while (j.hasNext()){
                boo &= rv.getBloques().contains(j.next());
            }
        }

        df.commitTransaction();

        df.endSession();
        
        return boo;
    }

    private boolean reservaNumBloquesValido(Reserva rv) {
        int siz = rv.getBloques().size();
        return (0 < siz && siz < 5);
    }
}
