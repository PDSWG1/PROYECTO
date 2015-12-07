package edu.eci.cosw.sampleapp.test;

import edu.eci.pdsw.entities.*;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import edu.eci.pdsw.services.ServiceFacadeException;
import edu.eci.pdsw.services.ServicesFacade;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;                   
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
        try (Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "")) {
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
        }
    }

    //funcion de conversion rapida
    private long pLong(String s){return Long.parseLong(s);}
    
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
        
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo,numcomputadores)"
                + "VALUES (1, NOW(), 1, 3, 'PDSW', 'Plataformas', 2096724,3)");
        
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
        Assert.assertTrue("No es valia la semana realiza la consulta con una semana invalida", ans == null);
        semana = 1;
        ans = sf.reservaEsperadar(semana);
        Iterator<Reserva> i = ans.iterator();
        int a = 1;
        while(i.hasNext()){
            Reserva rv = i.next();
            Profesor pro = rv.getProfesor();
            Assert.assertTrue("Profesor no valido",pro.getCodigo() == 2096724 
                    && pro.getNombre().equals("Cesar Vega") 
                    && pro.getCodigoNombre().equals("CEVE") 
                    && pro.getEmail().equals("cesar.vega-f@mail.escuelaing.edu.co") 
                    && pro.getTelefono() == pLong("3134723073") 
                    && pro.getCedula() == pLong("1013622836"));
            Laboratorio lab = rv.getLaboratorio();
            Assert.assertTrue("Laboratorio no valido", lab.getNombreLab().equals("Plataformas")
                    && lab.getNumerocomputadores() == 30
                    && lab.getEncargado().equals("Nicolas Gomez"));
            Assert.assertTrue("ID no valido", rv.getId() == 1 || rv.getId() == 2);
            Assert.assertTrue("Bloque no valido", rv.getBloques().size() == ((rv.getId() == 1)? 4: 2));
            Assert.assertTrue("Asignatura no valido", rv.getAsignatura().getId().equals("PDSW"));
            Assert.assertTrue("Semana no valido", rv.getSemana() == 1);
            Assert.assertTrue("Dia no valido", rv.getDia() == 3);
        }
        
    }

    /**
     * --> Seleccione la informaciÃ³n de un laboratorio(verificando el numero de equipos y software disponibles)
     * @throws SQLException
     * @throws ServiceFacadeException
     * @throws PersistenceException
     */
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
        
        Assert.assertTrue("El numero de computadores no es valido", ans.getNumerocomputadores()==30);
        Assert.assertTrue("El encargado no es valido", ans.getEncargado().equals("Nicolas Gomez"));
       
        Iterator<Software> i = ans.getLabsoftware().iterator();
        int cont = 0, cont1 = 0;
        while (i.hasNext()){
            Software so = i.next();
            if (so.getNombre().equals("Unity")){
                cont++;
                Assert.assertTrue("No es la misma informacion de Software Unity", so.getLicencia().equals("5") 
                        && so.getVersion().equals("5.6") 
                        && so.getUrlDown().equals("http://unity3d.com/es/get-unity/download"));
            }else{
                cont1++;
                Assert.assertTrue("No es la misma informacion de Software Python", so.getLicencia().equals("3") 
                        && so.getVersion().equals("3.4") 
                        && so.getUrlDown().equals("http://unity3d.com/es/get-unity/download"));
            }
        }
        Assert.assertTrue("No es la misma informacion de Software", cont == 1 && cont1 == 1);
    }   
    
    /**
     * --> verificar la reserva de que sea posible realizarla(no en bloque montado).
     * @throws SQLException
     * @throws ServiceFacadeException
     * @throws PersistenceException
     */
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
        
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo, numcomputadores )"
                + "VALUES (1, NOW(), 1, 3, 'PDSW', 'Plataformas', 2096724, 30)");
        
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 1)");
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 2)");
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 3)");        
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (1, 4)");
        
        Set<Asignatura> asis = new LinkedHashSet<>();
                
        Asignatura asi = new Asignatura("PDSW", "Proceso de Desarrollo de Software", 4);               
        
        asis.add(asi);
              
        Profesor pr = new Profesor(Long.parseLong("2096724"), "Cesar Vega", "CEVE", "cesar.vega-f@mail.escuelaing.edu.co",Long.parseLong("3134723073"),Long.parseLong("1013622836"), asis);
        
        Set<Software> soft = new LinkedHashSet<>();
        
        Software sof = new Software("Unity", "5", "5.6", "http://unity3d.com/es/get-unity/download");
        
        soft.add(sof);
        
        Laboratorio lb = new Laboratorio("Plataformas", 30, "Nicolas Gomez", soft);
        
        Date fc = new Date(2015, 11, 05);
        int numcomputadores=1;
        Set<Integer> reservas = new LinkedHashSet();
        reservas.add(4);
        reservas.add(5);
        
        Reserva rv = new Reserva(2, fc, pr, lb, 1, 3, reservas, asi, numcomputadores);
        
        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        
        sf.insertReserva(rv);
        
        Set<Reserva> ans = sf.reservaEsperadar(1);
        
        reservas = new LinkedHashSet();
        for (int i = 1; i < 5; i++){
            reservas.add(i);
        }
        
        Iterator<Reserva> i = ans.iterator();
        while (i.hasNext()){
            Reserva r = i.next();
            Assert.assertTrue("Ingreso los datos incorectos no debe poder hacerlo", reservas.containsAll(r.getBloques()));
        }
        
    }  
   
    /**
     * --> verificar que la reserva  no sea de mas de 6 horas y menos de 1.5 (que estan seleccionados entre 1 y 4 checks)
     *
     * @throws java.sql.SQLException
     * @throws edu.eci.pdsw.services.ServiceFacadeException
     * @throws edu.eci.pdsw.samples.persistence.PersistenceException
     */
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
        
        Set<Integer> reservas = new LinkedHashSet();
        reservas.add(1);
        reservas.add(2);
        reservas.add(3);
        reservas.add(4);
        reservas.add(5);
        int numcomputadores=1;
        Reserva rv = new Reserva(2, fc, pr, lb, 1, 3, reservas, asi,numcomputadores);
        
        
        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        
        sf.insertReserva(rv);
        
        Set<Reserva> ans = sf.reservaEsperadar(1);
        
        Assert.assertTrue("Fallo la prueba si ingreso la reserva", ans.isEmpty());
    }    

    /**
     * --> verificar que el horario  de la reserva este dentro del margen del horario institucional (7am-7pm)
     *
     * @throws java.sql.SQLException
     * @throws edu.eci.pdsw.services.ServiceFacadeException
     * @throws edu.eci.pdsw.samples.persistence.PersistenceException
     **/
    @Test
    public void reservaHorarioValido() throws SQLException, ServiceFacadeException, PersistenceException {
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo,numcomputadores)"
                + "VALUES (9, NOW(), 2, 4, 'PDSW', 'Redes', 2096129,1)");
        
        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Redes', 20, 'Nicolas Gomez')");
        
        stmt.execute("INSERT INTO PROFESORES (codigo, nombre, codigoNombre, email, telefono, cedula) "
                + "VALUES (2096129, 'Joseph Arboleda', 'JNAD', 'joseph.arboleda@mail.escuelaing.edu.co', 3118331935, 1013658663)");
        
        Set<Asignatura> asig = new LinkedHashSet<>();
        
        Set<Software> soft = new LinkedHashSet<>();
                
        Asignatura asign = new Asignatura("PDSW", "Proceso de Desarrollo de Software", 4);               
        
        asig.add(asign);
        
        Profesor pr = new Profesor(Long.parseLong("2096129"), "Joseph Arboleda", "JNAD", "joseph.arboleda@mail.escuelaing.edu.co",Long.parseLong("3118331935"),Long.parseLong("1013658663"), asig);
        
        Software sof = new Software("Blender", "6", "2.76", "https://www.blender.org/download/");
        
        soft.add(sof);
        
        Laboratorio lb = new Laboratorio("Redes", 20, "Nicolas Gomez", soft);
        
        Date fc = new Date(2015, 11, 05);
        
        Set<Integer> reservas = new HashSet(0);
        int numcomputadores=1;                
        Reserva rv = new Reserva(10, fc, pr, lb, 1, 4, reservas, asign,numcomputadores);

        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        
        sf.insertReserva(rv);
        
        Set<Reserva> ans=sf.reservaEsperadar(1);
        boolean bol=true;
        
        for(Reserva r:ans) {
            if (r.getId()==rv.getId()){
                bol =false;
            }
        }   
        Assert.assertTrue("Fallo la prueba la reserva no tiene un bloque valido",bol);

    }
   
    /**
     * --> Replicar la reserva varias semanas validando  que falle al menos uno de los 3 metodos para insertar (bloque valido,semana valida,reservabloque valido) 
     *
     * @throws java.sql.SQLException
     * @throws edu.eci.pdsw.services.ServiceFacadeException
     * @throws edu.eci.pdsw.samples.persistence.PersistenceException
    **/
    @Test
    public void repiclarReservaNoValido()  throws SQLException, ServiceFacadeException, PersistenceException {
        clearDB();
        
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo, numcomputadores)"
                + "VALUES (20, NOW(), 2, 4, 'PIMO', 'Ingenieria de software', 2096139, 24)");
        
        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Ingenieria de software', 24, 'Nicolas Gomez')");
        
        stmt.execute("INSERT INTO ASIGNATURAS (mnemonico, nombre, creditos) "
                + "VALUES ('PIMO', 'Programacion imperativa modular', 4)");
        
        stmt.execute("INSERT INTO PROFESORES (codigo, nombre, codigoNombre, email, telefono, cedula) "
                + "VALUES (2096139, 'Camilo Rocha', 'CARO', 'camilo.rocha@escuelaing.edu.co', 3134723033, 1013628836)");
        
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (20, 2)");
        
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (20, 3)");
        
        
        Set<Asignatura> asis = new LinkedHashSet<>();
        
        Set<Software> soft = new LinkedHashSet<>();
                
        Asignatura asi = new Asignatura("PIMO", "Programacion imperativa modular", 4);               
        
        asis.add(asi);
              
        Profesor pr = new Profesor(Long.parseLong("2096139"), "Camilo Rocha", "CARO", "camilo.rocha@escuelaing.edu.co",Long.parseLong("3134723033"),Long.parseLong("1013628836"), asis);
        
        Software sof = new Software("Python", "8", "3.5.0", "https://www.python.org/downloads/");
        
        soft.add(sof);
        
        Laboratorio lb = new Laboratorio("Ingenieria de software", 24, "Nicolas Gomez", soft);
        
        Date fc = new Date(2015, 11, 05);
        
        Set<Integer> reservas = new LinkedHashSet();
        reservas.add(1);
        reservas.add(2);
        int numcomputadores = 4;
        Reserva rv = new Reserva(21, fc, pr, lb, 1, 4, reservas, asi, numcomputadores);
        
        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");

        sf.insertReservaReplay(rv);
        
        Set<Reserva> ans =sf.reservaEsperadar(2);
        for (Reserva r :ans){
            Assert.assertTrue("No se pudo replicar la reserva",r.getId()==20);
        }
           
    }
     
    /**
     * --> Replicar la reserva varias semanas validando los 3 metodos para insertar (bloque valido,semana valida,reservabloque valido) 
     *
     * @throws java.sql.SQLException
     * @throws edu.eci.pdsw.services.ServiceFacadeException
     * @throws edu.eci.pdsw.samples.persistence.PersistenceException
    **/
    @Test
    public void repiclarReservaValido()  throws SQLException, ServiceFacadeException, PersistenceException {
        clearDB();
        
        Set<Asignatura> asis = new LinkedHashSet<>();
        
        Set<Software> soft = new LinkedHashSet<>();
                
        Asignatura asi = new Asignatura("PIMO", "Programacion imperativa modular", 4);               
        
        asis.add(asi);
              
        Profesor pr = new Profesor(Long.parseLong("2096139"), "Camilo Rocha", "CARO", "camilo.rocha@escuelaing.edu.co",Long.parseLong("3134723033"),Long.parseLong("1013628836"), asis);
        
        Software sof = new Software("Python", "8", "3.5.0", "https://www.python.org/downloads/");
        
        soft.add(sof);
        
        Laboratorio lb = new Laboratorio("Ingenieria de software", 24, "Nicolas Gomez", soft);
        
        Date fc = new Date(2015, 11, 05);
        
        Set<Integer> reservas = new LinkedHashSet();
        reservas.add(1);
        reservas.add(2);
        int numcomputadores=1;
        Reserva rv = new Reserva(21, fc, pr, lb, 1, 3, reservas, asi,numcomputadores);
        
        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        
        sf.insertReservaReplay(rv);
        
        for (int i=rv.getSemana(), j=21;i<17;i++){
            Set<Reserva> ans =sf.reservaEsperadar(i);
            for (Reserva r :ans){
                Assert.assertTrue("No se pudo replicar la reserva",r.getId() == (j++));
            }
            
        }
        
    }
    
    @Test
    public void mostrarMatriz() throws SQLException, PersistenceException {
        clearDB();

        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo,numcomputadores)"
                + "VALUES (20, NOW(), 2, 4, 'PIMO', 'Ingenieria de Software', 2096139,1)");

        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Ingenieria de Software', 20, 'Nicolas Gomez')");

        stmt.execute("INSERT INTO ASIGNATURAS (mnemonico, nombre, creditos) "
                + "VALUES ('PIMO', 'Programacion imperativa modular', 4)");

        stmt.execute("INSERT INTO PROFESORES (codigo, nombre, codigoNombre, email, telefono, cedula) "
                + "VALUES (2096139, 'Camilo Rocha', 'CARO', 'camilo.rocha@escuelaing.edu.co', 3134723033, 1013628836)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (20, 2)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (20, 3)");

        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo,numcomputadores)"
                + "VALUES (21, NOW(), 2, 4, 'PIMO', 'Plataformas Computacionales', 2096139,1)");

        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Plataformas Computacionales', 20, 'Nicolas Gomez')");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (21, 2)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (21, 3)");

        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo,numcomputadores)"
                + "VALUES (22, NOW(), 2, 4, 'PIMO', 'Multimedia y Moviles', 2096139,1)");

        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Multimedia y Moviles', 20, 'Nicolas Gomez')");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (22, 2)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (22, 3)");

        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");

        ArrayList<ArrayList<ArrayList<booString>>> ans = sf.mostrarInformacionTabla(2);
        
        Assert.assertTrue("No coinciden los datos1",ans.get(1).get(5).get(1).toString().equals("PIMO CARO; #PC 19"));
        Assert.assertTrue("No coinciden los datos2",ans.get(1).get(5).get(2).toString().equals("PIMO CARO; #PC 19"));
        Assert.assertTrue("No coinciden los datos3",ans.get(1).get(5).get(3).toString().equals("PIMO CARO; #PC 19"));
        Assert.assertTrue("No coinciden los datos4",ans.get(2).get(5).get(1).toString().equals("PIMO CARO; #PC 19"));
        Assert.assertTrue("No coinciden los datos5",ans.get(2).get(5).get(2).toString().equals("PIMO CARO; #PC 19"));
        Assert.assertTrue("No coinciden los datos6",ans.get(2).get(5).get(3).toString().equals("PIMO CARO; #PC 19")); 
        
    }

    /**
     * --> Reserva de laboratorios con equipos disponibles
     *
     * @throws java.sql.SQLException
     * @throws edu.eci.pdsw.samples.persistence.PersistenceException
    **/
    @Test
    public void reservaLabDisponible() throws SQLException, PersistenceException{
        clearDB();
        Set<Asignatura> asis = new LinkedHashSet<>();
        int numcomputadores=70;
        boolean compu;
        Set<Software> soft = new LinkedHashSet<>();

        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        
        
        stmt.execute("INSERT INTO PROFESORES (codigo, nombre, codigoNombre, email, telefono, cedula) "
                + "VALUES (2096139, 'Camilo Rocha', 'CARO', 'camilo.rocha@escuelaing.edu.co', 3134723033, 1013628836)");
        stmt.execute("INSERT INTO ASIGNATURAS (mnemonico, nombre, creditos) "
                + "VALUES ('PIMO', 'Programacion imperativa modular', 4)");
        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Ingenieria de Software', 20, 'Maria Blanco')");
        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Redes', 10, 'Nicolas Gomez')");
        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('sala computacional', 5, 'Hugo Sanchez')");
        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('B0', 30, 'Aurora Elvira')");
        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero)"
                + "VALUES (20, 1)");
        stmt.execute("INSERT INTO LABSOFT (laboratorio_nombre, softwares_nombre) "
                + "VALUES ('Ingenieria de Software', 'Python')");
        
        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo,numcomputadores)"
                + "VALUES (20, NOW(), 2, 4, 'PIMO', 'Ingenieria de Software', 2096139,15)");
        
        
                
        Asignatura asi = new Asignatura("PIMO", "Programacion imperativa modular", 4);               
        
        asis.add(asi);
              
        Profesor pr = new Profesor(Long.parseLong("2096139"), "Camilo Rocha", "CARO", "camilo.rocha@escuelaing.edu.co",Long.parseLong("3134723033"),Long.parseLong("1013628836"), asis);
        
        Software sof = new Software("Python", "8", "3.5.0", "https://www.python.org/downloads/");
        
        soft.add(sof);
        
        Laboratorio lb = new Laboratorio("Ingenieria de Software", 24, "Maria Blanco", soft);
        
        Date fc = new Date(2015, 11, 05);
        
        Set<Integer> reservas = new LinkedHashSet();
        reservas.add(1);
        


        Reserva rv = new Reserva(20, fc, pr, lb, 2, 4, reservas, asi,numcomputadores);
        
        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        
        
        compu= sf.reservaLabDisponible(rv.getBloques(),rv.getLaboratorio(),rv.getSemana(),rv.getDia(),rv.getNumcomputadores());
        Assert.assertTrue("El numero de computadoras excede el limite",compu==false);
    }
    
    @Test
    public void informacionProfesor() throws SQLException, PersistenceException{
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();

        stmt.execute("INSERT INTO PROFESORES (codigo, nombre, codigoNombre, email, telefono, cedula) "
                + "VALUES (2096139, 'Camilo Rocha', 'CARO', 'camilo.rocha@escuelaing.edu.co', 3134723033, 1013628836)");

        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");

        Profesor pr = sf.getProfesor(2096139);

        Assert.assertTrue("No coinciden los datos",pr.getCodigo() == 2096139);
        Assert.assertTrue("No coinciden los datos",pr.getNombre().equals("Camilo Rocha"));
        Assert.assertTrue("No coinciden los datos",pr.getCodigoNombre().equals("CARO"));
        Assert.assertTrue("No coinciden los datos",pr.getEmail().equals("camilo.rocha@escuelaing.edu.co"));
        Assert.assertTrue("No coinciden los datos",pr.getTelefono() == Long.parseLong("3134723033"));
        Assert.assertTrue("No coinciden los datos",pr.getCedula() == Long.parseLong("1013628836")); 
           
    }
    
    @Test
    public void filtroSoftwareNumComputadores() throws SQLException, PersistenceException{
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();

        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo, numcomputadores) "
            + "VALUES (22, NOW(), 2, 4, 'PIMO', 'Multimedia y Moviles', 2096139, 1)");
        
        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Multimedia y Moviles', 7, 'Nicolas Gomez')");

        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Unity', '5', '5.6', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Python', '3', '3.4', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('NetBeans', '5', '5.6', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Eclipse', '3', '3.4', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO LABSOFT (laboratorio_nombre, softwares_nombre) "
                + "VALUES ('Multimedia y Moviles', 'Unity')");
        
        stmt.execute("INSERT INTO LABSOFT (laboratorio_nombre, softwares_nombre) "
                + "VALUES ('Multimedia y Moviles', 'Python')");

        stmt.execute("INSERT INTO ASIGNATURAS (mnemonico, nombre, creditos) "
                + "VALUES ('PIMO', 'Programacion imperativa modular', 4)");

        stmt.execute("INSERT INTO PROFESORES (codigo, nombre, codigoNombre, email, telefono, cedula) "
                + "VALUES (2096139, 'Camilo Rocha', 'CARO', 'camilo.rocha@escuelaing.edu.co', 3134723033, 1013628836)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (22, 2)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (22, 3)");

        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo,numcomputadores) "
            + "VALUES (23, NOW(), 2, 1, 'PIMO', 'Ingenieria de software', 2096139, 1)");
        
        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Ingenieria de software', 20, 'Nicolas Gomez')");
        
        stmt.execute("INSERT INTO LABSOFT (laboratorio_nombre, softwares_nombre) "
                + "VALUES ('Ingenieria de software', 'Unity')");
        
        stmt.execute("INSERT INTO LABSOFT (laboratorio_nombre, softwares_nombre) "
                + "VALUES ('Ingenieria de software', 'Python')");
        
        stmt.execute("INSERT INTO LABSOFT (laboratorio_nombre, softwares_nombre) "
                + "VALUES ('Ingenieria de software', 'NetBeans')");
        
        stmt.execute("INSERT INTO LABSOFT (laboratorio_nombre, softwares_nombre) "
                + "VALUES ('Ingenieria de software', 'Eclipse')");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (23, 1)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (23, 2)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (23, 3)");

        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo,numcomputadores) "
            + "VALUES (24, NOW(), 2, 2, 'PIMO', 'Multimedia y Moviles', 2096139, 3)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (24, 2)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (24, 3)");

        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo,numcomputadores) "
            + "VALUES (25, NOW(), 2, 3, 'PIMO', 'Ingenieria de software', 2096139, 7)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (25, 2)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (25, 3)");        

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (25, 4)");

        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        //retornar laboratorios y horas

        int numComputadores = 1;
        Set<String> softs = new HashSet<>();
        softs.add("Unity");
        softs.add("Python");
        softs.add("NetBeans");
        int semana = 2;

        sf.mostrarInformacionTabla(semana);
        ArrayList<ArrayList<ArrayList<booString>>> a = sf.getDispFiltroSoftwareNumCompu(numComputadores, softs, semana);
        
        boolean boo = true;
        for (int k = 0; k < 8; k++){
            for(int i = 2; i < 8; i++){
                for (int j = 0; j < a.get(k).get(i).size(); j++){
                    if( j == 0 && !a.get(k).get(i).get(j).isDisponible()){
                        boo = false;
                    }
                }    
            }
        } 
        Assert.assertTrue("No coinciden los valores", boo);
    }
    
    @Test
    public void getAllSoftware() throws SQLException{
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Unity', '5', '5.6', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Python', '3', '3.4', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('NetBeans', '5', '5.6', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Eclipse', '3', '3.4', 'http://unity3d.com/es/get-unity/download')");
        
        Set<String> sof = new HashSet<>();
        sof.add("Unity");
        sof.add("Python");
        sof.add("NetBeans");
        sof.add("Eclipse");

        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        
        Set<Software> softs = sf.getAllSoftware();
        int ban = 0;
        for (Software s: softs){
            if(sof.contains(s.getNombre())){
                ban++;
            }
        }
        
        Assert.assertTrue("No coinciden los valores", ban == 4);
    } 
    
    @Test
    public void getSoftware() throws SQLException{
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Unity', '5', '5.6', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Python', '3', '3.4', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('NetBeans', '5', '5.6', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Eclipse', '3', '3.4', 'http://unity3d.com/es/get-unity/download')");

        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        
        Software soft = sf.getSoftware("Eclipse");
        
        Assert.assertTrue("No coinciden los valores", soft.getNombre().equals("Eclipse"));
        
        soft = sf.getSoftware("Unity");
        
        Assert.assertTrue("No coinciden los valores", soft.getNombre().equals("Unity"));
        
        soft = sf.getSoftware("Python");
        
        Assert.assertTrue("No coinciden los valores", soft.getNombre().equals("Python"));
        
        soft = sf.getSoftware("NetBeans");
        
        Assert.assertTrue("No coinciden los valores", soft.getNombre().equals("NetBeans"));
    } 
    
    @Test
    public void mostrarReservasSemanaYLaboratorio() throws SQLException, PersistenceException{
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();

        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo, numcomputadores) "
            + "VALUES (22, NOW(), 2, 4, 'PIMO', 'Multimedia y Moviles', 2096139, 1)");
        
        stmt.execute("INSERT INTO LABORATORIOS (nombre, numComputadores, encargado) "
                + "VALUES ('Multimedia y Moviles', 7, 'Nicolas Gomez')");

        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Unity', '5', '5.6', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Python', '3', '3.4', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('NetBeans', '5', '5.6', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO SOFTWARES (nombre, licencia, version, urlDown) "
                + "VALUES ('Eclipse', '3', '3.4', 'http://unity3d.com/es/get-unity/download')");
        
        stmt.execute("INSERT INTO LABSOFT (laboratorio_nombre, softwares_nombre) "
                + "VALUES ('Multimedia y Moviles', 'Unity')");
        
        stmt.execute("INSERT INTO LABSOFT (laboratorio_nombre, softwares_nombre) "
                + "VALUES ('Multimedia y Moviles', 'Python')");

        stmt.execute("INSERT INTO ASIGNATURAS (mnemonico, nombre, creditos) "
                + "VALUES ('PIMO', 'Programacion imperativa modular', 4)");

        stmt.execute("INSERT INTO PROFESORES (codigo, nombre, codigoNombre, email, telefono, cedula) "
                + "VALUES (2096139, 'Camilo Rocha', 'CARO', 'camilo.rocha@escuelaing.edu.co', 3134723033, 1013628836)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (22, 2)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (22, 3)");

        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo,numcomputadores) "
            + "VALUES (23, NOW(), 2, 1, 'PIMO', 'Multimedia y Moviles', 2096139, 1)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (23, 1)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (23, 2)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (23, 3)");

        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo,numcomputadores) "
            + "VALUES (24, NOW(), 2, 1, 'PIMO', 'Multimedia y Moviles', 2096139, 3)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (24, 2)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (24, 3)");

        stmt.execute("INSERT INTO RESERVAS (id, fechaRadicado, semana, dia, asignatura, laboratorio_nombre, profesores_codigo,numcomputadores) "
            + "VALUES (25, NOW(), 2, 3, 'PIMO', 'Multimedia y Moviles', 2096139, 7)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (25, 2)");

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (25, 3)");        

        stmt.execute("INSERT INTO BLOQUES (reservas_id, numero) "
            + "VALUES (25, 4)");

        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        //retornar laboratorios y horas
        
        String[][] ans1 = new String[][]{
            {"7:00am-8:30am","Disponible", "Disponible", "Disponible", "Disponible", "Disponible", "Disponible"},
            {"8:30am-10:00am","PIMO CARO; " , "Disponible", "Disponible", "Disponible", "Disponible", "Disponible"},
            {"10:00am-11:30pm","PIMO CARO; PIMO CARO; " , "Disponible", "PIMO CARO; " , "PIMO CARO; " , "Disponible", "Disponible"},
            {"11:30pm-1:00pm","PIMO CARO; PIMO CARO; " , "Disponible", "PIMO CARO; " , "PIMO CARO; " , "Disponible", "Disponible"},
            {"1:00pm-2:30pm","Disponible", "Disponible", "PIMO CARO; " , "Disponible", "Disponible", "Disponible"},
            {"2:30pm-4:00pm","Disponible", "Disponible", "Disponible", "Disponible", "Disponible", "Disponible"},
            {"4:00pm-5:30pm","Disponible", "Disponible", "Disponible", "Disponible", "Disponible", "Disponible"},
            {"5:30pm-7:00pm","Disponible", "Disponible", "Disponible", "Disponible", "Disponible", "Disponible"}};

        String[][] ans = sf.getReservasSemanaYLaboratorio(2, "Multimedia y Moviles");
        boolean boo = true;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 6; j++){
                boo &= ans[i][j].equals(ans1[i][j]);
            }
        }
        Assert.assertTrue("No coinciden los valores", boo);
    }
    
    @Test
    public void UsuarioPassValido() throws SQLException{
        clearDB();
        Connection conn = DriverManager.getConnection("jdbc:h2:file:./target/db/testdb;MODE=MYSQL", "sa", "");
        Statement stmt = conn.createStatement();
        
        stmt.execute("INSERT INTO user(user_id, name, password) "+
                "VALUES ('2096121', 'Hector Fabio Cadavid Rodriguez' , '2096121')");
        stmt.execute("INSERT INTO user(user_id, name, password) "+
                "VALUES ('2096122', 'Camilo Andres Rocha Diaz' , '2096122')");
        stmt.execute("INSERT INTO user(user_id, name, password) "+
                "VALUES ('2096123', 'Maria Clara Irma Fernandez' , '2096123')");
        stmt.execute("INSERT INTO user(user_id, name, password) "+
                "VALUES ('2096124', 'Ruben Cesar Dario Vladez' , '2096124')");
        stmt.execute("INSERT INTO user(user_id, name, password) "+
                "VALUES ('2096125', 'Olga Lucia Godoy Morales' , '2096125')");	
        stmt.execute("INSERT INTO user(user_id, name, password) "+
                "VALUES ('1013622878', 'admin' , '1013622878')");	
        stmt.execute("INSERT INTO user(user_id, name, password) "+
                "VALUES ('91043054002', 'guest' , '91043054002')");	

        stmt.execute("INSERT INTO role(role_id) "+
                "VALUES ('admin')");	
        stmt.execute("INSERT INTO role(role_id) "+
                "VALUES ('employee')");
        stmt.execute("INSERT INTO role(role_id) "+
                "VALUES ('guest')");	

        stmt.execute("INSERT INTO userroles(userid, roleid) "+
                "VALUES ('2096121','employee')");
        stmt.execute("INSERT INTO userroles(userid, roleid) "+
                "VALUES ('2096122','employee')");	
        stmt.execute("INSERT INTO userroles(userid, roleid) "+
                "VALUES ('2096123','employee')");	
        stmt.execute("INSERT INTO userroles(userid, roleid) "+
                "VALUES ('2096124','employee')");	
        stmt.execute("INSERT INTO userroles(userid, roleid) "+
                "VALUES ('2096125','employee')");	
        stmt.execute("INSERT INTO userroles(userid, roleid) "+
                "VALUES ('1013622878','admin')");	
        stmt.execute("INSERT INTO userroles(userid, roleid) "+
                "VALUES ('91043054002','guest')");	
        
        
        ServicesFacade sf = ServicesFacade.getInstance("h2-applicationconfig.properties");
        
        String password;
        
        password = sf.getPassword("2096121");
        Assert.assertTrue("No coinciden los valores", password.equals("2096121"));
        
        password = sf.getPassword("2096122");
        Assert.assertTrue("No coinciden los valores", password.equals("2096122"));
        
        password = sf.getPassword("2096123");
        Assert.assertTrue("No coinciden los valores", password.equals("2096123"));
        
        password = sf.getPassword("2096124");
        Assert.assertTrue("No coinciden los valores", password.equals("2096124"));
        
        password = sf.getPassword("2096125");
        Assert.assertTrue("No coinciden los valores", password.equals("2096125"));
        
        password = sf.getPassword("1013622878");
        Assert.assertTrue("No coinciden los valores", password.equals("1013622878"));
        
        password = sf.getPassword("91043054002");
        Assert.assertTrue("No coinciden los valores", password.equals("91043054002"));
        
        password = sf.getPassword("123");
        Assert.assertTrue("No coinciden los valores", password == null);
        
    }
} 