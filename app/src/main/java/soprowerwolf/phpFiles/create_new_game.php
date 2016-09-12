<?php

/*
 * Following code insert a new player to the database
 * All details are read from HTTP Post Request
 * name, email and password is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "root", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("SoPro_db_test")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

	
// 1. create new gameID and insert gameID into _game and gameID and the first playerID with role of the game into player_game
  if (!empty($_POST['playerID']) && isset($_POST['playerID']) && !empty($_POST['role']) && isset($_POST['role']))
  { 
	$player = $_POST['playerID'];
	$role = $_POST['role'];
	
	$i = 1;
	$insert = 0;
	$size = mysql_query("SELECT * FROM _game");
	$DatabaseSize = mysql_num_rows($size);
	while($i <= $DatabaseSize+1 && $insert == 0)
	{
		$currentRow = mysql_query("SELECT count(*) FROM _game WHERE gameID = '$i'");
		if(mysql_result($currentRow, 0) == 1)
		{
			$i++;
		}
		else
		{
			//set new game in free row
			mysql_query("INSERT INTO _game (gameID) VALUES ('$i')")
			or die("Spiel in Datenbank einfügen fehlgeschlagen");
			
			mysql_query("SELECT gameID FROM _game ORDER BY gameID DESC")
			or die("Sortieren der _game - Tabelle fehlgeschlagen");
			
			mysql_query("INSERT INTO player_game (gameID, playerID, role) VALUES ('$i', '$player', '$role')")
			or die("Spieler und Spiel in player_game hinzufügen fehlgeschlagen");
			
			mysql_query("SELECT gameID FROM player_game ORDER BY gameID DESC")
			or die("Sortieren der player_game - Tabelle fehlgeschlagen");
			
			$insert = 1;
			$response["ID"] = '$i';
			
			// check if game has been inserted 
	
			$resultGame = mysql_query("SELECT gameID FROM _game WHERE gameID = '$i'");
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
 // 3. insert the Roles into player_game
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
				$response["message"] = "Role successfully inserted.";

				// echoing JSON response
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