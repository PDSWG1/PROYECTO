package edu.eci.cosw.sampleapp.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import static org.junit.Assert.fail;
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
        stmt.execute("delete from COMENTARIOS");
        stmt.execute("delete from SUSCRIPTORES");
        conn.commit();
        conn.close();
    }

    @Test
    public void sampleTest() {
          //fail("No hay pruebas implementadas");	
    }


    @Test
    public void diponibilidadHorario() throws SQLException {
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO PROFESORES (codigo, nombre, codigoNombre, email, telefono, cedula) "
                + "VALUES (2096724, 'Cesar Vega', 'CEVE', 'cesar.vega-f@mail.escuelaing.edu.co', 3134723073, 1013622836)");
        
        stmt.execute("INSERT INTO ASIGNATURAS (mnemonico, nombre, creditos) "
                + "VALUES ('PDSW', 'Proceso de Desarrollo de Software', 4)");
        
        stmt.execute("INSERT INTO profesoresAsignaturas (profesores_codigo, asignaturas_mnemonico) "
                + "VALUES (2096724, 'PDSW')");

        stmt.execute("INSERT INTO LOBARATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Plataformas', 30, 'Nicolas Gomez')");
        
        stmt.execute("INSERT INTO RESERVAS (id, comentario, puntaje, fecha, CLIENTES_id)"
                + "VALUES (2, 'esta Malo otra vez', 23, NOW(), 2096724)");
        
        
    }

    /**
      @Test
    public void sampleTest() {
          //fail("No hay pruebas implementadas");	
    }
    **/
} 

