<?php

/*
 * Following code will change settings for the "Hexe" (victimHex, heiltrank)
 * All details are read from HTTP Post Request
 * GameID, victimHex/ heiltrank is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

// check for required fields
if(isset($_POST['poison']) && !empty($_POST['poison']) && isset($_POST['gameID']) && !empty($_POST['gameID']) && isset($_POST['victimHex']) && !empty($_POST['victimHex']))
{
	$gameID = $_POST['gameID'];
	$victimHex = $_POST['victimHex'];
	
	// change poison 
    mysql_query("UPDATE _GAME SET poison=1 WHERE gameID = '$gameID'")
	or die("Die Änderung des Gifttranks ist fehlgeschlagen");
	
	// set victimHex
    mysql_query("UPDATE _GAME SET victimHex='$victimHex' WHERE gameID = '$gameID'")
	or die("Die Änderung des Hexenopfers ist fehlgeschlagen");
	
	// check if poison has been changed
	$resultP = mysql_query("SELECT poison FROM _GAME WHERE gameID = '$gameID'");
	// check if victimHex has been changed
    $resultVH = mysql_query("SELECT victimHex FROM _GAME WHERE gameID = '$gameID'");
	
    if ($resultP == 1 && $resultVH == $victimHex) 
	{
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "VictimHex and poison successfully updated.";

        // echoing JSON response
        echo json_encode($response);
	}
}

else if (isset($_POST['gameID']) && !empty($_POST['gameID']) && isset($_POST['heal']) && !empty($_POST['heal'])) {

        $gameID = $_POST['gameID'];

        // set heal
        mysql_query("UPDATE _GAME SET heal=1 WHERE gameID = '$gameID'")
    	or die("Die Änderung des Heiltranks ist fehlgeschlagen");
		
		// change victimWer
        mysql_query("UPDATE _GAME SET victimWer=null WHERE gameID = '$gameID'")
    	or die("Die Rettung des Werwolfopfers ist fehlgeschlagen");

        // check if heiltrank and victimWer has been changed
        $resultH = mysql_query("SELECT heal FROM _GAME WHERE gameID = '$gameID'");
		$resultVW = mysql_query("SELECT victimWer FROM _GAME WHERE gameID = '$gameID'");
        if ($resultH == 1 && $resultVW == null) {
            // successfully updated
            $response["success"] = 1;
            $response["message"] = "heal and victimWer successfully updated.";

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