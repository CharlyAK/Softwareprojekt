                                                            
<?php
/*
 * Following code will update the number of votes for all players in game
 */

// array for JSON response
$response = array();

// connecting to db
$link = mysql_connect("localhost", "jkloss", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
or die("Auswahl der Datenbank fehlgeschlagen");

// check for required fields
if (isset($_POST['playerID'])) {

    $choice = $_POST['playerID'];
    mysql_query("UPDATE `player_game` SET `numOfVotes`=`numOfVotes`+1 WHERE `playerID`= $choice");

        // successfully updated
        $response["success"] = 1;
        $response["message"] = "numOfVotes successfully updated.";
    }


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

