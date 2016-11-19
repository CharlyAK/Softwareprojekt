<?php
/**
 * This file saves a image to the database
 */

$link = mysql_connect("localhost", "jkloss", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();

if (isset($_POST['playerID']) && !empty($_POST['playerID']) && isset($_POST['image']) && !empty($_POST['image'])){

    $playerID = $_POST['playerID'];
    $image = $_POST['image'];

    mysql_query("UPDATE `_PLAYER` SET `image`= '$image' WHERE `playerID`= '$playerID'");

    // successfully updated
    $response["success"] = 1;
    $response["message"] = "Image was successfully updated.";

    // echoing JSON response
    echo json_encode($response);
}

else if (isset($_GET['playerID']) && !empty($_GET['playerID'])){
    
	 $playerID = $_GET['playerID'];

    $response["image"] = mysql_fetch_array(mysql_query("SELECT `image` FROM `_PLAYER` WHERE `playerID` = '$playerID'"));


    // successfully downloaded
    $response["success"] = 1;
    $response["message"] = "Image was downloaded.";

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
