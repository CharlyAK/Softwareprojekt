<?php

/*
 * Following code insert a new player to the database
 * All details are read from HTTP Post Request
 * name, email and password is needed
 */

// Verbindung aufbauen, auswählen einer Datenbank
$link = mysql_connect("localhost", "root", "werwolf")
    or die("Keine Verbindung möglich!");

mysql_select_db("SoPro_db_test")
    or die("Auswahl der Datenbank fehlgeschlagen");


// array for JSON response
$response = array();
if (isset($_GET["game"])) 
{

    // get all games from game table
    $result = mysql_query("SELECT * FROM _game") or die(mysql_error());

    // check for empty result
    if (mysql_num_rows($result) > 0) 
	{
        // looping through all results
        $response["games"] = array();

        while ($row = mysql_fetch_array($result)) 
		{
            // temp user array
            $games = array();
            $games["gameID"] = $row["gameID"];

            // push single game into final response array
            array_push($response["games"], $games);
        }
        // success
        $response["success"] = 1;

        // echoing JSON response
        echo json_encode($response);

	}
}
    
// check for required fields
 else if (!empty($_POST['gameID']) && isset($_POST['gameID']))
  { 
	$gameID = $_POST['gameID'];
	//set new game in free row
			mysql_query("INSERT INTO _game VALUES ('$gameID')")
			or die("Spiel in Datenbank einfügen fehlgeschlagen");
			
			mysql_query("SELECT * FROM _game ORDER BY playerID DESC")
			or die("Sortieren der Tabelle fehlgeschlagen");
	
  
    // check if game has been inserted 
	
    $result = mysql_query("SELECT gameID FROM _game WHERE gameID = '$gameID'");
    if ($result == $gameID) 
	{
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Game successfully inserted.";

        // echoing JSON response
        echo json_encode($response); 
    }
   
 }else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>