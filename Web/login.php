<?php
session_start();
include('connection.php');
function getCurrentTimestamp(){
	return (new DateTime())->format('Y-m-d H:i:s');
}
$mobile = $_POST['mobile'];
$otp = $_POST['otp'];
$time = getCurrentTimestamp();
$qry ="SELECT clock FROM otpverification WHERE TIMESTAMPDIFF(MINUTE,clock,'".getCurrentTimestamp()."') <= 5
AND otp = $otp AND mobile = $mobile";
$row = mysqli_query($conn,$qry);
if(mysqli_num_rows($row) == 1)
{
	$qry = "SELECT name, pincode FROM users WHERE mobile = $mobile";
	//echo $qry;
  $row = mysqli_query($conn, $qry);
	if(mysqli_num_rows($row) != 1)
	{
		$_SESSION['message'] = "User not found. Please SignUp.";
		echo $_SESSION['message'];
		header('location:'.$_POST['redirect']);
		exit();
	}
	$userData = mysqli_fetch_assoc($row);
  $_SESSION['id']= session_id();
  $_SESSION['user_name'] = $userData['name'];
	$_SESSION['mobile'] = $mobile;
	$_SESSION['pincode'] = $userData['pincode'];
	header('location:'.$_POST['redirect']);
}
else {
  $_SESSION['message'] = "Invalid Input";
  echo "not ok";
  header('location:'.$_POST['redirect']);
}
?>
