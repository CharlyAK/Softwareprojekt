<?php
 
/*
 * Following code will get the playerID of on player
 * A player is identified by his/her combination of email and password
 */
 
// array for JSON response
$response = array();

// connecting to db
$link = mysql_connect("localhost", "jkloss", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
or die("Auswahl der Datenbank fehlgeschlagen");

if (!empty($_POST['playerID']) && isset($_POST['playerID']))
{
	$playerID = $_POST['playerID'];
	mysql_query("DELETE FROM _PLAYER WHERE playerID = '$playerID'")
	or die("Löschen des Spielers fehlgeschlagen");
}
else 
 {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}

?>