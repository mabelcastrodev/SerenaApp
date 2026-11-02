<?php
/*
 * CONEXIÓN A LA BASE DE DATOS DE SERENA
 *
 * Cambia los cuatro datos de abajo por los que te dé tu hosting
 * cuando crees la base de datos.
 */

$servidor = "localhost";       // casi siempre es localhost
$basedatos = "serena";         // nombre de la base de datos
$usuario = "root";             // usuario de MySQL
$clave = "";                   // contraseña de MySQL

function conectar()
{
    global $servidor, $basedatos, $usuario, $clave;

    $conexion = new PDO(
        "mysql:host=$servidor;dbname=$basedatos;charset=utf8mb4",
        $usuario,
        $clave
    );
    $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    return $conexion;
}

/*
 * Devuelve el resultado de una consulta como JSON,
 * igual que los servicios que usamos en clase.
 */
function responder($filas)
{
    header("Content-Type: application/json; charset=utf-8");
    echo json_encode($filas, JSON_UNESCAPED_UNICODE);
}
