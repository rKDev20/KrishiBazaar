<?php
$json = file_get_contents('php://input');
$params = json_decode($json,true);
$params["token"]);
$params["product_id"]);
$params["price"]);


$result = array('status' => 1);
echo json_encode($result);

function error($error_text){
	$result=array("status"=>0);
	echo json_encode($result);
}
?>