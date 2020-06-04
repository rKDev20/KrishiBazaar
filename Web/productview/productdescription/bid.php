<?php
session_start();
include('../../util/db.php');
include('../../util/notify.php');
if(isset($_SESSION['mobile']))
{
  $mobile = $_SESSION['mobile'];
	if(empty($_POST['price']))
	{
		$_SESSION['message'] = "Price missing";
		header('location:'.$_POST['redirect']);
		exit();
	}
  $p = $_POST['price'];
  $ad = $_POST['ad_id'];
  $pin = $_SESSION['pincode'];
  //$time = getCurrentTimestamp();
  $insert = "INSERT INTO transactions (`transaction_id`, `ad_id`, `proposed_rate`, `mobile`, `clock`, `pincode`, `status`)
  VALUES (NULL,$ad, $p, $mobile,CURRENT_TIMESTAMP,$pin,2)";
	$q = mysqli_query($conn,$insert);
	echo $insert;
	if($q)
	{
		echo "Success";
    
    header('location:'.$_POST['redirect']);
		exit();
	}
	else {
		echo "Failed";
		header('location:'.$_POST['redirect']);
		exit();
	}
}
?>
