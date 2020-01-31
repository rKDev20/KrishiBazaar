<?php
$json = file_get_contents('php://input');
$params = json_decode($json,true);
if(isset($params["token"])){
	echo json_encode(array('status' => 1));
}
function error($error_text){
	$result=array("error"=>$error_text);
	echo json_encode($result);
}
?>