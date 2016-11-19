<?php

/*
 *  Deletes a game and the corresponding players
 *  @param gameID
 */

$link = mysql_connect("localhost", "jkloss", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
or die("Auswahl der Datenbank fehlgeschlagen");

// array for JSON response
$response = array();


//----------------
if (isset($_GET['gameID'])) {

    $gameID = $_GET["gameID"];

    //delete the players
    $result1 = mysql_query("DELETE FROM player WHERE gameID = '$gameID'")
        or die("Unable to delete player");
    //delete the game
    $result2 = mysql_query("DELETE FROM game WHERE gameID = '$gameID'")
        or die("Unable to delete game");

    if($result1 && $result2){
        $response["success"] = 1;
        $response["message"] = "Game successfully deleted.";
    } else{
        $response["success"] = 0;
        $response["message"] = "Game was not deleted.";
    }

    // echoing JSON response
    echo json_encode($response);

}else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>
