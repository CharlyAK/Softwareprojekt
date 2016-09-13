<?php

/*
 * Following code will set the Lovers for the current game
 * All details are read from HTTP Post Request
 * GameID, Lover1 and Lover2 is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['gameID']) && !empty($_POST['gameID']) && isset($_POST['lover1']) && !empty($_POST['lover1']) && isset($_POST['lover2']) && !empty($_POST['lover2'])) {

    $gameID = $_POST['gameID'];
    $lover1 = $_POST['lover1'];
	$lover2 = $_POST['lover2'];

    // set lover1
    mysql_query("UPDATE player_game SET lover='$lover1' WHERE gameID = '$gameID' AND playerID = '$lover2'")
	    or die("Setzen des ersten Lovers ist fehlgeschlagen");
		
	// set lover2
    mysql_query("UPDATE player_game SET lover='$lover2' WHERE gameID = '$gameID' AND playerID = '$lover1'")
	    or die("Setzen des zweiten Lovers ist fehlgeschlagen");	


     // check if lover1 has been set
    $resultLover1 = mysql_query("SELECT lover FROM game WHERE gameID = '$gameID' AND playerID = '$lover2'");
	// check if lover2 has been set
    $resultLover2 = mysql_query("SELECT lover FROM game WHERE gameID = '$gameID' AND playerID = '$lover1'");
    if ($resultLover1 == $lover1 && $resultLover2 == $lover2) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Player successfully added.";

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
