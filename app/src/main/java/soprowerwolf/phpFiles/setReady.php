<?php

/*
 * Following code will set the current victims
 * All details are read from HTTP Post Request
 * GameID, victimWer/ victimDor is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['gameID']) && !empty($_POST['gameID']) && isset($_POST['playerID']) && !empty($_POST['playerID'])) {

	$gameID = $_POST['gameID'];
	$playerID = $_POST['playerID'];
	
	mysql_query("UPDATE player_game SET ready = 1 WHERE gameID = '$gameID' AND playerID = '$playerID'")
	or die("Setzen von ready fehlgeschlagen");
	
	// check if ready is set
	$result = mysql_query("SELECT ready FROM player_game WHERE gameID = '$gameID' AND playerID = '$playerID'");
	if($result == 1)
	{
		$response["success"] = 1;
		$response["message"] = "Player is ready";

		// echoing JSON response
		echo json_encode($response);
	}
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>