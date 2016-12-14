<?php

/*
 * Following code will set the loginState of a player
 * playerID is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['playerID']) && !empty($_POST['playerID']) && isset($_POST['login']) && !empty($_POST['login'])) {

	$playerID = $_POST['playerID'];
	$login = $_POST['login'];

	mysql_query("UPDATE _PLAYER SET login = '$login' WHERE playerID = '$playerID'")
       	or die("Setzen von login fehlgeschlagen");

} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>