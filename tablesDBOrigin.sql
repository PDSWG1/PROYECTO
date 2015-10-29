-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2015-10-29 20:52:40.261




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
CREATE TABLE laboratorio (
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
    asigantura varchar(5)  NULL,
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





-- foreign keys
-- Reference:  bloques_reservas (table: bloques)


ALTER TABLE bloques ADD CONSTRAINT bloques_reservas FOREIGN KEY bloques_reservas (reservas_id)
    REFERENCES reservas (id);
-- Reference:  labSoft_laboratorio (table: labSoft)


ALTER TABLE labSoft ADD CONSTRAINT labSoft_laboratorio FOREIGN KEY labSoft_laboratorio (laboratorio_nombre)
    REFERENCES laboratorio (nombre);
-- Reference:  labSoft_softwares (table: labSoft)


ALTER TABLE labSoft ADD CONSTRAINT labSoft_softwares FOREIGN KEY labSoft_softwares (softwares_nombre)
    REFERENCES softwares (nombre);
-- Reference:  profesoresAsignaturas_asignaturas (table: profesoresAsignaturas)


ALTER TABLE profesoresAsignaturas ADD CONSTRAINT profesoresAsignaturas_asignaturas FOREIGN KEY profesoresAsignaturas_asignaturas (asignaturas_mnemonico)
    REFERENCES asignaturas (mnemonico);
-- Reference:  profesoresAsignaturas_profesores (table: profesoresAsignaturas)


ALTER TABLE profesoresAsignaturas ADD CONSTRAINT profesoresAsignaturas_profesores FOREIGN KEY profesoresAsignaturas_profesores (profesores_codigo)
    REFERENCES profesores (codigo);
-- Reference:  reservas_laboratorio (table: reservas)


ALTER TABLE reservas ADD CONSTRAINT reservas_laboratorio FOREIGN KEY reservas_laboratorio (laboratorio_nombre)
    REFERENCES laboratorio (nombre);
-- Reference:  reservas_profesores (table: reservas)


ALTER TABLE reservas ADD CONSTRAINT reservas_profesores FOREIGN KEY reservas_profesores (profesores_codigo)
    REFERENCES profesores (codigo);
-- Reference:  solicitudes_laboratorio (table: solicitudes)


ALTER TABLE solicitudes ADD CONSTRAINT solicitudes_laboratorio FOREIGN KEY solicitudes_laboratorio (laboratorio_nombre)
    REFERENCES laboratorio (nombre);
-- Reference:  solicitudes_profesores (table: solicitudes)


ALTER TABLE solicitudes ADD CONSTRAINT solicitudes_profesores FOREIGN KEY solicitudes_profesores (profesores_codigo)
    REFERENCES profesores (codigo);



-- End of file.

