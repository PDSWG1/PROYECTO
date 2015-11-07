-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2015-10-29 20:52:40.261

-- tables
-- Table ASIGNATURAS
CREATE TABLE ASIGNATURAS(
    mnemonico varchar(5)  NOT NULL,
    nombre varchar(200)  NOT NULL,
    creditos int  NOT NULL,
    CHECK (creditos>0),
    CONSTRAINT asignaturas_pk PRIMARY KEY (mnemonico)
);

-- Table BLOQUES
CREATE TABLE BLOQUES(
    reservas_id int  NOT NULL,
    numero int  NOT NULL,
    CONSTRAINT bloques_pk PRIMARY KEY (reservas_id,numero)
);

-- Table LABSOFT
CREATE TABLE LABSOFT(
    laboratorio_nombre varchar(200)  NOT NULL,
    softwares_nombre varchar(200)  NOT NULL,
    CONSTRAINT labSoft_pk PRIMARY KEY (laboratorio_nombre,softwares_nombre)
);

-- Table LABORATORIOS
CREATE TABLE LABORATORIOS(
    nombre varchar(200)  NOT NULL,
    numComputadores int  NOT NULL,
    encargado varchar(200)  NOT NULL,
    CONSTRAINT laboratorio_pk PRIMARY KEY (nombre)
);

-- Table PROFESORES
CREATE TABLE PROFESORES(
    codigo int  NOT NULL,
    nombre varchar(200)  NOT NULL,
    codigoNombre varchar(5)  NOT NULL,
    email varchar(200)  NOT NULL,
    telefono int  NULL,
    cedula int  NOT NULL,
    CONSTRAINT profesores_pk PRIMARY KEY (codigo)
);

-- Table PROFESORESASIGNATURAS
CREATE TABLE PROFESORESASIGNATURAS(
    profesores_codigo int  NOT NULL,
    asignaturas_mnemonico varchar(5)  NOT NULL,
    CONSTRAINT profesoresAsignaturas_pk PRIMARY KEY (profesores_codigo,asignaturas_mnemonico)
);

-- Table RESERVAS
CREATE TABLE RESERVAS(
    id int  NOT NULL,
    fechaRadicado date  NOT NULL,
    dia int NOT NULL,
    semana int NOT NULL,
    asignatura varchar(5) NOT NULL,
    laboratorio_nombre varchar(200)  NOT NULL,
    profesores_codigo int  NOT NULL,
    CHECK (0 < dia and dia < 7),
    CHECK (0 < semana and semana < 17),
    CONSTRAINT reservas_pk PRIMARY KEY (id)
);

-- Table SOFTWARES
CREATE TABLE SOFTWARES(
    nombre varchar(200)  NOT NULL,
    licencia varchar(500)  NOT NULL,
    version varchar(10)  NOT NULL,
    urlDown varchar(500)  NOT NULL,
    CONSTRAINT softwares_pk PRIMARY KEY (nombre)
);

-- Table SOLICITUDES
CREATE TABLE SOLICITUDES(
    id int  NOT NULL,
    fecha date  NOT NULL,
    estado bool  NOT NULL,
    profesor int  NOT NULL,
    comentario varchar(500)  NULL,
    profesores_codigo int  NOT NULL,
    laboratorio_nombre varchar(200)  NOT NULL,
    CONSTRAINT solicitudes_pk PRIMARY KEY (id)
);

