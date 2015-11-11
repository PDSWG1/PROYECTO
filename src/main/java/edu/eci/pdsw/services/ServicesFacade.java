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
     * Traer los laboratorios de la semana de reserva
     * @param semana es el numero de la semana que se quiere consultar
     * @return
     * @throws ServiceFacadeException
     * @throws PersistenceException
     * @throws SQLException
     */
    public Set<Reserva> reservaEsperadar(int semana) throws ServiceFacadeException, PersistenceException, SQLException{
        Set<Reserva> ans = null;
        if (semanaValida(semana)) {
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
     * Verificar si  los bloques son validos (r>0 && r<9)
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
     * Envia el string hora del bloque dado 
     * @param bloque
     * @return bloque transformado en su string en horas
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
    
    /**
     * consulta el laboratorio con el nombre que se envia como parametro
     * @param nombre, id del laboratorio el cual se quiere consultar
     * @return Laboratorio
     * @throws PersistenceException
     * @throws SQLException
     */
    public Laboratorio infoLaboratorio(String nombre) throws PersistenceException, SQLException {
        DaoFactory df = DaoFactory.getInstance(properties);

        df.beginSession();

        DaoLaboratorio dpro = df.getDaoLaboratorio();

        Laboratorio ans = dpro.getLaboratorio(nombre);

        df.commitTransaction();

        df.endSession();
        return ans;
    }

    /**
     * Envia la reserva que se va a ingresar a la base de datos
     * @param rv, Reserva que se va a ingresar a la base de datos
     * @throws PersistenceException
     * @throws SQLException
     * @throws ServiceFacadeException
     */
    public void insertReserva(Reserva rv) throws PersistenceException, SQLException, ServiceFacadeException {
        if (reservaHorarioValido(rv) && reservaSemanaDiaValido(rv) && reservaNumBloquesValido(rv) && semanaValida(rv.getSemana())){
            DaoFactory df = DaoFactory.getInstance(properties);

            df.beginSession();

            DaoLaboratorio dpro = df.getDaoLaboratorio();

            dpro.insertReserva(rv);

            df.commitTransaction();

            df.endSession();
        }
        
    }
    
    /**
     * verifica que no hayan bloques en otra semana que se crucen con los bloques de la reserva que se va a ingresar
     * @param rv, Reserva
     * @return boolean que dice si es posible o no hacer el ingreso
     * @throws PersistenceException
     * @throws SQLException
     */
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
    
    /**
     * verifica que el numero de bloques sea valido (0 < siz && siz < 5)
     * @param rv, Reserva
     * @return boolean que dice si es posible o no hacer el ingreso
     * @throws PersistenceException
     * @throws SQLException
     */
    private boolean reservaNumBloquesValido(Reserva rv) {
        int siz = rv.getBloques().size();
        return (0 < siz && siz < 5);
    }
    
    /**
     * verifica que la semana de la reserva sea valida  (0 < semana && semana < 17)
     * @param rv, Reserva
     * @return boolean que dice si es posible o no hacer el ingreso
     * @throws PersistenceException
     * @throws SQLException
     */    
    private boolean semanaValida(int semana){
        return (0 < semana && semana < 17);
    }

    
    /**
     * Insertar la reserva  y verificar si es posible replicarlar hasta finalizar las semanas
     * @param rv todos los datos de la reserva a consultar
     * @throws PersistenceException
     * @throws SQLException
     * @throws ServiceFacadeException
     */
    public void insertReservaReplay(Reserva rv) throws PersistenceException, SQLException, ServiceFacadeException {
        int semanaReplay=rv.getSemana();
        int idReplay=rv.getId();
        
        boolean bol=true;
        for(int i=semanaReplay;i<17 && bol;i++){
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
        }
    }
}
