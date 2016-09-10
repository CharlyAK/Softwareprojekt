<?php

/*
 * Following code will change "alive"
 * All details are read from HTTP Post Request
 * GameID, PlayerID is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "root", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("SoPro_db_test")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['gameID']) && isset($_POST['dayORnight'])) {

    $gameID = $_POST['gameID'];
	$dayORnight =$_POST['dayORnight'];

	if($dayORnight == 'day')
	{
		// get the victim of the day
		$victimDor = mysql_query("SELECT victimDor FROM player_game WHERE gameID = '$gameID'")
		or die("Kein Opfer des Tages gefunden");
		
		mysql_query("UPDATE player_game SET alive = 0 WHERE playerID = '$victimDor'")
		or die("Änderung von 'alive' fehlgeschlagen");
		
		// check if alive has been changed

		$result = mysql_query("SELECT alive FROM player_game WHERE playerID = '$victimDor' AND gameID = '$gameID'");
		if ($result == 0) 
		{
			// successfully updated
			$response["success"] = 1;
			$response["message"] = "Player successfully updated.";

			// echoing JSON response
			echo json_encode($response);
		}
	}
	else
	{
		// get the victims of the night
		$victimWer = mysql_query("SELECT victimWer FROM player_game WHERE gameID = '$gameID'")
		or die("Kein Opfer der Werwölfe gefunden");
		
		$victimHex = mysql_query("SELECT victimHex FROM player_game WHERE gameID = '$gameID'")
		or die("Kein Opfer der Hexe gefunden");
		
		// change alive of victimWer to 0
		mysql_query("UPDATE player_game SET alive = 0 WHERE playerID = '$victimWer'")
		or die("Änderung von 'alive' fehlgeschlagen");
		
		// check if alive has been changed
		$result = mysql_query("SELECT alive FROM player_game WHERE playerID = '$victimDor' AND gameID = '$gameID'");
		if ($result == 0) 
		{
			// successfully updated
			$response["success"] = 1;
			$response["message"] = "Player successfully updated.";

			// echoing JSON response
			echo json_encode($response);
		}
		
		if($victimHex != null)
		{
			// change alive of victimHex to 0
			mysql_query("UPDATE player_game SET alive = 0 WHERE playerID = '$victimHex'")
			or die("Änderung von 'alive' fehlgeschlagen");
			
			// check if alive has been changed
			$result = mysql_query("SELECT alive FROM player_game WHERE playerID = '$victimDor' AND gameID = '$gameID'");
			if ($result == 0) 
			{
				// successfully updated
				$response["success"] = 1;
				$response["message"] = "Player successfully updated.";

				// echoing JSON response
				echo json_encode($response);
			}
		}	
	}  
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>