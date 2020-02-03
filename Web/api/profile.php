<?php
$json = file_get_contents('php://input');
$params = json_decode($json,true);
$params["token"];





$success=array(
	'name' => "name",
	'mobile' => 8981874182,
	'address' => "kiit university",
	'pincode' => 751024);
$result=array('success' => $success));
echo json_encode($result);

function error($error_text){
	$result=array("error"=>$error_text);
	echo json_encode($result);
}
?>