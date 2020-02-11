<?php
//DONE
include '../db.php';
$json = file_get_contents('php://input');
$params = json_decode($json,true);
$params["token"];
if (!isset($params["token"]))
	error();
$token=$params["token"];
if ($token==="")
	error();
$query="DELETE FROM authorisation WHERE token = '".$token."';";
mysqli_query($conn,$query);
if (mysqli_affected_rows($conn)==1)
	success();
error();

function success(){
	echo json_encode(array('status' => 1));
	die();
}

function error(){
	echo json_encode(array("status"=>0));
	die();
}
?>