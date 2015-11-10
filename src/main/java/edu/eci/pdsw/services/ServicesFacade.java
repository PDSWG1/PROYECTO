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
        if (semanaValida(semana)) {
            DaoFactory df = DaoFactory.getInstance(properties);
        
            df.beginSession();
       
            DaoLaboratorio dpro = df.getDaoLaboratorio();
        
            Set<Reserva> ans = dpro.reservaEsperadar(semana);
        
            df.commitTransaction();
        
            df.endSession();
            
            return ans;
        }else{
            throw new ServiceFacadeException("Semana no valida");
        }
        
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
     * @param bloque
     * @return 
    **/
    public Set<String> transformadorBloque(int bloque){
        Set<String> bloques=new LinkedHashSet();
        switch (bloque) {
            case 1:
                bloques.add("7:00am-8:30am");
                break;
            case 2:
                bloques.add("8:30am-10:00am");
                break;
            case 3:
                bloques.add("10:00am-11:30pm");
                break;
            case 4:
                bloques.add("11:30pm-1:00pm");
                break;
            case 5:
                bloques.add("1:00pm-2:30pm");
                break;
            case 6:
                bloques.add("2:30pm-4:00pm");
                break;
            case 7:
                bloques.add("4:00pm-5:30pm");
                break;
            case 8:   
                bloques.add("5:30pm-7:00pm");
                break;
            default:
                break;
        }
        return bloques;
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
        if (reservaHorarioValido(rv) && reservaSemanaDiaValido(rv) && reservaNumBloquesValido(rv) && semanaValida(rv.getSemana())){
            DaoFactory df = DaoFactory.getInstance(properties);

            df.beginSession();

            DaoLaboratorio dpro = df.getDaoLaboratorio();

            dpro.insertReserva(rv);

            df.commitTransaction();

            df.endSession();
        }else{
            throw new ServiceFacadeException("No se pudo ingresar a la base de datos");
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
    
    private boolean semanaValida(int semana){
        return 0 < semana && semana < 17;
    }

    public void insertReservaReplay(Reserva rv) throws PersistenceException, SQLException, ServiceFacadeException {
        int semana=rv.getSemana();
        int semanaReplay=rv.getSemana();
        int idReplay=rv.getId();
        boolean bol=true;
        
        for(int i=semana;i<17 && bol;i++){
            rv.setSemana(i);
            bol &= (reservaHorarioValido(rv) && reservaSemanaDiaValido(rv) && reservaNumBloquesValido(rv));
        }
        
        if (bol){
            for(int i=semanaReplay;i<17 ;i++){               
                rv.setSemana(i);
                rv.setId(idReplay);                    
                insertReserva(rv);
                idReplay++;
            }    
        }else{
            throw new ServiceFacadeException("No se ingreso la reserva");
        }
    }
}
