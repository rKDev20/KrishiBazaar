<?php
//DONE
include '../db.php';
define('WRONG_PARAMETERS', 0);
define('INVALID_TOKEN',1);
$json = file_get_contents('php://input');
$params = json_decode($json,true);
if (!isset($params["token"]))
	error();
$token=$params["token"];
if ($token==="")
	error(WRONG_PARAMETERS);
$query="SELECT * FROM users INNER JOIN authorisation ON users.mobile = authorisation.mobile WHERE authorisation.token='".$token."';";
$result=mysqli_query($conn,$query);
if(mysqli_affected_rows($conn)==1){
	$result=mysqli_fetch_assoc($result);
	success($result["name"],$result["mobile"],$result["address"],$result["pincode"]);
}
else
	error(INVALID_TOKEN);

function success($name,$mobile,$address,$pincode){
	$success=array(
		'name' => $name,
		'mobile' => $mobile,
		'address' => $address,
		'pincode' => $pincode);
	$result=array('success' => $success);
	echo json_encode($result);
	die();
}
function error($error_text){
	$result=array("error"=>$error_text);
	echo json_encode($result);
	die();
}
?>