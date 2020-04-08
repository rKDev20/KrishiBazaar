<?php
//DONE
include_once '../util/sendSms.php';
include_once '../util/db.php';
include_once '../util/utils.php';
include_once '../sms/constants.php';

$json = file_get_contents('php://input');
$params = json_decode($json,true);
if(!isset($params["mobile"]))
	error(2);
$mobile=$params["mobile"];
if (!checkMobile($mobile))
	error(4);
$otp= rand(100000,999999);
$otpMessage="Welcome to KrishiBazaar! Your OTP is $otp. Do not share it with anyone.";
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
	else success();
}
$result=mysqli_query($conn,$query);
if(!$result)
	error(6);
if(sendSms($mobile,$otpMessage))
	success();
error(7);
function success(){
	echo json_encode(array('status' => 1));
	die();	
}
function error($err){
	echo json_encode(array("status"=>$err));
	die();
}
?>