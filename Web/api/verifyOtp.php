<?php
//DONE
include_once '../util/db.php';
include_once '../util/utils.php';
define('WRONG_PARAMETERS', 0);
define('MISMATCH', 1);

define('USER_EXISTS', 1);
define('USER_NEW', 0);
$json = file_get_contents('php://input');
$params = json_decode($json,true);
if(!(isset($params["mobile"])&&isset($params["otp"])&&isset($params["fcm"])))
	error(WRONG_PARAMETERS);
$mobile=$params["mobile"];
$otp=$params["otp"];
$fcm=$params["fcm"];
if (!(checkMobile($mobile)&&checkOtp($otp))||$fcm==="")
	error(WRONG_PARAMETERS);
$query="SELECT mobile FROM otpverification WHERE mobile = ".$mobile." AND otp =".$otp." AND TIMESTAMPDIFF(MINUTE,clock,'".getCurrentTimestamp()."') < 5;";
$result = mysqli_query($conn,$query);
if(mysqli_num_rows($result)==0)
	error(MISMATCH);
$query="SELECT pincode FROM users WHERE mobile = ".$mobile.";";
$result = mysqli_query($conn,$query);
if (mysqli_num_rows($result)==0) {
	$query="INSERT INTO users (mobile) VALUES(".$mobile.");";
	mysqli_query($conn,$query);
	$status=USER_NEW;
}
else{
	if(is_null(mysqli_fetch_assoc($result)["pincode"]))
		$status=USER_NEW;
	else $status=USER_EXISTS;
}
do{
	$token=bin2hex(openssl_random_pseudo_bytes(16));
	$query="INSERT INTO authorisation VALUES('".$token."',".$mobile.",'".$fcm."');";
	mysqli_query($conn,$query);
	$err=mysqli_error($conn);
	echo $err;
}while($err!=="");
$query="DELETE FROM otpverification WHERE mobile = '".$mobile."' AND otp = ".$otp.";";
mysqli_query($conn,$query);
success($status,$token);
function success($status,$token){
	$result=array("success"=>array(
		'status' => $status,
		'token' => $token));
	echo json_encode($result);
	die();	
}
function error($error_text){
	$result=array("error"=>$error_text);
	echo json_encode($result);
	die();
}
?>