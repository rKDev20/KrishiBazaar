<?php
//DONE
include '../db.php';
include '../utils.php';
define('PENDING', 2);

$json = file_get_contents('php://input');
$params = json_decode($json,true);
if(!(isset($params["token"])&&isset($params["product_id"])&&isset($params["pincode"])))
	error();
if (isset($params["price"])&& is_numeric($params["price"]))
	$price=$params["price"];
else
	$price="NULL";
$token=$params["token"];
$product_id=$params["product_id"];
$pincode=$params["pincode"];
if ($token===""||!is_numeric($product_id)||!checkPincode($pincode))
	error();
$query="SELECT ad.mobile FROM advertisements ad INNER JOIN authorisation au ON ad.mobile=au.mobile WHERE au.token='".$token."' AND ad_id=".$product_id.";";
mysqli_query($conn,$query);
if (mysqli_error($conn)||mysqli_affected_rows($conn)==1)
	error();
$query="INSERT INTO transactions(ad_id, proposed_rate, mobile, pincode, status) SELECT ".$product_id.", ".$price.", mobile, ".$pincode.", ".PENDING." FROM authorisation WHERE token='".$token."';";
mysqli_query($conn,$query);
if (mysqli_error($conn))
	error();
if (mysqli_affected_rows($conn)==1)
	success();
else error();

function success(){
	echo json_encode(array("status"=>1));
	die();
}
function error(){
	echo json_encode(array("status"=>0));
	die();
}
?>