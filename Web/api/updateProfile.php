<?php
//NOT NECESSARY
$json = file_get_contents('php://input');
$params = json_decode($json,true);
$params["token"]);
$params["name"]);
$params["mobile"]);
$params["address"]);
$params["pincode"]);






echo json_encode(array('status' => 1));

function error($error_text){
	$result=array("status"=>0);
	echo json_encode($result);
}
?>