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
if (isset($_GET["playerID"]) && isset($_GET["gameID"])) {

    $playerID = $_GET['playerID'];
    $gameID = $_GET['gameID'];
 
    // get a player_game from player_game table
    $result = mysql_query("SELECT * FROM player_game WHERE playerID = '$playerID' AND gameID = '$gameID'");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $player_game = array();
			$player_game["gameID"] = $result["gameID"];
            $player_game["playerID"] = $result["playerID"];
			$player_game["role"] = $result["role"];
            $player_game["alive"] = $result["alive"];
			$player_game["lover"] = $result["lover"];
			$player_game["currentlySelectedVictimWer"] = $result["currentlySelectedVictimWer"];
			$player_game["currentlySelectedVictimDor"] = $result["currentlySelectedVictimDor"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["player_game"] = array();
 
            array_push($response["player_game"], $player_game);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no player found
            $response["success"] = 0;
            $response["message"] = "No player found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no player found
        $response["success"] = 0;
        $response["message"] = "No player found";
 
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
