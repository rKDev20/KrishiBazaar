<?php
//DONE
include '../sendSms.php';
include '../db.php';
include '../utils.php';
$json = file_get_contents('php://input');
$params = json_decode($json,true);
if(!isset($params["mobile"]))
	error();
$mobile=$params["mobile"];
if (!checkMobile($mobile))
	error();
$otp= rand(100000,999999);
$otpMessage="Welcome to KrishiBazaar!%0a Your OTP is $otp. Do not share it with anyone.";
$query="SELECT clock FROM otpverification WHERE mobile = ".$mobile.";";
$result = mysqli_query($conn,$query);
if(mysqli_num_rows($result)==0)
	$query="INSERT INTO otpverification (otp,mobile) VALUES (".$otp.",".$mobile.")";
else{
	$time=new DateTime();
	$clock=new DateTime(mysqli_fetch_assoc($result)['clock']);
	$diff=($time->diff($clock));
	$diff=$diff->format('%i');
	if($diff>1)
		$query="UPDATE otpverification SET clock='".getCurrentTimestamp()."', otp=".$otp." WHERE mobile = ".$mobile.";";
	else error();
}
$result=mysqli_query($conn,$query);
echo mysqli_error($conn);
if(!$result)
	error();
if(sendSms($mobile,$otpMessage))
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