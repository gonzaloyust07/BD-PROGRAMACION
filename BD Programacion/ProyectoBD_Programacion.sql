-- Esquema para una instalación nueva de la aplicación de gestión de vóley.
-- Para una base voleibol_espana antigua, no ejecutes este archivo:
-- ejecuta Migracion_voleibol_espana.sql una sola vez.

CREATE DATABASE IF NOT EXISTS voleibol_espana
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;

USE voleibol_espana;

CREATE TABLE IF NOT EXISTS ccaa (
    id_ccaa INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    capital_regional VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_ccaa),
    UNIQUE KEY uq_ccaa_nombre (nombre)
);

CREATE TABLE IF NOT EXISTS equipos (
    id_equipo INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    rama VARCHAR(30) NOT NULL,
    division VARCHAR(100) NOT NULL,
    localidad VARCHAR(100) NOT NULL,
    fundacion INT NULL,
    id_ccaa INT NOT NULL,
    PRIMARY KEY (id_equipo),
    CONSTRAINT fk_equipos_ccaa FOREIGN KEY (id_ccaa)
        REFERENCES ccaa (id_ccaa)
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS jugadores (
    id_jugador INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    posicion VARCHAR(40) NOT NULL,
    numero INT NOT NULL,
    edad INT NOT NULL,
    nacionalidad VARCHAR(60) NOT NULL,
    capitan TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id_jugador)
);

CREATE TABLE IF NOT EXISTS equipo_jugador (
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
);

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    login VARCHAR(60) NOT NULL,
    password VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY uq_usuarios_login (login)
);

INSERT IGNORE INTO ccaa (nombre, capital_regional) VALUES
    ('Andalucía', 'Sevilla'),
    ('Aragón', 'Zaragoza'),
    ('Principado de Asturias', 'Oviedo'),
    ('Islas Baleares', 'Palma'),
    ('Canarias', 'Las Palmas de Gran Canaria'),
    ('Cantabria', 'Santander'),
    ('Castilla-La Mancha', 'Toledo'),
    ('Castilla y León', 'Valladolid'),
    ('Cataluña', 'Barcelona'),
    ('Comunidad Valenciana', 'Valencia'),
    ('Extremadura', 'Mérida'),
    ('Galicia', 'Santiago de Compostela'),
    ('Comunidad de Madrid', 'Madrid'),
    ('Región de Murcia', 'Murcia'),
    ('Comunidad Foral de Navarra', 'Pamplona'),
    ('País Vasco', 'Vitoria-Gasteiz'),
    ('La Rioja', 'Logroño'),
    ('Ceuta', 'Ceuta'),
    ('Melilla', 'Melilla');

INSERT IGNORE INTO usuarios (login, password) VALUES ('admin', 'admin');
