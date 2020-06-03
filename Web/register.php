<?php
session_start();
include('connection.php');
include('util/utils.php');
$mobile = $_POST['mobile'];
$otp = $_POST['otp'];
$time = getCurrentTimestamp();
$qry ="SELECT clock FROM otpverification WHERE TIMESTAMPDIFF(MINUTE,clock,'".getCurrentTimestamp()."') <= 5 AND otp = $otp AND mobile = $mobile";
$row = mysqli_query($conn,$qry);
if(mysqli_num_rows($row) == 1)
{
  $name = $_POST['name'];
  $address = $_POST['address'];
  $pincode = $_POST['pinCode'];
	if(checkPincode($conn,$pincode) == false)
	{
		$_SESSION['message'] = "Invalid Pincode";
	  header('location:register.php');
	}
  $qry = "SELECT name FROM users WHERE mobile = $mobile";
  $row = mysqli_query($conn, $qry);
  if(mysqli_num_rows($row) == 1)
  {
    $_SESSION['id']= session_id();
    $_SESSION['user_name'] = mysqli_fetch_assoc($row)['name'];
    $_SESSION['mobile'] = $mobile;
    header('location:index.php');
  }
  else
  {
        $insert = "INSERT INTO users (`mobile`, `address`, `pincode`, `name`) VALUES ($mobile, '$address', $pincode, '$name')";
        $new_row = mysqli_query($conn,$insert);
        if($new_row)
        {
          $_SESSION['id']= session_id();
          $_SESSION['user_name'] = $name;
          $_SESSION['mobile'] = $mobile;
          header('location:index.php');
        }
    }
}
else {
  $_SESSION['message'] = "Invalid Input";
  header('location:register.php');
}
?>
