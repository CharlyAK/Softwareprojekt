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

	$name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
// check for required fields
  if (!empty($name) && isset($name) && !empty($email) && isset($email) && !empty($password) && isset ($password))
  { 
	$i = 1;
	$insert = 0;
	$size = mysql_query("SELECT * FROM _PLAYER");
	$DatabaseSize = mysql_num_rows($size);
	while($i <= $DatabaseSize+1 && $insert == 0)
	{
		$currentRow = mysql_query("SELECT count(*) FROM _PLAYER WHERE playerID = '$i'");
		if(mysql_result($currentRow, 0) == 1)
		{
			$i++;
		}
		else
		{
			//set new player in free row
			mysql_query("INSERT INTO _PLAYER VALUES ('$i', '$name', '$email', '$password')")
			or die("Spieler in Datenbank einfügen fehlgeschlagen");
			
			mysql_query("SELECT playerID, name, email, password FROM _PLAYER ORDER BY playerID DESC")
			or die("Sortieren der Tabelle fehlgeschlagen");
			
			$insert = 1;
			$response["ID"] = '$i';
		}
	}
  
    // check if player has been inserted 
	
    $result = mysql_query("SELECT email FROM _player WHERE name = '$name' AND password = '$password'");
    if ($result == $email) 
	{
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Player successfully inserted.";

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