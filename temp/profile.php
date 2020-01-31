<?php
$json = file_get_contents('php://input');
$params = json_decode($json,true);
if(isset($params["token"])){
	echo json_encode(array('success' => array('name' => $params["token"],'mobile' => 8981874182,'address' => "kiit university",'pincode' => 751024)));
}
function error($error_text){
	$result=array("error"=>$error_text);
	echo json_encode($result);
}
?>