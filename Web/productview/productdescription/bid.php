<?php
session_start();
include('../../connection.php');
function getCurrentTimestamp(){
	return (new DateTime())->format('Y-m-d H:i:s');
}
if(isset($_SESSION['mobile']))
{
  $mobile = $_SESSION['mobile'];
  $p = $_POST['price'];
  $ad = $_POST['ad_id'];
  $pin = $_SESSION['pincode'];
  //$time = getCurrentTimestamp();
  $insert = "INSERT INTO transactions (`transaction_id`, `ad_id`, `proposed_rate`, `mobile`, `timestamp`, `pincode`, `status`)
  VALUES (NULL,$ad, $p, $mobile,CURRENT_TIMESTAMP,$pin,2)";
	$q = mysqli_query($conn,$insert);
	//echo $insert;
  //redirect back to previous page
  header('location:'.$_POST['redirect']);
}
?>
