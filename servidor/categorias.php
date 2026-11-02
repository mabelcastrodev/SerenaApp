<?php
/*
 * SERVICIO: categorias.php
 * Devuelve las categorías de Serena con el total de sesiones
 * que tiene cada una y la ruta de su imagen.
 *
 * Ejemplo de respuesta:
 * [{"idcategoria":1,"nombre":"Ansiedad","descripcion":"...","total":6,
 *   "imagen":"imagenes/categorias/c1.jpg"}]
 */

require_once "conexion.php";

$conexion = conectar();

$sql = "SELECT c.idcategoria,
               c.nombre,
               c.descripcion,
               c.imagen,
               COUNT(m.idmeditacion) AS total
        FROM categorias c
        LEFT JOIN meditaciones m
               ON m.idcategoria = c.idcategoria
              AND m.habilitado = 1
        WHERE c.habilitado = 1
        GROUP BY c.idcategoria, c.nombre, c.descripcion, c.imagen
        ORDER BY c.idcategoria";

$consulta = $conexion->prepare($sql);
$consulta->execute();

$filas = $consulta->fetchAll(PDO::FETCH_ASSOC);

// Los números llegan como texto desde MySQL, los convertimos
// para que Gson los lea como Int en Android.
foreach ($filas as $i => $fila) {
    $filas[$i]["idcategoria"] = (int) $fila["idcategoria"];
    $filas[$i]["total"] = (int) $fila["total"];
}

responder($filas);
