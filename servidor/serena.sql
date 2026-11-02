-- ======================================================
--  BASE DE DATOS: serena
--  App Serena (Android + Jetpack Compose + Retrofit)
--  Mabel Castro Vidarte
--
--  Cómo usarlo:
--  1. Entra al phpMyAdmin de tu hosting.
--  2. Crea la base de datos (o usa la que te dio el hosting).
--  3. Abre la pestaña "Importar" y sube este archivo.
-- ======================================================

SET NAMES utf8mb4;

-- ------------------------------------------------------
-- Tabla 1: categorias
-- Los tipos de práctica que muestra el Home en círculos.
-- ------------------------------------------------------
DROP TABLE IF EXISTS meditaciones;
DROP TABLE IF EXISTS categorias;

CREATE TABLE categorias (
    idcategoria   INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(60)  NOT NULL,
    descripcion   VARCHAR(200) NOT NULL,
    habilitado    TINYINT(1)   NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- El icono y el color de cada categoria los decide la app,
-- no se guardan aqui porque son parte del diseno.
INSERT INTO categorias (idcategoria, nombre, descripcion) VALUES
(1, 'Ansiedad',     'Prácticas cortas para calmar la mente cuando todo se siente acelerado'),
(2, 'Dormir',       'Sonidos y respiraciones suaves para quedarte dormida sin esfuerzo'),
(3, 'Diario',       'Escribe cómo te sentiste hoy y observa tu avance de la semana'),
(4, 'Respiración',  'Ejercicios guiados de respiración de 3 a 10 minutos'),
(5, 'Concentración','Sesiones para estudiar o trabajar sin distraerte'),
(6, 'Gratitud',     'Un momento al día para reconocer lo bueno que ya tienes');

-- ------------------------------------------------------
-- Tabla 2: meditaciones
-- Cada sesión pertenece a una categoría (clave foránea).
-- ------------------------------------------------------
CREATE TABLE meditaciones (
    idmeditacion  INT AUTO_INCREMENT PRIMARY KEY,
    idcategoria   INT          NOT NULL,
    titulo        VARCHAR(100) NOT NULL,
    descripcion   VARCHAR(255) NOT NULL,
    duracion      INT          NOT NULL,
    nivel         VARCHAR(20)  NOT NULL,
    autor         VARCHAR(60)  NOT NULL,
    destacado     TINYINT(1)   NOT NULL DEFAULT 0,
    habilitado    TINYINT(1)   NOT NULL DEFAULT 1,
    CONSTRAINT fk_meditacion_categoria
        FOREIGN KEY (idcategoria) REFERENCES categorias (idcategoria)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO meditaciones (idcategoria, titulo, descripcion, duracion, nivel, autor, destacado) VALUES
(1, 'Paz en tu día',              'Aprende a meditar en solo 5 minutos y vuelve a tu centro', 5,  'Principiante', 'Serena', 1),
(1, 'Soltar la preocupación',     'Una guía para dejar ir lo que no puedes controlar',        12, 'Principiante', 'Lucía Fernández', 0),
(1, 'Calma antes del examen',     'Baja el ritmo del corazón antes de una prueba importante', 8,  'Principiante', 'Serena', 0),
(2, 'Lluvia sobre el bosque',     'Sonido continuo de lluvia para dormir profundo',           45, 'Principiante', 'Serena', 1),
(2, 'Cuerpo pesado',              'Relajación guiada desde los pies hasta la frente',         20, 'Intermedio',   'Diego Ramos', 0),
(2, 'Noche sin pantallas',        'Rutina de diez minutos para cerrar el día',                10, 'Principiante', 'Serena', 0),
(3, 'Mi semana en tres líneas',   'Una pauta breve para escribir cómo te fue',                6,  'Principiante', 'Serena', 0),
(3, 'Carta a mi yo de ayer',      'Escritura guiada para entender un día difícil',            15, 'Intermedio',   'Lucía Fernández', 0),
(4, 'Respiración 4-7-8',          'Inhala cuatro, sostén siete, exhala ocho',                 3,  'Principiante', 'Serena', 1),
(4, 'Respiración cuadrada',       'Cuatro tiempos iguales para ordenar la mente',             7,  'Intermedio',   'Diego Ramos', 0),
(5, 'Enfoque profundo',           'Ruido blanco suave para bloques largos de estudio',        60, 'Intermedio',   'Serena', 0),
(5, 'Volver a la tarea',          'Microsesión para retomar cuando te distraes',              2,  'Principiante', 'Serena', 0),
(6, 'Tres cosas buenas',          'Reconoce tres momentos del día que valieron la pena',      5,  'Principiante', 'Lucía Fernández', 0),
(6, 'Gracias a mi cuerpo',        'Una práctica de gratitud hacia ti misma',                  9,  'Intermedio',   'Serena', 0);
