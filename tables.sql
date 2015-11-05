-- tables
-- Table asignaturas
CREATE TABLE asignaturas (
    mnemonico varchar(5)  NOT NULL,
    nombre varchar(200)  NOT NULL,
    creditos int  NOT NULL,
    CHECK (creditos>0),
    CONSTRAINT asignaturas_pk PRIMARY KEY (mnemonico)
);

-- Table bloques
CREATE TABLE bloques (
    reservas_id int  NOT NULL,
    numero int  NOT NULL,
    CONSTRAINT bloques_pk PRIMARY KEY (reservas_id,numero)
);

-- Table labSoft
CREATE TABLE labSoft (
    laboratorio_nombre varchar(200)  NOT NULL,
    softwares_nombre varchar(200)  NOT NULL,
    CONSTRAINT labSoft_pk PRIMARY KEY (laboratorio_nombre,softwares_nombre)
);

-- Table laboratorio
CREATE TABLE laboratorios (
    nombre varchar(200)  NOT NULL,
    numComputadores int  NOT NULL,
    encargado varchar(200)  NOT NULL,
    CONSTRAINT laboratorio_pk PRIMARY KEY (nombre)
);

-- Table profesores
CREATE TABLE profesores (
    codigo int  NOT NULL,
    nombre varchar(200)  NOT NULL,
    codigoNombre varchar(5)  NOT NULL,
    email varchar(200)  NOT NULL,
    telefono int  NULL,
    cedula int  NOT NULL,
    CONSTRAINT profesores_pk PRIMARY KEY (codigo)
);

-- Table profesoresAsignaturas
CREATE TABLE profesoresAsignaturas (
    profesores_codigo int  NOT NULL,
    asignaturas_mnemonico varchar(5)  NOT NULL,
    CONSTRAINT profesoresAsignaturas_pk PRIMARY KEY (profesores_codigo,asignaturas_mnemonico)
);

-- Table reservas
CREATE TABLE reservas (
    id int  NOT NULL,
    fechaRadicado date  NOT NULL,
    fechaReserva date  NOT NULL,
    semana int NOT NULL,
    asigantura varchar(5) NOT NULL,
    laboratorio_nombre varchar(200)  NOT NULL,
    profesores_codigo int  NOT NULL,
    CHECK (fechaRadicado > fechaReserva),
    CONSTRAINT reservas_pk PRIMARY KEY (id)
);

-- Table softwares
CREATE TABLE softwares (
    nombre varchar(200)  NOT NULL,
    licencia varchar(500)  NOT NULL,
    version varchar(10)  NOT NULL,
    urlDown varchar(500)  NOT NULL,
    CONSTRAINT softwares_pk PRIMARY KEY (nombre)
);

-- Table solicitudes
CREATE TABLE solicitudes (
    id int  NOT NULL,
    fecha date  NOT NULL,
    estado bool  NOT NULL,
    profesor int  NOT NULL,
    comentario varchar(500)  NULL,
    profesores_codigo int  NOT NULL,
    laboratorio_nombre varchar(200)  NOT NULL,
    CONSTRAINT solicitudes_pk PRIMARY KEY (id)
);
