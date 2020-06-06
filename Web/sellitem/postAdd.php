<?php
session_start();
if(!isset($_SESSION['mobile']))
{
		$_SESSION['message'] = "Please LogIn/SignUp to post ad.";
	  header('location:../index.php');
}
include_once '../util/db.php';
include_once '../util/utils.php';
$pname = $_POST['pname'];
if($_POST['category'] == -1)
	{
		$cat = 'NULL';
	}
else {
	$cat = $_POST['category'];
}
if($_POST['SubCategory'] != -1)
	{
		$sub = $_POST['SubCategory'];
	}
else {
	$sub = 'NULL';
}

$q = $_POST['quantity'];
$price = $_POST['price'];
$pin = $_POST['pin'];
$des = $_POST['description'];
$mobile = $_SESSION['mobile'];
if(checkPincode($conn,$pin) == false)
{
  $_SESSION['message'] = "Invalid Pincode";
  header('location:sell.php');
}
$qry = "INSERT INTO advertisements
(`mobile`, `category`, `sub_category`, `name`, `quantity`, `rate`, `description`, `pincode`, `clock`, `status`)
VALUES
($mobile, $cat, $sub, '$pname', $q, $price, '$des', $pin, CURRENT_TIMESTAMP, 1)";
$runQry = mysqli_query($conn,$qry);

if($runQry)
{
  $_SESSION['message'] = "Add posted successfully!!";
  //echo "Posted \n";
  //echo $qry;
  header('location:../profile/profile.php');
	exit();
}
else {
	echo $_POST['category'];
	echo $_POST['SubCategory'];
	echo $cat;
	echo $sub;
  $_SESSION['message'] = "Could not post add!!";
  echo "Not Posted \n";
  echo $qry;
  header('location:sell.php');
	exit();
}
?>
