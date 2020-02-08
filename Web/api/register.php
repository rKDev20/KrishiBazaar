<?php
//DONE
include '../db.php';
include '../utils.php';
$json = file_get_contents('php://input');
$params = json_decode($json,true);
if(!(isset($params["token"])&&isset($params["name"])&&isset($params["address"])&&isset($params["pincode"])))
	error();
$token=$params["token"];
$name=$params["name"];
$address=$params["address"];
$pincode=$params["pincode"];
if ($address===""||!checkPincode($pincode)||$name===""||$token==="") 
	error();
$query="UPDATE users INNER JOIN authorisation ON users.mobile = authorisation.mobile SET users.address='".$address."',users.pincode=".$pincode.",users.name='".$name."' WHERE authorisation.token='".$token."';";
mysqli_query($conn,$query);
success();

function success(){
	echo json_encode(array('status' => 1));
	die();
}

function error(){
	$result=array("status"=>0);
	echo json_encode($result);
	die();
}
?>


