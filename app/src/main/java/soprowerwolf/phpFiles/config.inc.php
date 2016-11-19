<?php
// use here a value of your choice (maximum 46 characters):
$cfg['blowfish_secret'] = 'a8b7c6d';    

$i = 0;
$i++;


// 'cookie' (or 'http' , especially if mcrypt could not be loaded,
// but cookie giving more meaningful error messages in the testing phase)
// older PHP versions had some problems with "cookie" on 64 bit boxes though
$cfg['Servers'][$i]['auth_type'] = 'cookie';

// PHP 5 support for new MySQL 4.1.3+ features:
$cfg['Servers'][$i]['extension'] = 'mysqli';

$cfg['Servers'][$i]['host']	= 'localhost';

$cfg['Servers'][$i]['connect_type']	= 'tcp';

$cfg['Servers'][$i]['compress']	= false;	// Use compressed protocol for the MySQL connection
$cfg['Servers'][$i]['user']	= 'jkloss';	// MySQL user
$cfg['Servers'][$i]['only_db']	= 'jkloss_db';	// If set to a db-name, only
?>
