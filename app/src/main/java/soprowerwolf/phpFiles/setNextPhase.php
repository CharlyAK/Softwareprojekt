<?php

/*
 * Following code will list all the players
 */

// array for JSON response
$response = array();

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");

if (isset($_POST["gameID"]) && !empty($_POST["gameID"]) && isset($_POST["currentPhase"]) && !empty($_POST["currentPhase"])) {

    $gameID = $_POST["gameID"];
	$currentPhase = $_POST["currentPhase"];
    
	// set nextPhase 
    mysql_query("UPDATE _PHASES p1, 
						(SELECT(sp.nextPhase) AS nextPhase FROM _PHASES sp WHERE gameID = '$gameID' AND phases = '$currentPhase') AS p2 	
					SET p1.currentPhase = 1 WHERE gameID = '$gameID' AND phases = p2.nextPhase");
	// set currentPhase false
	mysql_query("UPDATE _PHASES SET currentPhase = 0 WHERE phases = '$currentPhase'");
	
	//check if currentPhase has been changed
	$resultNewPhase = mysql_query("SELECT currentPhase FROM _PHASES WHERE gameID = '$gameID' AND phases = '$currentPhase'");
	$resultOldPhase = mysql_query("SELECT currentPhase FROM _PHASES WHERE gameID = '$gameID' AND nextPhase = '$currentPhase'");
    if ($resultNewPhase == 1 && $resultOldPhase == 0) 
	{
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Phases successfully updated.";

        // echoing JSON response
        echo json_encode($response); 
    }
}
else if(isset($_POST["gameID"]) && !empty($_POST["gameID"]) && isset($_POST["currentPhase"]) && !empty($_POST["currentPhase"]) && isset($_POST["nextPhase"]) && !empty($_POST["nextPhase"]))
{
	$gameID = $_POST["gameID"];
	$currentPhase = $_POST["currentPhase"];
	$nextPhase = $_POST["nextPhase"];
	
	mysql_query("UPDATE _PHASES SET currentPhase = 1 WHERE gameID = '$gameID' AND phases = '$nextPhase'");
	mysql_query("UPDATE _PHASES SET currentPhase = 0 WHERE gameID = '$gameID' AND phases = '$currentPhase'");
	
	//check if currentPhase has been changed
	$resultNewPhase = mysql_query("SELECT currentPhase FROM _PHASES WHERE gameID = '$gameID' AND phases = '$nextPhase'");
	$resultOldPhase = mysql_query("SELECT currentPhase FROM _PHASES WHERE gameID = '$gameID' AND phases = '$currentPhase'");
    if ($resultNewPhase == 1 && $resultOldPhase == 0) 
	{
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Phases successfully updated.";

        // echoing JSON response
        echo json_encode($response); 
    }
}
else{
        // required field is missing
        $response["success"] = 0;
        $response["message"] = "Required field(s) is missing";

        // echoing JSON response
        echo json_encode($response);
    }

?>