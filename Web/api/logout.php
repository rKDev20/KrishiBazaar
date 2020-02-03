<?php
$json = file_get_contents('php://input');
$params = json_decode($json,true);
$params["token"]);

echo json_encode(array('status' => 1));

function error($error_text){
	$result=array("status"=>0);
	echo json_encode($result);
}
?>