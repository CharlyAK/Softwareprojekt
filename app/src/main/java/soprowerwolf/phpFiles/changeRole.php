<?php

/*
 * Following code will change the Role of the former "Dieb"
 * All details are read from HTTP Post Request
 * GameID, PlayerID and newRole is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

	$gameID = $_POST['gameID'];
    $playerID = $_POST['playerID'];
    $newRole = $_POST['newRole'];
	$notChoosen = $_POST['notChoosen'];
	$nothingChoosen = $_POST['nothingChoosen'];
// check for required fields
  if (!empty($gameID) && isset($gameID) && !empty($playerID) && isset($playerID) && !empty($newRole) && isset ($newRole) && !empty($notChoosen) && isset ($notChoosen))
  {
    // set new Role
    mysql_query("UPDATE player_game SET role='$newRole' WHERE playerID = '$playerID' AND gameID = '$gameID'")
	or die("Die Änderung der Rolle ist fehlgeschlagen");

	// check if role has been changed

    $result = mysql_query("SELECT role FROM player_game WHERE playerID = '$playerID' AND gameID = 'gameID'");
    if ($result == $newRole)
	{
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Player successfully updated.";

        // echoing JSON response
        echo json_encode($response);
    }

	if (!empty($nothingChoosen) && isset ($nothingChoosen) && $nothingChoosen != 'Werwolf')
	{
	    //if Amor isn't choosen -> delete Lover-Phase, too
	    if(&nothingChoosen == 'Amor' || &notChoosen == 'Amor')
	    {
	        mysql_query("UPDATE _PHASES p1,
            						(SELECT(sp.nextPhase) AS newNextPhase FROM _PHASES sp WHERE gameID = '$gameID' AND phases = 'Lover') AS p2
            					SET p1.nextPhase = p2.newNextPhase WHERE gameID = '$gameID' AND p1.nextPhase = 'Amor'");

            mysql_query("DELETE FROM _PHASES WHERE gameID = '$gameID' AND phases = 'Amor'");
            mysql_query("DELETE FROM _PHASES WHERE gameID = '$gameID' AND phases = 'Lover'");
	    }

		//delete both phase which are not choosen --> nextPhase of "notChoosen" and "nothingChoosen" become nextPhase of another Phase
		mysql_query("UPDATE _PHASES p1,
						(SELECT(sp.nextPhase) AS newNextPhase FROM _PHASES sp WHERE gameID = '$gameID' AND phases = '$nothingChoosen') AS p2
					SET p1.nextPhase = p2.newNextPhase WHERE gameID = '$gameID' AND p1.nextPhase = '$nothingChoosen'");
		mysql_query("UPDATE _PHASES p1,
						(SELECT(sp.nextPhase) AS newNextPhase FROM _PHASES sp WHERE gameID = '$gameID' AND phases = '$notChoosen') AS p2
					SET p1.nextPhase = p2.newNextPhase WHERE gameID = '$gameID' AND p1.nextPhase = '$notChoosen'");
		mysql_query("DELETE FROM _PHASES WHERE gameID = '$gameID' AND phases = '$nothingChoosen'");
		mysql_query("DELETE FROM _PHASES WHERE gameID = '$gameID' AND phases = '$notChoosen'");

		//check if phase has been deleted
		$result1 = mysql_query("SELECT phases FROM _PHASES WHERE phases = '$nothingChoosen' AND gameID = '$gameID'");
		$result2 = mysql_query("SELECT phases FROM _PHASES WHERE phases = '$notChoosen' AND gameID = '$gameID'");
		if (empty($result1) && empty($result2))
		{
			// successfully deleted
			$response["success"] = 1;
			$response["message"] = "Phase '$notChoosen' and '$nothingChoosen'successfully deleted.";

			// echoing JSON response
			echo json_encode($response);
		}
	}
	else
	{
	    //if Amor isn't choosen -> delete Lover-Phase, too
        if(&nothingChoosen == 'Amor' || &notChoosen == 'Amor')
        {
            mysql_query("UPDATE _PHASES p1,
                (SELECT(sp.nextPhase) AS newNextPhase FROM _PHASES sp WHERE gameID = '$gameID' AND phases = 'Lover') AS p2
                    SET p1.nextPhase = p2.newNextPhase WHERE gameID = '$gameID' AND p1.nextPhase = 'Amor'");

            mysql_query("DELETE FROM _PHASES WHERE gameID = '$gameID' AND phases = 'Amor'");
            mysql_query("DELETE FROM _PHASES WHERE gameID = '$gameID' AND phases = 'Lover'");
        }

		//delete phase which is not choosen
		mysql_query("UPDATE _PHASES p1,
						(SELECT(sp.nextPhase) AS newNextPhase FROM _PHASES sp WHERE gameID = '$gameID' AND phases = '$notChoosen') AS p2
					SET p1.nextPhase = p2.newNextPhase WHERE gameID = '$gameID' AND p1.nextPhase = '$notChoosen'");
		mysql_query("DELETE FROM _PHASES WHERE gameID = '$gameID' AND phases = '$notChoosen'");

		//check if phase has been deleted
		$result = mysql_query("SELECT phases FROM _PHASES WHERE phases = '$notChoosen' AND gameID = '$gameID'");
		if (empty($result))
		{
			// successfully deleted
			$response["success"] = 1;
			$response["message"] = "Phase '$notChoosen'successfully deleted.";

			// echoing JSON response
			echo json_encode($response);
		}
	}

	// delete the two Roles for Dieb in player_game
	mysql_query("DELETE FROM player_game WHERE gameID = '$gameID' AND playerID = '0'");
 }
 else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing: changeRole";

    // echoing JSON response
    echo json_encode($response);
}
?>