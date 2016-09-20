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

if (isset($_GET["gameID"]) && !empty($_GET["gameID"])) {

    $gameID = $_GET["gameID"];
	
    // 
    $result = mysql_query("SELECT phases FROM _PHASES WHERE gameID = '$gameID' AND currentPhase = 1") 
	or die("Keine Phase gefunden");

    // check for empty result
    if (mysql_num_rows($result) > 0) {
        // looping through all results
        $response["currentPhase"] = array();

        while ($row = mysql_fetch_array($result)) {
            // temp user array
            $currentPhase = array();
            $currentPhase["currentPhase"] = $row["phases"];

            // push single player into final response array
            array_push($response["currentPhase"], $currentPhase);
        }
        // success
        $response["success"] = 1;

        // echoing JSON response
        echo json_encode($response);
    } else {
        // no player found
        $response["success"] = 0;
        $response["message"] = "No phase found";

        // echo no users JSON
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