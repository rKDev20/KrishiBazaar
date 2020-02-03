<?php
define('USER_EXISTS', 1);
define('USER_NEW', 0);
$json = file_get_contents('php://input');
$params = json_decode($json,true);
$params["mobile"]);
$params["otp"]);


$result=array(
	'status' => constant,
	'token' => token);
echo json_encode($result);

function error($error_text){
	$result=array("error"=>$error_text);
	echo json_encode($result);
}
?>