-- ============================================================
--  SCRIPT DE CREACIÓN: liga_voley
--  Proyecto: Gestión de Liga de Voleibol
--  Usuario MySQL: root / Contraseña: root
-- ============================================================

-- 1. Crear la base de datos (si no existe)
CREATE DATABASE IF NOT EXISTS liga_voley
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;

USE liga_voley;

-- ============================================================
-- TABLA: entrenador
-- ============================================================
CREATE TABLE IF NOT EXISTS entrenador (
    idEntrenador    INT          NOT NULL AUTO_INCREMENT,
    nombreCompleto  VARCHAR(100) NOT NULL,
    edad            INT          NOT NULL,
    nacionalidad    VARCHAR(60)  NOT NULL,
    precio          DOUBLE       NOT NULL DEFAULT 0.0,
    PRIMARY KEY (idEntrenador)
);

-- ============================================================
-- TABLA: equipo
-- ============================================================
CREATE TABLE IF NOT EXISTS equipo (
    idEquipo        INT          NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(100) NOT NULL,
    presupuesto     DOUBLE       NOT NULL DEFAULT 0.0,
    idEntrenador    INT          NULL,          -- puede no tener entrenador aún
    PRIMARY KEY (idEquipo),
    FOREIGN KEY (idEntrenador) REFERENCES entrenador(idEntrenador)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- ============================================================
-- TABLA: jugador
-- ============================================================
CREATE TABLE IF NOT EXISTS jugador (
    idJugador       INT          NOT NULL AUTO_INCREMENT,
    nombreCompleto  VARCHAR(100) NOT NULL,
    nombreCamiseta  VARCHAR(60)  NOT NULL,
    numero          INT          NOT NULL,
    edad            INT          NOT NULL,
    nacionalidad    VARCHAR(60)  NOT NULL,
    posicion        VARCHAR(40)  NOT NULL,
    lesionado       TINYINT(1)   NOT NULL DEFAULT 0,   -- 0 = sano, 1 = lesionado
    precio          DOUBLE       NOT NULL DEFAULT 0.0,
    PRIMARY KEY (idJugador)
);

-- ============================================================
-- TABLA: equipo_jugador  (relación N:M entre equipo y jugador)
-- Un jugador puede haber estado en varios equipos (traspasos)
-- Un equipo tiene varios jugadores
-- ============================================================
CREATE TABLE IF NOT EXISTS equipo_jugador (
    idEquipo        INT  NOT NULL,
    idJugador       INT  NOT NULL,
    PRIMARY KEY (idEquipo, idJugador),
    FOREIGN KEY (idEquipo)  REFERENCES equipo(idEquipo)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idJugador) REFERENCES jugador(idJugador)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ============================================================
-- DATOS DE EJEMPLO (opcionales, para probar)
-- ============================================================

INSERT INTO entrenador (nombreCompleto, edad, nacionalidad, precio) VALUES
('Carlos Martínez',  52, 'Española',   95000.0),
('Luca Rossi',       45, 'Italiana',  120000.0);

INSERT INTO equipo (nombre, presupuesto, idEntrenador) VALUES
('Club Voley Madrid',    500000.0, 1),
('Deportivo Coastal',    320000.0, 2);

INSERT INTO jugador (nombreCompleto, nombreCamiseta, numero, edad, nacionalidad, posicion, precio) VALUES
('Alejandro García',  'GARCÍA',   4,  24, 'Española',  'Líbero',        35000.0),
('Bruno Fernández',   'BRUNO',    7,  27, 'Española',  'Central',       42000.0),
('Marco Conti',       'CONTI',   10,  29, 'Italiana',  'Opuesto',       58000.0),
('Pedro Alves',       'ALVES',    3,  22, 'Portuguesa','Receptor',      27000.0),
('Iván Torres',       'TORRES',   6,  31, 'Española',  'Colocador',     61000.0),
('Sergio Ruiz',       'RUIZ',     9,  25, 'Española',  'Central',       39000.0),
('Antoine Dupont',    'DUPONT',  11,  26, 'Francesa',  'Receptor',      44000.0),
('Mateo López',       'LÓPEZ',    2,  23, 'Argentina', 'Líbero',        30000.0),
('Nicolás Vargas',    'VARGAS',   5,  28, 'Chilena',   'Opuesto',       52000.0),
('Luis Castro',       'CASTRO',   8,  30, 'Española',  'Colocador',     47000.0);

-- Asignar jugadores 1-7 al equipo 1, jugadores 8-10 al equipo 2
INSERT INTO equipo_jugador (idEquipo, idJugador) VALUES
(1, 1),(1, 2),(1, 3),(1, 4),(1, 5),(1, 6),(1, 7),
(2, 8),(2, 9),(2,10);

-- ============================================================
-- CONSULTAS DE EJEMPLO (para practicar / mostrar en el documento)
-- ============================================================

-- Ver todos los jugadores de un equipo concreto
-- SELECT j.* FROM jugador j
-- JOIN equipo_jugador ej ON j.idJugador = ej.idJugador
-- WHERE ej.idEquipo = 1;

-- Ver todos los equipos con su entrenador
-- SELECT e.nombre, e.presupuesto, en.nombreCompleto AS entrenador
-- FROM equipo e
-- LEFT JOIN entrenador en ON e.idEntrenador = en.idEntrenador;

-- Ver jugadores lesionados
-- SELECT * FROM jugador WHERE lesionado = 1;

-- Contar jugadores por equipo
-- SELECT e.nombre, COUNT(ej.idJugador) AS totalJugadores
-- FROM equipo e
-- LEFT JOIN equipo_jugador ej ON e.idEquipo = ej.idEquipo
-- GROUP BY e.idEquipo;
