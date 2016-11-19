<?php

/*
 * This file updates the player_game table
 * It is only used for debugging purposes at the moment
 */

// array for JSON response
$response = array();

// connecting to db
$link = mysql_connect("localhost", "jkloss", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
or die("Auswahl der Datenbank fehlgeschlagen");

// check for required fields
if (isset($_POST['gameID'])) {
    $gameID = $_POST["gameID"];

    mysql_query("UPDATE `player_game` SET `ready`= 1 WHERE `gameID` = '$gameID'");

	$response["success"] = 1;
        $response["message"] = "Player successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
}


?>
