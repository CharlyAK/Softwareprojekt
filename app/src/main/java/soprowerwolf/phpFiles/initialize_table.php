<?php

//needs number of players and roles

/* 
//in Java use
// nameValuePairs.add(new BasicNameValuePair("devices[]", device1)); nameValuePairs.add(new BasicNameValuePair("devices[]", device2));
*/

$link = mysql_connect("localhost", "jkloss", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("jkloss_db")
    or die("Auswahl der Datenbank fehlgeschlagen");

// array for JSON response
$response = array();


//----------------
if (isset($_POST['roles[]'])) {

    //$roles = array();
    //$roles = $_POST['roles[]'];
    $roles[0] = "Hexe";
    //$roles[1] = "Werwolf";



	//get the maximum gameID existing
	$result = mysql_query("SELECT MAX(gameID) from game")
	or die("Max value nicht erreichbar");
	$max = mysql_fetch_array($result, MYSQL_NUM);
	//new gameID
	$gameID = $max[0] + 1;

    //create new game
	mysql_query("INSERT INTO game VALUES ('$gameID',NULL ,NULL ,NULL , 1 ,NULL ,NULL )")
	or die("Erstellen des Spiels mit neuer gameID nicht moeglich");

    //insert players into new game
	for ($i = 0; $i < sizeof($roles); $i++) {
		mysql_query("INSERT INTO player VALUES ('$i','$gameID', NULL ,'$roles[$i]','1')")
		or die("Einfuegen der Spieler in das neue Spiel nicht moeglich");
	}

    $response["success"] = 1;
    $response["message"] = "Game successfully created.";

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
