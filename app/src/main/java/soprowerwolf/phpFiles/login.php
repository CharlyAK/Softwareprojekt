<?php
 
/*
 * Following code will get the playerID of on player
 * A player is identified by his/her combination of email and password
 */
 
// array for JSON response
$response = array();

// connecting to db
$link = mysql_connect("localhost", "root", "werwolf")
or die("Keine Verbindung möglich!");

mysql_select_db("SoPro_db_test")
or die("Auswahl der Datenbank fehlgeschlagen");

if(!empty($_GET['email']) && isset($_GET['email']) && !empty($_GET['password']) && isset ($_GET['password']))
	{
		$email = $_GET['email'];
		$password = $_GET['password'];
		
		$result = mysql_query("SELECT playerID FROM _player WHERE email = '$email' AND password = '$password'");
		
		if (!empty($result)) 
		{
			// check for empty result
			if (mysql_num_rows($result) > 0) 
			{
				$result = mysql_fetch_array($result);
 
				$playerID = array();
				$playerID["playerID"] = $result["playerID"];
	
				// success
				$response["success"] = 1;
 
				// user node
				$response["playerID"] = array();
 
				array_push($response["playerID"], $playerID);
 
				// echoing JSON response
				echo json_encode($response);
			} else 
				{
					// no player found
					$response["success"] = 0;
					$response["message"] = "No player found";
 
					// echo no users JSON
					echo json_encode($response);
				}
		} else 
			{
				// no player found
				$response["success"] = 0;
				$response["message"] = "No player found";
 
				// echo no users JSON
				echo json_encode($response);
			}
	}
 else 
 {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>