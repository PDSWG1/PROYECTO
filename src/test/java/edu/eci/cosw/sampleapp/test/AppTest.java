package edu.eci.cosw.sampleapp.test;

import edu.eci.pdsw.entities.Profesor;
import edu.eci.pdsw.entities.Reserva;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import edu.eci.pdsw.services.ServiceFacadeException;
import edu.eci.pdsw.services.ServicesFacade;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import org.junit.After;
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

    @Test
    public void sampleTest() {
          //fail("No hay pruebas implementadas");	
    }


    @Test
    public void diponibilidadHorario() throws SQLException, ServiceFacadeException, PersistenceException {
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
                
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, dia, asignatura, laboratorio_nombre, profesores_codigo)"
                + "VALUES (2, NOW(), '2015-11-22', 'PDSW', 'Plataformas', 2096724)");
        
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 1)");
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 2)");
        
        ServicesFacade sf = ServicesFacade.getInstance("");
        int semana = 1;
        Set<Reserva> ans = sf.reservaEspe(semana);
        
    }

    /**
     * --> verificar que la reserva  no sea de mas de 6 horas y menos de 1.5 (que estan seleccionados entre 1 y 4 checks)
     *
     * @throws java.sql.SQLException
     * @throws edu.eci.pdsw.services.ServiceFacadeException
     * @throws edu.eci.pdsw.samples.persistence.PersistenceException
     */
    @Test
    public void reservaNoMasDe6Horas() throws SQLException, ServiceFacadeException, PersistenceException
    {
    clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
    
    
    
    
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
    
} 


/**
      @Test
    public void sampleTest() {
          //fail("No hay pruebas implementadas");	
    }
    **/
