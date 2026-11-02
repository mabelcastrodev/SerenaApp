-- ======================================================
--  SOLO SI TU BASE YA TIENE DATOS (no borra nada)
--  Agrega la columna "imagen" a las dos tablas y le pone
--  a cada fila la ruta de su imagen:
--    imagenes/categorias/c<id>.jpg
--    imagenes/meditaciones/m<id>.jpg
--  Se puede ejecutar mas de una vez sin problema (MariaDB).
-- ======================================================

ALTER TABLE categorias   ADD COLUMN IF NOT EXISTS imagen VARCHAR(200) NOT NULL DEFAULT '';
ALTER TABLE meditaciones ADD COLUMN IF NOT EXISTS imagen VARCHAR(200) NOT NULL DEFAULT '';

UPDATE categorias   SET imagen = CONCAT('imagenes/categorias/c',   idcategoria,  '.jpg');
UPDATE meditaciones SET imagen = CONCAT('imagenes/meditaciones/m', idmeditacion, '.jpg');
