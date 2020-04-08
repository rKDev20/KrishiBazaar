<?php
//DONE
include_once '../util/db.php';
define('ACTIVE', 1);
define('INACTIVE',0);

$json = file_get_contents('php://input');
$params = json_decode($json,true);
if (!(isset($params["token"])&&isset($params["product_id"])))
	error();
$token=$params["token"];
$product_id=$params["product_id"];
if (!is_numeric($product_id)||$token==="")
	error();
$query="UPDATE advertisements ad INNER JOIN authorisation au ON ad.mobile=au.mobile SET ad.status=".INACTIVE." WHERE ad_id=".$product_id." AND token='".$token."';";
mysqli_query($conn,$query);
if (mysqli_error($conn)||mysqli_affected_rows($conn)!=1)
	error();
else
	success();
function success(){
	echo json_encode(array("status"=>1));
	die();
}
function error(){
	echo json_encode(array("status"=>0));
	die();
}
?>