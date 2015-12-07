/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.services;

import edu.eci.pdsw.entities.Laboratorio;
import edu.eci.pdsw.entities.Profesor;
import edu.eci.pdsw.entities.Reserva;
import edu.eci.pdsw.entities.Software;
import edu.eci.pdsw.entities.booString;
import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.DaoLaboratorio;
import edu.eci.pdsw.samples.persistence.DaoProfesor;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.reflect.Array.set;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * @throws PersistenceException
     */
    public Set<Reserva> reservaEsperadar(int semana) throws PersistenceException{
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
     * @return Bool (True) si el bloque es valido, Bool(False) si el bloque no esta en los Set<Reserva> ans = new LinkedHashSet<>();
        Reserva rv; 
        Profesor pr;
        Laboratorio lb;
        Asignatura asi = null;
        Software soft;parametros institucionales
     * @throws ServiceFacadeException
     * @throws PersistenceException
     * @throws SQLException
     */
    private boolean reservaHorarioValido(Reserva rv) throws PersistenceException{
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
    public static ArrayList<booString> transformadorBloqueString(int bloque){
        ArrayList<booString> bloques=new ArrayList<>();
        switch (bloque) {
            case 1:
                bloques.add(new booString("7:00am-8:30am"));
                break;
            case 2:
                bloques.add(new booString("8:30am-10:00am"));
                break;
            case 3:
                bloques.add(new booString("10:00am-11:30pm"));
                break;
            case 4:
                bloques.add(new booString("11:30pm-1:00pm"));
                break;
            case 5:
                bloques.add(new booString("1:00pm-2:30pm"));
                break;
            case 6:
                bloques.add(new booString("2:30pm-4:00pm"));
                break;
            case 7:
                bloques.add(new booString("4:00pm-5:30pm"));
                break;
            case 8:   
                bloques.add(new booString("5:30pm-7:00pm"));
                break;
            default:
                break;
        }
        return bloques;
    }
    
        /**
     * Envia el string hora del bloque dado 
     * @param bloque
     * @return bloque transformado en su string en horas
    **/
    public static Set<Integer> transformadorBloqueInteger(Set<String> bloque){
        Set<Integer> bloquesSelected=new HashSet<>();
        for (String s: bloque){
            switch (s) {
                case "7:00am-8:30am":
                    bloquesSelected.add(1);
                    break;
                case "8:30am-10:00am":
                    bloquesSelected.add(2);
                    break;
                case "10:00am-11:30pm":
                    bloquesSelected.add(3);
                    break;
                case "11:30pm-1:00pm":
                    bloquesSelected.add(4);
                    break;
                case "1:00pm-2:30pm":
                    bloquesSelected.add(5);
                    break;
                case "2:30pm-4:00pm":
                    bloquesSelected.add(6);
                    break;
                case "4:00pm-5:30pm":
                    bloquesSelected.add(7);
                    break;
                case "5:30pm-7:00pm":   
                    bloquesSelected.add(8);
                    break;
                default:
                    break;
            }
        }
        return bloquesSelected;
    }
    
    /**
     * consulta el laboratorio con el nombre que se envia como parametro
     * @param nombre, id del laboratorio el cual se quiere consultar
     * @return Laboratorio
     * @throws PersistenceException
     */
    public Laboratorio infoLaboratorio(String nombre) throws PersistenceException {
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
     */
    public void insertReserva(Reserva rv) throws PersistenceException {
        if (reservaHorarioValido(rv) && reservaSemanaDiaValido(rv) && reservaNumBloquesValido(rv) && semanaValida(rv.getSemana())){
            DaoFactory df = DaoFactory.getInstance(properties);

            df.beginSession();

            DaoLaboratorio dpro = df.getDaoLaboratorio();

            dpro.insertReserva(rv);

            df.commitTransaction();

            df.endSession();
        }else{
            Logger.getLogger(ServicesFacade.class.getName()).log(Level.SEVERE, "No se puede realizar la reserva", "No se puede realizar la reserva");
        }
    }
    
    /**
     * verifica que no hayan bloques en otra semana que se crucen con los bloques de la reserva que se va a ingresar
     * @param rv, Reserva
     * @return boolean que dice si es posible o no hacer el ingreso
     * @throws PersistenceException
     * @throws SQLException
     */
    private boolean reservaSemanaDiaValido(Reserva rv) throws PersistenceException{
        boolean boo = true;
        DaoFactory df = DaoFactory.getInstance(properties);

        df.beginSession();

        DaoLaboratorio dpro = df.getDaoLaboratorio();

        Set<Reserva> ans = dpro.reservaLabSemanDia(rv.getLaboratorio().getNombreLab(), rv.getSemana(), rv.getDia());
        
        for (Reserva r : ans){
            boo = true;
            for (int j : r.getBloques()){
                boo &= rv.getNumcomputadores()+r.getNumcomputadores() <= rv.getLaboratorio().getNumerocomputadores();
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
     */
    private boolean reservaNumBloquesValido(Reserva rv) {
        int siz = rv.getBloques().size();
        return (0 < siz && siz < 5);
    }
    
    /**
     * verifica que la semana de la reserva sea valida  (0 < semana && semana < 17)
     * @param rv, Reserva
     * @return boolean que dice si es posible o no hacer el ingreso
     */    
    private boolean semanaValida(int semana){
        return (0 < semana && semana < 17);
    }

    
    /**
     * Insertar la reserva  y verificar si es posible replicarlar hasta finalizar las semanas
     * @param rv todos los datos de la reserva a consultar
     * @throws PersistenceException
     */
    public void insertReservaReplay(Reserva rv) throws PersistenceException {
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
    
    private ArrayList<String> AString(String s){
        ArrayList<String> a = new ArrayList<>();
        a.add(s);
        return a;
    }
    
    private static final String[][] matrizLLena = new String[48][6];
    private int lb, dia;
    private ArrayList<ArrayList<ArrayList<booString>>> a;
    private ArrayList<ArrayList<booString>> days;
    private ArrayList<booString> labs;
    private static final ArrayList<booString> bloques = new ArrayList<>();
    static{
        for(String[] s: matrizLLena){
            Arrays.fill(s, "Disponible");
        }
        for (int i = 1; i < 9; i++){
            bloques.add(transformadorBloqueString(i).get(0));
        }
    }
    
    public ArrayList<booString> getBloques(){
        return bloques;
    }
    /**
     *
     * @param semana
     * @return 
     * @throws PersistenceException
     */
    public ArrayList<ArrayList<ArrayList<booString>>> mostrarInformacionTabla(int semana) throws PersistenceException{
        booString[][] matriz = new booString[48][6];
        int[][] sumas = new int[48][6];
        for (int i = 0; i < 48; i++){
            for (int j = 0; j < 6; j++){
                matriz[i][j] = new booString("Disponible");
                sumas[i][j] = 0;
            }
        }
        
        Set<Reserva> ans = reservaEsperadar(semana);
        for (Reserva r :ans){
            lb = numLaboratorio(r.getLaboratorio().getNombreLab());
            dia = r.getDia()-1;
            for (int i : r.getBloques()){
                sumas[lb+((i-1)*6)][dia] += r.getNumcomputadores();
                if (matriz[lb+((i-1)*6)][dia].getLista().get(0).equals("Disponible")){
                    matriz[lb+((i-1)*6)][dia] = new booString(r.getAsignatura().getId()+" "+r.getProfesor().getCodigoNombre());
                }else{
                    matriz[lb+((i-1)*6)][dia].setLista(r.getAsignatura().getId()+" "+r.getProfesor().getCodigoNombre());
                }
            }
        }
        Set<Laboratorio> la = getAllLabs();
        int[] lab = new int[7];
        for (Laboratorio l: la){
            lab[numLaboratorio(l.getNombreLab())] = l.getNumerocomputadores();
        }
        a = new ArrayList<>();
        for (int k = 0; k < 8; k++){
            days = new ArrayList<>();
            days.add(transformadorBloqueString(k+1));
            for(int i = 0; i < 7; i++){
                labs = new ArrayList<>();
                if(i==0){
                    labs.add(new booString("BO"));
                    labs.add(new booString("Ingenieria de Software"));
                    labs.add(new booString("Multimedia y Moviles"));
                    labs.add(new booString("Plataformas Computacionales"));
                    labs.add(new booString("Redes de Computadores"));
                    labs.add(new booString("Aula Inteligente"));
                    days.add(labs);
                }else{
                    for (int j = 0; j < 6; j++){
                        matriz[(k*6)+j][i-1].setLista("#PC "+(lab[j]-sumas[(k*6)+j][i-1]));
                        labs.add(matriz[(k*6)+j][i-1]);
                    }
                days.add(labs);
                }
            }
            a.add(days);
        }
        return a;
    }
    
    private int numLaboratorio(String laboratorio){
        int ans = 0;
        switch (laboratorio){
            case "B0":
                ans = 0;
                break;  
            case "Ingenieria de Software":
                ans = 1;
                break;  
            case "Multimedia y Moviles":
                ans = 2;
                break;  
            case "Plataformas Computacionales":
                ans = 3;
                break;  
            case "Redes de Computadores":
                ans = 4;
                break;  
            case "Aula Inteligente":
                ans = 5;
                break;  
            default:
                break;    
        }
        return ans;
    }
    
    public Set<Laboratorio> getAllLabs() throws PersistenceException{
        
        DaoFactory df = DaoFactory.getInstance(properties);

        df.beginSession();

        DaoLaboratorio dpro = df.getDaoLaboratorio();

        Set<Laboratorio> ans = dpro.getAlllabs();

        df.commitTransaction();

        df.endSession();
        
        return ans;
    }
    
    public Profesor getProfesor(int n) throws PersistenceException{
        Profesor ans = null;
        try{
            DaoFactory df = DaoFactory.getInstance(properties);
            
            df.beginSession();

            DaoProfesor dpro = df.getDaoProfesor();

            ans = dpro.getProfesor(n);

            df.commitTransaction();

            df.endSession();
        } catch (SQLException ex) {
            Logger.getLogger(ServicesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ans;
    }
    
    public boolean reservaLabDisponible(Set<Integer> bloques, Laboratorio laboratorio, int semana, int dia, int numcomputadores)  throws PersistenceException,SQLException{
        
        boolean boo=true;
        DaoFactory df = DaoFactory.getInstance(properties);

        df.beginSession();
        
        DaoLaboratorio dpro = df.getDaoLaboratorio();
        
        Integer[] ans = dpro.reservaLabDisponible(bloques,laboratorio,semana,dia,numcomputadores);
        
        for(int i=1;i<9 && boo;i++){
            if (ans[i]+numcomputadores>laboratorio.getNumerocomputadores()){
                boo=false;
                
            }
        }
        df.commitTransaction();
        df.endSession();
        return boo;
        
    }   

    public ArrayList<ArrayList<ArrayList<booString>>> getDispFiltroSoftwareNumCompu(int numComputadores, Set<String> softs, int semana) throws PersistenceException {
        Set<Reserva> ans = reservaEsperadar(semana);
        int[][] matriz1 = new int[48][6];
        for (Reserva r :ans){
            lb = numLaboratorio(r.getLaboratorio().getNombreLab());
            dia = r.getDia()-1;
            for (int i : r.getBloques()){
                if(matriz1[lb+((i-1)*6)][dia] + r.getNumcomputadores() + numComputadores <= r.getLaboratorio().getNumerocomputadores()){
                    matriz1[lb+((i-1)*6)][dia] += r.getNumcomputadores();
                }else{
                    matriz1[lb+((i-1)*6)][dia] = Integer.MAX_VALUE;
                }
            }
        }
        Set<Laboratorio> lab = getAllLabs();
        Laboratorio[] estosSon = new Laboratorio[6];
        Arrays.fill(estosSon, null);

        for (Laboratorio l: lab){
            boolean boo = true;
            lb = numLaboratorio(l.getNombreLab());
            Set<Software> gSoft = l.getLabsoftware();
            Set<String> nombres = new HashSet<>();
            for (Software s: gSoft){
                nombres.add(s.getNombre());
            }
            for (String s:softs){
                boo &= nombres.contains(s);
            }
            if(boo){
                estosSon[lb] = l;
            }
        }
        for (int k = 0; k < 8; k++){
            for(int i = 2; i < 8; i++){
                for (int j = 0; j < 6; j++){
                    if(matriz1[(k*6)+j][j] != Integer.MAX_VALUE && estosSon[j] != null){
                        a.get(k).get(i).get(j).setDisponible(true);
                    }
                }
            }
        } 
        return a;
    }

    public Set<Software> getAllSoftware() {
        Set<Software> ans = null;
        try{
            DaoFactory df = DaoFactory.getInstance(properties);
            
            df.beginSession();

            DaoLaboratorio dpro = df.getDaoLaboratorio();

            ans = dpro.getAllSoftware();

            df.commitTransaction();

            df.endSession();
        } catch (PersistenceException ex) {
            Logger.getLogger(ServicesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ans;
    }
    
    public Software getSoftware(String nombre) {
        Software ans = null;
        try{
            DaoFactory df = DaoFactory.getInstance(properties);
            
            df.beginSession();

            DaoLaboratorio dpro = df.getDaoLaboratorio();

            ans = dpro.getSoftware(nombre);

            df.commitTransaction();

            df.endSession();
        } catch (PersistenceException ex) {
            Logger.getLogger(ServicesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ans;
    }

    public int getIndexReserva() {
        int ans = 0;
        try{
            DaoFactory df = DaoFactory.getInstance(properties);
            
            df.beginSession();

            DaoLaboratorio dpro = df.getDaoLaboratorio();

            ans = dpro.getIndexReserva();

            df.commitTransaction();

            df.endSession();
        } catch (PersistenceException ex) {
            Logger.getLogger(ServicesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ans;
    }
    
    // hacer un funcion que traiga el horario semana y laboratorio
    public String[][] getReservasSemanaYLaboratorio(int semana, String laboratorio) throws PersistenceException{
        String[][] ans1 = new String[8][6];
        try{
            Set<Reserva> ans;
            DaoFactory df = DaoFactory.getInstance(properties);
            
            df.beginSession();

            DaoLaboratorio dpro = df.getDaoLaboratorio();
            
            for (String[] s : ans1){
                Arrays.fill(s, "Disponible");
            }
            
            for (int dia = 1; dia < 7; dia++){
                ans = dpro.reservaLabSemanDia(laboratorio, semana, dia);
                for (Reserva r :ans){
                    for (int i : r.getBloques()){
                        if(ans1[i][dia-1].equals("Disponible")){
                            ans1[i][dia-1] = r.getAsignatura().getId()+" "+r.getProfesor().getCodigoNombre()+"; ";
                        }else{
                            ans1[i][dia-1] += r.getAsignatura().getId()+" "+r.getProfesor().getCodigoNombre()+"; ";
                        }
                    }
                }
            }

            df.commitTransaction();

            df.endSession();
            
        }catch (PersistenceException ex) {
            Logger.getLogger(ServicesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ans1;
    }
   
    // mostrar disponibles
    
    // mostrar cuantos computadores hay disponible
    
    //mas pruebas a al filtro

    public String getPassword(String username) throws SQLException {
        String ans = null;
        try{
            DaoFactory df = DaoFactory.getInstance(properties);
            
            df.beginSession();

            DaoProfesor dpro = df.getDaoProfesor();
            
            ans = dpro.getPassword(username);

            df.commitTransaction();

            df.endSession();
            
        }catch (PersistenceException ex) {
            Logger.getLogger(ServicesFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ans;
    }
    
    
}
