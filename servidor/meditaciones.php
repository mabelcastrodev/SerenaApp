<?php
/*
 * SERVICIO: meditaciones.php
 * Devuelve las sesiones de Serena junto al nombre de su categoría.
 *
 *   meditaciones.php                 -> todas
 *   meditaciones.php?idcategoria=2   -> solo las de Dormir (Sesiones por categoría)
 *   meditaciones.php?idmeditacion=5  -> solo una (pantalla Detalle)
 */

require_once "conexion.php";

$conexion = conectar();

$idcategoria  = isset($_GET["idcategoria"])  ? (int) $_GET["idcategoria"]  : 0;
$idmeditacion = isset($_GET["idmeditacion"]) ? (int) $_GET["idmeditacion"] : 0;

$sql = "SELECT m.idmeditacion,
               m.idcategoria,
               c.nombre AS categoria,
               m.titulo,
               m.descripcion,
               m.duracion,
               m.nivel,
               m.autor,
               m.destacado,
               m.imagen
        FROM meditaciones m
        INNER JOIN categorias c
                ON c.idcategoria = m.idcategoria
        WHERE m.habilitado = 1";

if ($idcategoria > 0) {
    $sql .= " AND m.idcategoria = :idcategoria";
}
if ($idmeditacion > 0) {
    $sql .= " AND m.idmeditacion = :idmeditacion";
}

$sql .= " ORDER BY m.destacado DESC, m.idmeditacion";

$consulta = $conexion->prepare($sql);

if ($idcategoria > 0) {
    $consulta->bindParam(":idcategoria", $idcategoria, PDO::PARAM_INT);
}
if ($idmeditacion > 0) {
    $consulta->bindParam(":idmeditacion", $idmeditacion, PDO::PARAM_INT);
}

$consulta->execute();

$filas = $consulta->fetchAll(PDO::FETCH_ASSOC);

foreach ($filas as $i => $fila) {
    $filas[$i]["idmeditacion"] = (int) $fila["idmeditacion"];
    $filas[$i]["idcategoria"] = (int) $fila["idcategoria"];
    $filas[$i]["duracion"] = (int) $fila["duracion"];
    $filas[$i]["destacado"] = (int) $fila["destacado"];
}

responder($filas);
