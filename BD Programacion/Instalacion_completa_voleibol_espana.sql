CREATE DATABASE IF NOT EXISTS voleibol_espana
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;

USE voleibol_espana;
SET NAMES utf8mb4;

CREATE TABLE ccaa (
    id_ccaa INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    capital_regional VARCHAR(100) NULL,
    PRIMARY KEY (id_ccaa),
    UNIQUE KEY uq_ccaa_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE ciudades (
    id_ciudad INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    poblacion INT NULL,
    id_ccaa INT NULL,
    PRIMARY KEY (id_ciudad),
    KEY id_ccaa (id_ccaa),
    CONSTRAINT ciudades_ibfk_1 FOREIGN KEY (id_ccaa)
        REFERENCES ccaa (id_ccaa)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE equipos (
    id_equipo INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    rama VARCHAR(20) NOT NULL,
    division VARCHAR(50) NOT NULL,
    localidad VARCHAR(100) NOT NULL,
    id_ccaa INT NOT NULL,
    fundacion INT NULL,
    PRIMARY KEY (id_equipo),
    KEY fk_equipos_ccaa (id_ccaa),
    CONSTRAINT fk_equipos_ccaa FOREIGN KEY (id_ccaa)
        REFERENCES ccaa (id_ccaa)
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE jugadores (
    id_jugador INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    posicion VARCHAR(40) NOT NULL,
    numero INT NOT NULL,
    edad INT NOT NULL,
    nacionalidad VARCHAR(60) NOT NULL,
    capitan TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id_jugador)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE equipo_jugador (
    id_equipo INT NOT NULL,
    id_jugador INT NOT NULL,
    PRIMARY KEY (id_equipo, id_jugador),
    CONSTRAINT fk_equipo_jugador_equipo FOREIGN KEY (id_equipo)
        REFERENCES equipos (id_equipo)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_equipo_jugador_jugador FOREIGN KEY (id_jugador)
        REFERENCES jugadores (id_jugador)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuarios (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    login VARCHAR(60) NOT NULL,
    password VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY uq_usuarios_login (login)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO ccaa (id_ccaa, nombre, capital_regional) VALUES
    (1, 'Canarias', 'Las Palmas / Santa Cruz de Tenerife'),
    (2, 'Andalucía', 'Sevilla'),
    (3, 'Islas Baleares', 'Palma de Mallorca'),
    (4, 'La Rioja', 'Logroño'),
    (5, 'Galicia', 'Santiago de Compostela'),
    (6, 'Comunidad de Madrid', 'Madrid'),
    (7, 'Cataluña', 'Barcelona'),
    (8, 'Castilla y León', 'Valladolid'),
    (9, 'Región de Murcia', 'Murcia'),
    (11, 'Aragón', 'Zaragoza'),
    (12, 'Principado de Asturias', 'Oviedo'),
    (13, 'Cantabria', 'Santander'),
    (14, 'Castilla-La Mancha', 'Toledo'),
    (15, 'Comunidad Valenciana', 'Valencia'),
    (16, 'Extremadura', 'Mérida'),
    (17, 'Comunidad Foral de Navarra', 'Pamplona'),
    (18, 'País Vasco', 'Vitoria-Gasteiz'),
    (19, 'Ceuta', 'Ceuta'),
    (20, 'Melilla', 'Melilla');

INSERT INTO ciudades (id_ciudad, nombre, poblacion, id_ccaa) VALUES
    (1, 'Las Palmas de Gran Canaria', 378000, 1),
    (2, 'Almería', 200000, 2),
    (3, 'Palma', 420000, 3),
    (4, 'Manacor', 44000, 3),
    (5, 'Haro', 11000, 4),
    (6, 'Lugo', 98000, 5),
    (7, 'San Sebastián de los Reyes', 91000, 6),
    (8, 'Leganés', 190000, 6),
    (9, 'Barcelona', 1620000, 7),
    (10, 'Soria', 390000, 8),
    (11, 'Murcia', 460000, 9),
    (12, 'Gijón', 270000, 12),
    (13, 'Sabadell', 215000, 7);

INSERT INTO equipos
    (id_equipo, nombre, rama, division, localidad, id_ccaa, fundacion) VALUES
    (2, 'Unicaja Costa de Almería', 'Masculino', 'Superliga / Liga Iberdrola', 'Almería', 2, 1986),
    (3, 'Grupo Herce Soria', 'Masculino', 'Superliga / Liga Iberdrola', 'Soria', 8, 1988),
    (4, 'Conectabalear CV Manacor', 'Masculino', 'Superliga / Liga Iberdrola', 'Manacor', 3, 1989),
    (5, 'Club Vóley Palma', 'Masculino', 'Superliga / Liga Iberdrola', 'Palma', 3, 2013),
    (6, 'Arenal Emevé', 'Masculino', 'Superliga / Liga Iberdrola', 'Lugo', 5, 1982),
    (7, 'CV Sayre Mayo Suministros', 'Femenino', 'Superliga / Liga Iberdrola', 'Las Palmas de Gran Canaria', 1, 2010),
    (8, 'OCISA Haro Rioja Voley', 'Femenino', 'Superliga / Liga Iberdrola', 'Haro', 4, 1996),
    (9, 'Arenal Emevé Femenino', 'Femenino', 'Superliga / Liga Iberdrola', 'Lugo', 5, 1982),
    (10, 'Club Voleibol Leganés', 'Masculino', 'Superliga 2', 'Leganés', 6, 1972),
    (11, 'Voley Playa Madrid', 'Masculino', 'Superliga 2', 'San Sebastián de los Reyes', 6, 2004),
    (12, 'Intasa San Sadurniño', 'Masculino', 'Superliga 2', 'Lugo', 5, 1993),
    (13, 'CV Sayre Décimas', 'Femenino', 'Superliga 2', 'Las Palmas de Gran Canaria', 1, 2010),
    (14, 'Fedivalca Volei Grau', 'Femenino', 'Superliga 2', 'Barcelona', 7, 1985),
    (15, 'Zalaeta', 'Femenino', 'Superliga 2', 'Lugo', 5, 1975),
    (16, 'CVS Tren de Sóller', 'Masculino', 'Primera División Nacional', 'Palma', 3, 2002),
    (17, 'Club Natació Sabadell', 'Masculino', 'Primera División Nacional', 'Sabadell', 7, 1916),
    (18, 'CV Sanse', 'Femenino', 'Primera División Nacional', 'San Sebastián de los Reyes', 6, 1992),
    (19, 'RGC Covadonga', 'Femenino', 'Primera División Nacional', 'Gijón', 12, 1941);

INSERT INTO jugadores
    (id_jugador, nombre, posicion, numero, edad, nacionalidad, capitan) VALUES
    (1, 'Alejandro Martín', 'Colocador', 1, 25, 'Española', 1),
    (2, 'Daniel Ortega', 'Opuesto', 2, 27, 'Española', 1),
    (3, 'Javier Santos', 'Receptor', 3, 24, 'Española', 1),
    (4, 'Marcos Vidal', 'Central', 4, 26, 'Española', 1),
    (5, 'Pablo Romero', 'Líbero', 5, 23, 'Española', 1),
    (6, 'Hugo Navarro', 'Colocador', 6, 28, 'Española', 1),
    (7, 'Adrián Torres', 'Receptor', 7, 22, 'Española', 1),
    (8, 'Sergio Molina', 'Central', 8, 29, 'Española', 1),
    (9, 'Lucía Fernández', 'Colocador', 9, 24, 'Española', 1),
    (10, 'Marta Ruiz', 'Opuesto', 10, 26, 'Española', 1),
    (11, 'Elena Castro', 'Receptor', 11, 23, 'Española', 1),
    (12, 'Paula Jiménez', 'Central', 12, 25, 'Española', 1),
    (13, 'Claudia Moreno', 'Líbero', 13, 21, 'Española', 1),
    (14, 'Irene Romero', 'Colocador', 14, 27, 'Española', 1),
    (15, 'Nerea Sánchez', 'Receptor', 15, 22, 'Española', 1),
    (16, 'Aitor López', 'Opuesto', 16, 24, 'Española', 1),
    (17, 'Unai García', 'Central', 17, 26, 'Española', 1),
    (18, 'Diego Pérez', 'Líbero', 18, 23, 'Española', 1);

INSERT INTO equipo_jugador (id_equipo, id_jugador) VALUES
    (2, 1),
    (3, 2),
    (4, 3),
    (5, 4),
    (6, 5),
    (7, 6),
    (8, 7),
    (9, 8),
    (10, 9),
    (11, 10),
    (12, 11),
    (13, 12),
    (14, 13),
    (15, 14),
    (16, 15),
    (17, 16),
    (18, 17),
    (19, 18);

INSERT INTO usuarios (id_usuario, login, password) VALUES
    (1, 'admin', 'admin');

ALTER TABLE ccaa AUTO_INCREMENT = 21;
ALTER TABLE ciudades AUTO_INCREMENT = 14;
ALTER TABLE equipos AUTO_INCREMENT = 22;
ALTER TABLE jugadores AUTO_INCREMENT = 19;
ALTER TABLE usuarios AUTO_INCREMENT = 2;
