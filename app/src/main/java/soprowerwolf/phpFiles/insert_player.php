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

// 1. insert player into player_game
  if (!empty($_POST['gameID']) && isset($_POST['gameID']) && !empty($_POST['playerID']) && isset($_POST['playerID']))
  {
		$gameID = $_POST['gameID'];
		$playerID = $_POST['playerID'];
		
		mysql_query("UPDATE player_game SET playerID = '$playerID' WHERE gameID='$gameID' AND playerID = 0 LIMIT 1")
		or die("Einfügen des Spielers fehlgeschlagen");
		
		// check if player has been inserted 
	
			$resultGame = mysql_query("SELECT playerID FROM player_game WHERE gameID = '$gameID'");
			if ($result == $playerID) 
			{
				// successfully updated
				$response["success"] = 1;
				$response["message"] = "Player successfully inserted.";

				// echoing JSON response
				echo json_encode($response); 
			}
  }
  
  else if(!empty($_GET['gameID']) && isset($_GET['gameID']) && !empty($_GET['playerID']) && isset($_GET['playerID']))
  {
		$gameID = $_GET['gameID'];
		$playerID = $_GET['playerID'];
		
		$result = mysql_query("SELECT role FROM player_game WHERE playerID = '$playerID' AND gameID = '$gameID'");
		
		if (!empty($result)) {
			// check for empty result
			if (mysql_num_rows($result) > 0) {
 
				$result = mysql_fetch_array($result);
 
				$role = array();
				$role["role"] = $result["role"];
		
				// success
				$response["success"] = 1;
 
				// user node
				$response["role"] = array();
 
				array_push($response["role"], $role);
 
				// echoing JSON response
				echo json_encode($response);
        } else {
            // no player found
            $response["success"] = 0;
            $response["message"] = "No player found";
 
            // echo no users JSON
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