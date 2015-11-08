package edu.eci.cosw.sampleapp.test;

import edu.eci.pdsw.entities.Asignatura;
import edu.eci.pdsw.entities.Laboratorio;
import edu.eci.pdsw.entities.Profesor;
import edu.eci.pdsw.entities.Reserva;
import edu.eci.pdsw.entities.Software;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import edu.eci.pdsw.services.ServiceFacadeException;
import edu.eci.pdsw.services.ServicesFacade;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;

public class AppTest {
    
    public AppTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void clearDB() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("delete from ASIGNATURAS");
        stmt.execute("delete from BLOQUES");
        stmt.execute("delete from LABSOFT");
        stmt.execute("delete from LABORATORIOS");
        stmt.execute("delete from PROFESORES");
        stmt.execute("delete from PROFESORESASIGNATURAS");
        stmt.execute("delete from RESERVAS");
        stmt.execute("delete from SOFTWARES");
        stmt.execute("delete from SOLICITUDES");
        conn.commit();
        conn.close();
    }

    /**
     * --> semana es valido (1 <= semana <=16 y !(1 <= semana <=16))
     * @throws SQLException
     * @throws ServiceFacadeException
     * @throws PersistenceException
     */
    @Test
    public void semanaValida() throws SQLException, ServiceFacadeException, PersistenceException {
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO PROFESORES (codigo, nombre, codigoNombre, email, telefono, cedula) "
                + "VALUES (2096724, 'Cesar Vega', 'CEVE', 'cesar.vega-f@mail.escuelaing.edu.co', 3134723073, 1013622836)");
        
        stmt.execute("INSERT INTO ASIGNATURAS (mnemonico, nombre, creditos) "
                + "VALUES ('PDSW', 'Proceso de Desarrollo de Software', 4)");
        
        stmt.execute("INSERT INTO PROFESORESASIGNATURAS (profesores_codigo, asignaturas_mnemonico) "
                + "VALUES (2096724, 'PDSW')");

        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Plataformas', 30, 'Nicolas Gomez')");
        
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo)"
                + "VALUES (1, NOW(), 1, 3, 'PDSW', 'Plataformas', 2096724)");
        
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 1)");
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 2)");
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 3)");        
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 4)");
                
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo)"
                + "VALUES (2, NOW(), 1, 3, 'PDSW', 'Plataformas', 2096724)");
        
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (2, 6)");
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (2, 7)");
        
        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        int semana = 17;
        Set<Reserva> ans = sf.reservaEsperadar(semana);
        Assert.assertTrue("No es valia la semana", ans.isEmpty());
        semana = 1;
        ans = sf.reservaEsperadar(semana);
        Assert.assertTrue("No es valia la semana", ans.size()==2);
    }

    /**
     * --> Seleccione la informaciÃ³n de un laboratorio(verificando el numero de equipos y software disponibles)
     * @throws SQLException
     * @throws ServiceFacadeException
     * @throws PersistenceException
     */
    /**
    @Test
    public void informacionLaboratorio() throws SQLException, ServiceFacadeException, PersistenceException {   
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        
        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Plataformas', 30, 'Nicolas Gomez')");
    
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Unity', '5', '5.6', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Python', '3', '3.4', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO LABSOFT (laboratorio_nombre, softwares_nombre) "
                + "VALUES ('Plataformas', 'Unity')");
        
        stmt.execute("INSERT INTO LABSOFT (laboratorio_nombre, softwares_nombre) "
                + "VALUES ('Plataformas', 'Python')");
        
        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
       
        String nombre = "Plataformas";
        Laboratorio ans = sf.infoLaboratorio(nombre);
        Assert.assertTrue("No es valia la semana", ans.getNumerocomputadores()==30);
        
        Set<String> nombres = new HashSet(); // para comparar
        nombres.add("Unity");
        nombres.add("Python");
        
        Assert.assertTrue("No es la misma informaciÃ³n de Software", nombres.equals(ans.getLabsoftware()));
    }   

    /**
     * --> verificar la reserva de que sea posible realizarla(no en bloque montado).
     * @throws SQLException
     * @throws ServiceFacadeException
     * @throws PersistenceException
     */
    /**
    @Test
    public void reservaNoMontada() throws SQLException, ServiceFacadeException, PersistenceException { 
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO PROFESORES (codigo, nombre, codigoNombre, email, telefono, cedula) "
                + "VALUES (2096724, 'Cesar Vega', 'CEVE', 'cesar.vega-f@mail.escuelaing.edu.co', 3134723073, 1013622836)");
        
        stmt.execute("INSERT INTO ASIGNATURAS (mnemonico, nombre, creditos) "
                + "VALUES ('PDSW', 'Proceso de Desarrollo de Software', 4)");
        
        stmt.execute("INSERT INTO PROFESORESASIGNATURAS (profesores_codigo, asignaturas_mnemonico) "
                + "VALUES (2096724, 'PDSW')");

        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Plataformas', 30, 'Nicolas Gomez')");
        
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo)"
                + "VALUES (1, NOW(), 1, 3, 'PDSW', 'Plataformas', 2096724)");
        
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 1)");
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 2)");
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 3)");        
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 4)");
        
        Set<Asignatura> asis = new LinkedHashSet<>();
        
        Set<Software> soft = new LinkedHashSet<>();
                
        Asignatura asi = new Asignatura("PDSW", "Proceso de Desarrollo de Software", 4);               
        
        asis.add(asi);
              
        Profesor pr = new Profesor(Long.parseLong("2096724"), "Cesar Vega", "CEVE", "cesar.vega-f@mail.escuelaing.edu.co",Long.parseLong("3134723073"),Long.parseLong("1013622836"), asis);
        
        Software sof = new Software("Unity", "5", "5.6", "http://unity3d.com/es/get-unity/download");
        
        soft.add(sof);
        
        Laboratorio lb = new Laboratorio("Plataformas", 30, "Nicolas Gomez", soft);
        
        Date fc = new Date(2015, 11, 05);
        
        Set<Integer> reservas = new HashSet();
        reservas.add(4);
        reservas.add(5);
        
        Reserva rv = new Reserva(2, fc, pr, lb, 1, 3, reservas, asi);
        
        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        
        sf.insertReserva(rv);
        
        Set<Reserva> ans = sf.reservaEsperadar(1);
        
        reservas = new HashSet();
        for (int i = 1; i < 5; i++){
            reservas.add(i);
        }
        
        Iterator<Reserva> i = ans.iterator();
        while (i.hasNext()){
            Reserva r = i.next();
            Assert.assertTrue("Ingreso los datos incorectos no debe poder hacerlo", reservas.equals(r.getBloques()));
        }
        
    }  
   
    /**
     * --> verificar que la reserva  no sea de mas de 6 horas y menos de 1.5 (que estan seleccionados entre 1 y 4 checks)
     *
     * @throws java.sql.SQLException
     * @throws edu.eci.pdsw.services.ServiceFacadeException
     * @throws edu.eci.pdsw.samples.persistence.PersistenceException
     */
    /**
    @Test
    public void reservaNoMasDe6Horas() throws SQLException, ServiceFacadeException, PersistenceException{
        clearDB();
        
        Set<Asignatura> asis = new LinkedHashSet<>();
        
        Set<Software> soft = new LinkedHashSet<>();
                
        Asignatura asi = new Asignatura("PDSW", "Proceso de Desarrollo de Software", 4);               
        
        asis.add(asi);
              
        Profesor pr = new Profesor(Long.parseLong("2096724"), "Cesar Vega", "CEVE", "cesar.vega-f@mail.escuelaing.edu.co",Long.parseLong("3134723073"),Long.parseLong("1013622836"), asis);
        
        Software sof = new Software("Unity", "5", "5.6", "http://unity3d.com/es/get-unity/download");
        
        soft.add(sof);
        
        Laboratorio lb = new Laboratorio("Plataformas", 30, "Nicolas Gomez", soft);
        
        Date fc = new Date(2015, 11, 05);
        
        Set<Integer> reservas = new HashSet();
        reservas.add(1);
        reservas.add(2);
        reservas.add(3);
        reservas.add(4);
        reservas.add(5);
        
        Reserva rv = new Reserva(2, fc, pr, lb, 1, 3, reservas, asi);
        
        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        
        sf.insertReserva(rv);
        
        Set<Reserva> ans = sf.reservaEsperadar(1);
        
        Assert.assertTrue("Fallo la prueba si ingreso la reserva", ans.isEmpty());
    }

    @Test
    public void reservaHorarioValido() throws SQLException, ServiceFacadeException, PersistenceException {
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo)"
                + "VALUES (2, NOW(), 2, 4, 'PDSW', 'Redes', 2096129)");
        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Redes', 20, 'Nicolas Gomez')");
        stmt.execute("INSERT INTO PROFESORES (codigo, nombre, codigoNombre, email, telefono, cedula) "
                + "VALUES (2096129, 'Joseph Arboleda', 'JNAD', 'joseph.arboleda@mail.escuelaing.edu.co', 3118331935, 1013658663)");
          
        ServicesFacade sf = ServicesFacade.getInstance("");
        
        reservaHorarioValido(); 
    }
    @Test
    public void reservaBloqueValido()  throws SQLException, ServiceFacadeException, PersistenceException {
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo)"
                + "VALUES (3, NOW(), 4, 5, 'PDSW', 'Redes', 2096129)");
        stmt.execute("INSERT INTO BLOQUES (id, fechaRadicado, semana, dia, asigantura, laboratorio_nombre, profesores_codigo)"
                + "VALUES (4, NOW(), '2015-11-01', 'PDSW', 'Redes', 2096129)");
        
        
        ServicesFacade sf = ServicesFacade.getInstance("");
        reservaBloqueValido();
        
    }
    @Test
     public void semanaValido()  throws SQLException, ServiceFacadeException, PersistenceException {
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo)"
                + "VALUES (5, NOW(), 2, 4, 'PDSW', 'Redes', 2096129)");
        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Redes', 20, 'Nicolas Gomez')");
        stmt.execute("INSERT INTO PROFESORES (codigo, nombre, codigoNombre, email, telefono, cedula) "
                + "VALUES (2096129, 'Joseph Arboleda', 'JNAD', 'joseph.arboleda@mail.escuelaing.edu.co', 3118331935, 1013658663)");
          
        ServicesFacade sf = ServicesFacade.getInstance("");
        semanaValido();
     }
     @Test
     public void repiclarReservaValido()  throws SQLException, ServiceFacadeException, PersistenceException {
        clearDB();
        
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        //Profesor pr=new Profesor(2096129,'Joseph Arboleda','JNAD' ,'joseph.arboleda@mail.escuelaing.edu.co' ,3118331935,1013658663);
           
     }
    **/
} 

