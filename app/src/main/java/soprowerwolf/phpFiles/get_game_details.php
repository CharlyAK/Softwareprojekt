<?php
 
/*
 * Following code will get single player details
 * A player is identified by his/her id and the gameID
 */
 
// array for JSON response
$response = array();

// connecting to db
$link = mysql_connect("localhost", "jkloss", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
or die("Auswahl der Datenbank fehlgeschlagen");


// check for post data
if (isset($_GET["gameID"])) {

    $gameID = $_GET['gameID'];
 
    // get a game from game table
    $result = mysql_query("SELECT * FROM _GAME WHERE gameID = '$gameID'");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $game = array();
			$game["gameID"] = $result["gameID"];
			$game["start"] = $result["start"];
            $game["poison"] = $result["poison"];
			$game["heal"] = $result["heal"];
			$game["victimDor"] = $result["victimDor"];
			$game["victimWer"] = $result["victimWer"];
			$game["victimHex"] = $result["victimHex"];
			$game["victimJaeger"] = $result["victimJaeger"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["game"] = array();
 
            array_push($response["game"], $game);
 
            // echoing JSON response
            echo json_encode($response);
        } 
    } else {
        // no player found
        $response["success"] = 0;
        $response["message"] = "No game found";
 
        // echo no users JSON
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
