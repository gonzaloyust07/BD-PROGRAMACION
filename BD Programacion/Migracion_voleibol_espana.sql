-- Migración única desde el esquema anterior de voleibol_espana.
-- Conserva los equipos: traslada la ciudad a localidad y su CCAA al equipo.
-- No elimina la base de datos ni las tablas ccaa/ciudades.

USE voleibol_espana;

INSERT INTO ccaa (nombre, capital_regional)
SELECT datos.nombre, datos.capital
FROM (
    SELECT 'Andalucía' AS nombre, 'Sevilla' AS capital UNION ALL
    SELECT 'Aragón', 'Zaragoza' UNION ALL
    SELECT 'Principado de Asturias', 'Oviedo' UNION ALL
    SELECT 'Islas Baleares', 'Palma' UNION ALL
    SELECT 'Canarias', 'Las Palmas de Gran Canaria' UNION ALL
    SELECT 'Cantabria', 'Santander' UNION ALL
    SELECT 'Castilla-La Mancha', 'Toledo' UNION ALL
    SELECT 'Castilla y León', 'Valladolid' UNION ALL
    SELECT 'Cataluña', 'Barcelona' UNION ALL
    SELECT 'Comunidad Valenciana', 'Valencia' UNION ALL
    SELECT 'Extremadura', 'Mérida' UNION ALL
    SELECT 'Galicia', 'Santiago de Compostela' UNION ALL
    SELECT 'Comunidad de Madrid', 'Madrid' UNION ALL
    SELECT 'Región de Murcia', 'Murcia' UNION ALL
    SELECT 'Comunidad Foral de Navarra', 'Pamplona' UNION ALL
    SELECT 'País Vasco', 'Vitoria-Gasteiz' UNION ALL
    SELECT 'La Rioja', 'Logroño' UNION ALL
    SELECT 'Ceuta', 'Ceuta' UNION ALL
    SELECT 'Melilla', 'Melilla'
) AS datos
WHERE NOT EXISTS (
    SELECT 1
    FROM ccaa existente
    WHERE existente.nombre = datos.nombre
       OR (datos.nombre = 'Principado de Asturias' AND existente.nombre = 'Asturias')
);

UPDATE ccaa
SET nombre = 'Principado de Asturias', capital_regional = 'Oviedo'
WHERE nombre = 'Asturias';

ALTER TABLE equipos
    ADD COLUMN localidad VARCHAR(100) NULL AFTER division,
    ADD COLUMN id_ccaa INT NULL AFTER localidad;

UPDATE equipos e
JOIN ciudades c ON e.id_ciudad = c.id_ciudad
SET e.localidad = c.nombre, e.id_ccaa = c.id_ccaa;

UPDATE equipos
SET localidad = 'Sin especificar'
WHERE localidad IS NULL;

UPDATE equipos
SET id_ccaa = (
    SELECT id_ccaa FROM ccaa
    WHERE nombre = 'Comunidad de Madrid'
    ORDER BY id_ccaa LIMIT 1
)
WHERE id_ccaa IS NULL;

UPDATE equipos
SET division = 'Superliga / Liga Iberdrola'
WHERE division IN ('Superliga', 'Superliga 1', 'Liga Iberdrola');

UPDATE equipos
SET division = 'Primera División Nacional'
WHERE division IN ('Primera Nacional', 'Primera División Nacional');

ALTER TABLE equipos
    DROP FOREIGN KEY equipos_ibfk_1,
    DROP COLUMN pabellon,
    DROP COLUMN id_ciudad,
    MODIFY localidad VARCHAR(100) NOT NULL,
    MODIFY id_ccaa INT NOT NULL,
    ADD CONSTRAINT fk_equipos_ccaa FOREIGN KEY (id_ccaa)
        REFERENCES ccaa (id_ccaa)
        ON UPDATE CASCADE;

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

INSERT IGNORE INTO usuarios (login, password) VALUES ('admin', 'admin');
