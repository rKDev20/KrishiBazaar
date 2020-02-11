<?php
//DONE
include '../db.php';
include '../utils.php';
$json = file_get_contents('php://input');
$params = json_decode($json,true);
if (!(isset($params["token"])&&isset($params["category"])&&isset($params["subCategory"])&&isset($params["name"])&&isset($params["quantity"])&&isset($params["price"])&&isset($params["description"])&&isset($params["pincode"])))
	error();
$token=$params["token"];//not null string
$category=$params["category"];//not null int
$subCategory=$params["subCategory"];//not null int
$name=$params["name"];//not null string
$quantity=$params["quantity"];//not null number
$price=$params["price"];//not null number
$description=$params["description"];//not null string
$pincode=$params["pincode"];//not null pincode
if ($token===""||!is_numeric($category)||!is_numeric($subCategory)||$name===""||!is_numeric($quantity)||!is_numeric($price)||$description===""||!checkPincode($pincode))
	error();
$query="INSERT INTO advertisements(mobile, category,sub_category, name, quantity, rate, description, pincode) SELECT mobile, ".$category.", ".$subCategory.", '".$name."', ".$quantity.", ".$price.", '".$description."', ".$pincode." FROM authorisation WHERE token='".$token."';";
mysqli_query($conn,$query);
if(mysqli_error($conn))
	error();
if (mysqli_affected_rows($conn)==1)
	success(mysql_insert_id($conn));
else error();

function success($product_id){
	echo json_encode(array('status' => $product_id));
}
function error(){
	echo json_encode(array("status"=>0));
	die();
}
?>