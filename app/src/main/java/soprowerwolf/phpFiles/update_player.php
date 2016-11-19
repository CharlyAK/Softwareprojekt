<?php
 
/*
 * Following code will update player information
 * A player is identified by his/her ID and the gameID
 */
 
// array for JSON response
$response = array();

// connecting to db
$link = mysql_connect("localhost", "jkloss", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
or die("Auswahl der Datenbank fehlgeschlagen");

 
// check for required fields
if (isset($_GET['gameID']) && isset($_GET['playerID'])) {

    $playerID = $_GET["playerID"];
    $gameID = $_GET["gameID"];
    $result = 0;

    //if you want to update the role
    if (isset($_GET["role"])) {
        $role = $_GET["role"];
        // mysql update row with matched IDs
        $result = mysql_query("UPDATE player SET role = '$role' WHERE playerID = '$playerID' AND gameID = '$gameID'");
    }

    //if you want to update the alive status
    if (isset($_GET["alive"])) {
        $alive = $_GET["alive"];
        // mysql update row with matched IDs
        $result = mysql_query("UPDATE player SET alive = '$alive' WHERE playerID = '$playerID' AND gameID = '$gameID'");
    }


    // check if row is updated or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Player successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Player was not updated";

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
