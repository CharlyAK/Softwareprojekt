<?php

$link = mysql_connect("localhost", "jkloss", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

mysql_query("DELETE FROM `player_game` WHERE 1");
mysql_query("DELETE FROM `_GAME` WHERE 1");
mysql_query("DELETE FROM `_PHASES` WHERE 1");

$response["success"] = 1;
$response["message"] = "Table was successfully reset";

// echoing JSON response
echo json_encode($response);

?>