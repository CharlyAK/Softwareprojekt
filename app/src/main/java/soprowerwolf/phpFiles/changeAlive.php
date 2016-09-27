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
if (isset($_POST['gameID'])) {

    $gameID = $_POST['gameID'];


	// get the victim of the day
	$victimDor = mysql_query("SELECT victimDor FROM player_game WHERE gameID = '$gameID'")
	or die("Kein Opfer des Tages gefunden");

	// get the Lover of the Victim
	$loverDor = mysql_query("SELECT lover FROM player_game WHERE player_ID = '$victimDor'") or die("Opfer hat keinen Lover");

	if($victimDor != 0) {
    	// change alive of victimDor to 0
    	mysql_query("UPDATE player_game SET alive = 0 WHERE playerID = '$victimDor'")
    	or die("Änderung von 'alive' fehlgeschlagen");

    	// check if alive has been changed
    	$result = mysql_query("SELECT alive FROM player_game WHERE playerID = '$victimDor' AND gameID = '$gameID'");
    	if ($result == 0) {
    		// successfully updated
    		$response["success"] = 1;
    		$response["message"] = "Player successfully updated.";

    		// echoing JSON response
    		echo json_encode($response);
    	}

    	if($loverDor != 0){
    	   // change alive of loverDor to 0
           mysql_query("UPDATE player_game SET alive = 0 WHERE playerID = '$loverDor'")
           or die("Änderung von 'alive' fehlgeschlagen");

           // check if alive has been changed
           $result = mysql_query("SELECT alive FROM player_game WHERE playerID = '$loverDor' AND gameID = '$gameID'");
           if ($result == 0) {
            // successfully updated
            $response["success"] = 1;
            $response["message"] = "Player successfully updated.";

            // echoing JSON response
            echo json_encode($response);
           }
    	}
    }


	// get the victim of the Werewolfes
    	$victimWer = mysql_query("SELECT victimWer FROM player_game WHERE gameID = '$gameID'")
    	or die("Kein Opfer der Werwölfe gefunden");

    	// get the Lover of the Victim
        	$loverWer = mysql_query("SELECT lover FROM player_game WHERE player_ID = '$victimWer'") or die("Opfer hat keinen Lover");

    	if($victimWer!= 0) {
        	// change alive of victimWer to 0
        	mysql_query("UPDATE player_game SET alive = 0 WHERE playerID = '$victimWer'")
        	or die("Änderung von 'alive' fehlgeschlagen");

        	// check if alive has been changed
        	$result = mysql_query("SELECT alive FROM player_game WHERE playerID = '$victimWer' AND gameID = '$gameID'");
        	if ($result == 0) {
        		// successfully updated
        		$response["success"] = 1;
        		$response["message"] = "Player successfully updated.";

        		// echoing JSON response
        		echo json_encode($response);
        	}

        	if($loverWer != 0){
                	   // change alive of loverWer to 0
                       mysql_query("UPDATE player_game SET alive = 0 WHERE playerID = '$loverDor'")
                       or die("Änderung von 'alive' fehlgeschlagen");

                       // check if alive has been changed
                       $result = mysql_query("SELECT alive FROM player_game WHERE playerID = '$loverWer' AND gameID = '$gameID'");
                       if ($result == 0) {
                        // successfully updated
                        $response["success"] = 1;
                        $response["message"] = "Player successfully updated.";

                        // echoing JSON response
                        echo json_encode($response);
                       }
                	}
        }


	// get the victim of the Witch
        	$victimHex = mysql_query("SELECT victimHex FROM player_game WHERE gameID = '$gameID'")
        	or die("Kein Opfer der Hexe gefunden");

        	// get the Lover of the Victim
            	$loverHex = mysql_query("SELECT lover FROM player_game WHERE player_ID = '$victimHex'") or die("Opfer hat keinen Lover");

        	if($victimHex!= 0) {
            	// change alive of victimHex to 0
            	mysql_query("UPDATE player_game SET alive = 0 WHERE playerID = '$victimHex'")
            	or die("Änderung von 'alive' fehlgeschlagen");

            	// check if alive has been changed
            	$result = mysql_query("SELECT alive FROM player_game WHERE playerID = '$victimHex' AND gameID = '$gameID'");
            	if ($result == 0) {
            		// successfully updated
            		$response["success"] = 1;
            		$response["message"] = "Player successfully updated.";

            		// echoing JSON response
            		echo json_encode($response);
            	}

            	if($loverHex != 0){
                    	   // change alive of loverDor to 0
                           mysql_query("UPDATE player_game SET alive = 0 WHERE playerID = '$loverHex'")
                           or die("Änderung von 'alive' fehlgeschlagen");

                           // check if alive has been changed
                           $result = mysql_query("SELECT alive FROM player_game WHERE playerID = '$loverHex' AND gameID = '$gameID'");
                           if ($result == 0) {
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
