<?php

/*
 * Following code will change "alive"
 * All details are read from HTTP Post Request
 * PlayerGameID is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['gameID']) && !empty($_POST['gameID'])) {

    $gameID = $_POST['gameID'];

	// get victimDor and change "alive" --> if there is a lover - change alive, too
	mysql_query("UPDATE player_game p1,
						(SELECT(sp.victimDor) AS victimDor FROM _GAME sp WHERE gameID = '$gameID') AS p2
					SET p1.alive = 0 WHERE gameID = '$gameID' AND playerID = p2.victimDor");

	$victimDor = mysql_query("SELECT victimDor FROM _GAME WHERE gameID = '$gameID'");

	if(empty($victimDor))
	{
		mysql_query("UPDATE player_game p1,
							(SELECT(sp.victimDor) AS victimDor FROM _GAME sp WHERE gameID = '$gameID') AS p2
						SET p1.alive = 0 WHERE gameID = '$gameID' AND lover = p2.victimDor");
	}

	// get victimWer and change "alive" --> if there is a lover - change alive, too
	mysql_query("UPDATE player_game p1,
						(SELECT(sp.victimWer) AS victimWer FROM _GAME sp WHERE gameID = '$gameID') AS p2
					SET p1.alive = 0 WHERE gameID = '$gameID' AND playerID = p2.victimWer");

	$victimWer = mysql_query("SELECT victimWer FROM _GAME WHERE gameID = '$gameID'");

	if(empty($victimWer))
	{
		mysql_query("UPDATE player_game p1,
							(SELECT(sp.victimWer) AS victimWer FROM _GAME sp WHERE gameID = '$gameID') AS p2
						SET p1.alive = 0 WHERE gameID = '$gameID' AND lover = p2.victimWer");
	}

	// get victimHex and change "alive" --> if there is a lover - change alive, too
	mysql_query("UPDATE player_game p1,
						(SELECT(sp.victimHex) AS victimHex FROM _GAME sp WHERE gameID = '$gameID') AS p2
					SET p1.alive = 0 WHERE gameID = '$gameID' AND playerID = p2.victimHex");

	$victimHex = mysql_query("SELECT victimHex FROM _GAME WHERE gameID = '$gameID'");

	if(empty($victimHex))
	{
		mysql_query("UPDATE player_game p1,
							(SELECT(sp.victimHex) AS victimHex FROM _GAME sp WHERE gameID = '$gameID') AS p2
						SET p1.alive = 0 WHERE gameID = '$gameID' AND lover = p2.victimHex");
	}

	// get victimJaeger and change "alive" --> if there is a lover - change alive, too
	mysql_query("UPDATE player_game p1,
						(SELECT(sp.victimJaeger) AS victimJaeger FROM _GAME sp WHERE gameID = '$gameID') AS p2
					SET p1.alive = 0 WHERE gameID = '$gameID' AND playerID = p2.victimJaeger");

	$victimJaeger = mysql_query("SELECT victimJaeger FROM _GAME WHERE gameID = '$gameID'");

	if(empty($victimJaeger))
	{
		mysql_query("UPDATE player_game p1,
							(SELECT(sp.victimJaeger) AS victimJaeger FROM _GAME sp WHERE gameID = '$gameID') AS p2
						SET p1.alive = 0 WHERE gameID = '$gameID' AND lover = p2.victimJaeger");
	}

	//reset victims
	mysql_query("UPDATE _GAME SET victimDor = 0 WHERE gameID = $gameID");
	mysql_query("UPDATE _GAME SET victimWer = 0 WHERE gameID = $gameID");
	mysql_query("UPDATE _GAME SET victimHex = 0 WHERE gameID = $gameID");
	mysql_query("UPDATE _GAME SET victimJaeger = 0 WHERE gameID = $gameID");

} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing: changeAlive";

    // echoing JSON response
    echo json_encode($response);
}
?>
