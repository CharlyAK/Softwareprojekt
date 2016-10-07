<?php

/*
 * Following code will get the number of votes for all players in game
 */

// array for JSON response
$response = array();

// connecting to db
$link = mysql_connect("localhost", "jkloss", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
or die("Auswahl der Datenbank fehlgeschlagen");


$i = 0;
$response["votes"] = array();


    //get all player
    while (!empty($_GET['id'.$i])){
        $playerID = $_GET['id'.$i];

        //a single numOfVotes for one player
        $result = mysql_query("SELECT numOfVotes FROM player_game WHERE playerID = '$playerID'");

        $row = mysql_fetch_array($result);
        $numOfVotes = array();
        $numOfVotes["votes"] = $row["numOfVotes"];

        array_push($response["votes"], $numOfVotes);

        $i++;
    }
    // success
    $response["success"] = 1;
    $response["message"] = "Got votes";


// echoing JSON response
echo json_encode($response);

?>

