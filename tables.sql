-- tables
-- Table ASIGNATURAS
CREATE TABLE IF NOT EXISTS `ASIGNATURAS`(
	`mnemonico` VARCHAR(5) NOT NULL,
	`nombre` VARCHAR(200) NOT NULL,
	`creditos` INT NOT NULL,
	PRIMARY KEY (`mnemonico`))
ENGINE = InnoDB;

-- Table BLOQUES
CREATE TABLE IF NOT EXISTS `BLOQUES`(
	`reservas_id` BIGINT NOT NULL,
	`numero` INT NOT NULL,
	PRIMARY KEY (`reservas_id`, `numero`))
ENGINE = InnoDB;

-- Table LABSOFT
CREATE TABLE IF NOT EXISTS `LABSOFT`(
	`laboratorio_nombre` VARCHAR(200)  NOT NULL,
	`softwares_nombre` VARCHAR(200)  NOT NULL,
	PRIMARY KEY (`laboratorio_nombre`,`softwares_nombre`))
ENGINE = InnoDB;

-- Table LABORATORIOS
CREATE TABLE IF NOT EXISTS `LABORATORIOS`(
	`nombre` VARCHAR(200) NOT NULL,
	`numComputadores` INT NOT NULL,
	`encargado` VARCHAR(200) NOT NULL,
	PRIMARY KEY (`nombre`))
ENGINE = InnoDB;

-- Table PROFESORES
CREATE TABLE IF NOT EXISTS `PROFESORES`(
	`codigo` BIGINT NOT NULL,
	`nombre` VARCHAR(200) NOT NULL,
	`codigoNombre` VARCHAR(5) NOT NULL,
	`email` VARCHAR(200) NOT NULL,
	`telefono` BIGINT NULL,
	`cedula` BIGINT NOT NULL,
	PRIMARY KEY (`codigo`))
ENGINE = InnoDB;

-- Table PROFESORESASIGNATURAS
CREATE TABLE IF NOT EXISTS `PROFESORESASIGNATURAS`(
	`profesores_codigo` BIGINT NOT NULL,
	`asignaturas_mnemonico` VARCHAR(5)  NOT NULL,
	PRIMARY KEY (`profesores_codigo`,`asignaturas_mnemonico`))
ENGINE = InnoDB;

-- Table RESERVAS
CREATE TABLE IF NOT EXISTS `RESERVAS`(
	`id` BIGINT NOT NULL,
	`fechaRadicado` DATE NOT NULL,
	`dia` INT NOT NULL,
	`semana` INT NOT NULL,
	`asignatura` VARCHAR(5) NOT NULL,
	`laboratorio_nombre` VARCHAR(200)  NOT NULL,
	`profesores_codigo` BIGINT NOT NULL,
	PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- Table SOFTWARES
CREATE TABLE IF NOT EXISTS `SOFTWARES`(
	`nombre` VARCHAR(200)  NOT NULL,
	`licencia` VARCHAR(500)  NOT NULL,
	`version` VARCHAR(10)  NOT NULL,
	`urlDown` VARCHAR(500)  NOT NULL,
	PRIMARY KEY (`nombre`))
ENGINE = InnoDB;

-- Table SOLICITUDES
CREATE TABLE IF NOT EXISTS `SOLICITUDES`(
	`id` INT NOT NULL,
	`fecha` DATE NOT NULL,
	`estado` BOOLEAN NOT NULL,
	`comentario` VARCHAR(500)  NULL,
	`profesores_codigo` BIGINT NOT NULL,
	`laboratorio_nombre` VARCHAR(200)  NOT NULL,
	PRIMARY KEY (`id`))
ENGINE = InnoDB;
