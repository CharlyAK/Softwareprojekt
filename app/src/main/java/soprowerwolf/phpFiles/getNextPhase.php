<?php

// array for JSON response
$response = array();

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


if (isset($_GET["gameID"]) && isset($_GET["currentPhase"])) {

	$gameID = $_GET["gameID"];
	$phase = $_GET["currentPhase"];

    $result = mysql_query("SELECT nextPhase FROM _PHASES WHERE gameID = '$gameID' AND phases = '$phase' ")
        or die("Keine Phase gefunden");

	if (!empty($result)) {

        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $result = mysql_fetch_array($result);

            $phase = array();
	        $phase["nextPhase"] = $result["nextPhase"];

            // success
            $response["success"] = 1;

            // user node
            $response["phase"] = array();

            array_push($response["phase"], $phase);

            // echoing JSON response
            echo json_encode($response);
        }

    } else {
        // no phase found
        $response["success"] = 0;
        $response["message"] = "No phase found";

        // echo no users JSON
        echo json_encode($response);
    }

}

else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}

?>