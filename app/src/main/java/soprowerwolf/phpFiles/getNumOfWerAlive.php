  GNU nano 1.3.12                                   Datei: getNumOfWerAlive.php                                                                              

<?php
/*
 * Following code get the number of alive werewolves
 */

// array for JSON response
$response = array();

// connecting to db
$link = mysql_connect("localhost", "jkloss", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
or die("Auswahl der Datenbank fehlgeschlagen");

// check for required fields
if (isset($_GET['gameID'])) {

    $gameID = $_GET['gameID'];
    $response["numOfWerAlive"] = mysql_num_rows(mysql_query("SELECT * FROM `player_game` WHERE `role` = 'Werwolf' AND `alive` = 1 AND `playerID` != 0"));

    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
}

else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}

?>


