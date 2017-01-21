<?php

// Deletes player_game-entry or player_game-, game- & phases-entry, after leaving an game
// Needs [playerID] or [playerID & gameID]

// array for JSON response
$response = array();

// connecting to db
$link = mysql_connect("localhost", "jkloss", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
or die("Auswahl der Datenbank fehlgeschlagen");


if(!empty($_GET['gameID']) && isset($_GET['gameID']) && !empty($_GET['playerID']) && isset($_GET['playerID'])){
    $gameID = $_GET['gameID'];
    $playerID = $_GET['playerID'];

    mysql_query("DELETE FROM player_game WHERE playerID = '$playerID'")
        or die("Löschen des Spielers aus player_game fehlgeschlagen");
    mysql_query("DELETE FROM _GAME WHERE gameID = '$gameID'")
        or die("Löschen des Spiels fehlgeschlagen");
    mysql_query("DELETE FROM _PHASES WHERE gameID = '$gameID'")
        or die("Löschen der Phasen fehlgeschlagen");
}

else if (!empty($_GET['playerID']) && isset($_GET['playerID'])){
	$playerID = $_GET['playerID'];

	mysql_query("DELETE FROM player_game WHERE playerID = '$playerID'")
	    or die("Löschen des Spielers aus player_game fehlgeschlagen");
}

else if(!empty($_POST['gameID']) && isset($_POST['gameID']))
{
    $gameID = $_POST['gameID'];

     mysql_query("DELETE FROM player_game WHERE gameID = '$gameID'")
            or die("Löschen des Spiels aus player_game fehlgeschlagen");
     mysql_query("DELETE FROM _GAME WHERE gameID = '$gameID'")
            or die("Löschen des Spiels auf _game fehlgeschlagen");
     mysql_query("DELETE FROM _PHASES WHERE gameID = '$gameID'")
            or die("Löschen der Phasen fehlgeschlagen");
}
else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing: exitGame";

    // echoing JSON response
    echo json_encode($response);
}

?>