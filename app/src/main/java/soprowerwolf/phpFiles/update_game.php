<?php

/*
 * Following code will update game information
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

    $gameID = $_GET["gameID"];
    $result = 0;

    //if you want to update the lover1
    if (isset($_GET["lover1"])) {
        $lover1 = $_GET["lover1"];
        // mysql update row with matched ID
        $result = mysql_query("UPDATE game SET lover1 = '$lover1' WHERE gameID = '$gameID'");
    }

    //if you want to update the lover2
    if (isset($_GET["lover2"])) {
        $lover2 = $_GET["lover2"];
        // mysql update row with matched ID
        $result = mysql_query("UPDATE game SET lover2 = '$lover2' WHERE gameID = '$gameID'");
    }

    //if you want to update the victimHex
    if (isset($_GET["victimHex"])) {
        $victimHex = $_GET["victimHex"];
        // mysql update row with matched ID
        $result = mysql_query("UPDATE game SET victimHex = '$victimHex' WHERE gameID = '$gameID'");
    }

    //if you want to update the heiltrank
    if (isset($_GET["heiltrank"])) {
        $heiltrank = $_GET["heiltrank"];
        // mysql update row with matched ID
        $result = mysql_query("UPDATE game SET heiltrank = '$heiltrank' WHERE gameID = '$gameID'");
    }

    //if you want to update the victimWer
    if (isset($_GET["victimWer"])) {
        $victimWer = $_GET["victimWer"];
        // mysql update row with matched ID
        $result = mysql_query("UPDATE game SET victimWer = '$victimWer' WHERE gameID = '$gameID'");
    }

    //if you want to update the victimDor
    if (isset($_GET["victimDor"])) {
        $victimDor = $_GET["victimDor"];
        // mysql update row with matched ID
        $result = mysql_query("UPDATE game SET victimDor = '$victimDor' WHERE gameID = '$gameID'");
    }



    // check if row is updated or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Game successfully updated.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Game was not updated";

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
