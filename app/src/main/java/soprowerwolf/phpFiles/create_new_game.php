<?php

/*
 * Following code insert a new player to the database
 * All details are read from HTTP Post Request
 * name, email and password is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

	
// 1. create new gameID and insert gameID into _GAME and gameID and the first playerID with role of the game into player_game
  if (!empty($_POST['playerID']) && isset($_POST['playerID']) && !empty($_POST['role']) && isset($_POST['role']))
  { 
	$player = $_POST['playerID'];
	$role = $_POST['role'];
	
	$i = 1;
	$insert = 0;
	$size = mysql_query("SELECT * FROM _GAME");
	$DatabaseSize = mysql_num_rows($size);
	while($i <= $DatabaseSize+1 && $insert == 0)
	{
		$currentRow = mysql_query("SELECT count(*) FROM _GAME WHERE gameID = '$i'");
		if(mysql_result($currentRow, 0) == 1)
		{
			$i++;
		}
		else
		{
			//set new game in free row
			mysql_query("INSERT INTO _GAME (gameID) VALUES ('$i')")
			or die("Spiel in Datenbank einfügen fehlgeschlagen");
			
			mysql_query("SELECT gameID FROM _GAME ORDER BY gameID DESC")
			or die("Sortieren der _game - Tabelle fehlgeschlagen");
			
			mysql_query("INSERT INTO player_game (gameID, playerID, role) VALUES ('$i', '$player', '$role')")
			or die("Spieler und Spiel in player_game hinzufügen fehlgeschlagen");
			
			mysql_query("SELECT gameID FROM player_game ORDER BY gameID DESC")
			or die("Sortieren der player_game - Tabelle fehlgeschlagen");
			
			$insert = 1;
			$response["ID"] = '$i';
			
			// check if game has been inserted 
	
			$resultGame = mysql_query("SELECT gameID FROM _GAME WHERE gameID = '$i'");
			$resultPlayer_Game = mysql_query("SELECT gameID FROM player_game WHERE playerID = '$player'");
			if ($resultGame == $resultPlayer_Game) 
			{
				// successfully updated
				$response["success"] = 1;
				$response["message"] = "Game successfully inserted.";

				// echoing JSON response
				echo json_encode($response); 
			}
		}
	}   
 }
 // 2. GET reqest to get the created gameID 
 else if (!empty($_GET['playerID']) && isset($_GET['playerID']))
 {
	$player = $_GET['playerID'];
	// get new gameID by playerID
	$result = mysql_query("SELECT gameID FROM player_game WHERE playerID = '$player'");
	
	if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $game = array();
            $game["gameID"] = $result["gameID"];
		
            // success
            $response["success"] = 1;
 
            // user node
            $response["gameID"] = array();
 
            array_push($response["gameID"], $game);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no game found
            $response["success"] = 0;
            $response["message"] = "No game found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    }
 }
 // 3. insert the Roles into player_game AND insert phases and nextPhase into Phases
 else  if (!empty($_POST['role']) && isset($_POST['role']) && !empty($_POST['gameID']) && isset($_POST['gameID']))
 {
	$role = $_POST['role'];
	$gameID = $_POST['gameID'];
	
	mysql_query("INSERT INTO player_game (gameID, role) VALUES ('$gameID','$role')")
	or die("Einfügen der Rolle '$role' fehlgeschlagen");
	
	// check if role has been inserted 
	
			$result = mysql_query("SELECT role FROM player_game WHERE gameID = '$gameID' AND role = '$role'");
			if ($result = $role) 
			{
				// successfully updated
				$response["success"] = 1;
				$response["message"] = "Role '$role' successfully inserted.";

				// echoing JSON response
				echo json_encode($response); 
			}
 }
 // 4. insert phases and nextPhase into Phases
 else if(!empty($_POST['gameID']) && isset($_POST['gameID']) && !empty($_POST['phase']) && isset($_POST['phase']) && !empty($_POST['nextPhase']) && isset($_POST['nextPhase']))
 {
	$phase = $_POST['phase'];
	$nextPhase = $_POST['nextPhase'];
	$gameID = $_POST['gameID'];
	
	mysql_query("INSERT INTO _PHASES (gameID, phases, nextPhase) VALUES ('$gameID','$phase', '$nextPhase')")
	or die("Einfügen der Phase '$phase' fehlgeschlagen");
	
	// set first Phase
	if(!empty($_POST['firstPhase']) && isset($_POST['firstPhase']))
	{
		mysql_query("UPDATE _PHASES SET currentPhase = 1 WHERE gameID = $gameID")
		or die("Setzen der ersten Phase fehlgeschlagen");
	}
	
	// check if phase has been inserted 
	
			$result = mysql_query("SELECT phases FROM _PHASES WHERE gameID = '$gameID' AND nextPhase = '$nextPhase'");
			if ($result = $phase) 
			{
				// successfully updated
				$response["success"] = 1;
				$response["message"] = "Phase '$phase' successfully inserted.";

				// echoing JSON response
				echo json_encode($response); 
			}
			
	 // 5. insert Phases which belong to every Game
	if($nextPhase == 'OpferNacht')
	{
		mysql_query("INSERT INTO _PHASES (gameID, phases, nextPhase) VALUES ('$gameID','OpferNacht', 'Tag')")
		or die("Einfügen der Phase 'OpferNacht' fehlgeschlagen");

		// check if phase has been inserted 
	
			$result = mysql_query("SELECT phases FROM _PHASES WHERE gameID = '$gameID' AND nextPhase = 'Tag'");
			if ($result = 'OpferNacht') 
			{
				// successfully updated
				$response["success"] = 1;
				$response["message"] = "Phase 'OpferNacht' successfully inserted.";

				// echoing JSON response
				echo json_encode($response); 
			}
			
		mysql_query("INSERT INTO _PHASES (gameID, phases, nextPhase) VALUES ('$gameID','Tag', 'OpferTag')")
		or die("Einfügen der Phase 'Tag' fehlgeschlagen");

		// check if phase has been inserted 
	
			$result = mysql_query("SELECT phases FROM _PHASES WHERE gameID = '$gameID' AND nextPhase = 'OpferTag'");
			if ($result = 'Tag') 
			{
				// successfully updated
				$response["success"] = 1;
				$response["message"] = "Phase 'Tag' successfully inserted.";

				// echoing JSON response
				echo json_encode($response); 
			}
	
		mysql_query("INSERT INTO _PHASES (gameID, phases, nextPhase) VALUES ('$gameID','OpferTag', 'Werwolf')")
		or die("Einfügen der Phase 'OpferTag' fehlgeschlagen");

		// check if phase has been inserted 
	
			$result = mysql_query("SELECT phases FROM _PHASES WHERE gameID = '$gameID' AND nextPhase = 'Werwolf'");
			if ($result = 'OpferTag') 
			{
				// successfully updated
				$response["success"] = 1;
				$response["message"] = "Phase 'OpferTag' successfully inserted.";

				// echoing JSON response
				echo json_encode($response); 
			}
			
		mysql_query("INSERT INTO _PHASES (gameID, phases, nextPhase) VALUES ('$gameID','Spielende', 'Spielende')")
		or die("Einfügen der Phase 'Spielende' fehlgeschlagen");

		// check if phase has been inserted 
	
			$result = mysql_query("SELECT phases FROM _PHASES WHERE gameID = '$gameID' AND nextPhase = 'Spielende'");
			if ($result = 'Spielende') 
			{
				// successfully updated
				$response["success"] = 1;
				$response["message"] = "Phase 'Spielende' successfully inserted.";

				// echoing JSON response
				echo json_encode($response); 
			}
			
		mysql_query("INSERT INTO _PHASES (gameID, phases, nextPhase) VALUES ('$gameID','Jaeger', 'Jaeger')")
		or die("Einfügen der Phase 'Jaeger' fehlgeschlagen");

		// check if phase has been inserted 
	
			$result = mysql_query("SELECT phases FROM _PHASES WHERE gameID = '$gameID' AND nextPhase = 'Jaeger'");
			if ($result = 'Jaeger') 
			{
				// successfully updated
				$response["success"] = 1;
				$response["message"] = "Phase 'Jaeger' successfully inserted.";

				// echoing JSON response
				echo json_encode($response); 
			}	
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