<?php
define('OWNED', 0);
define('AVAILABLE', 1);
define('PENDING', 2);
define('ACCEPTED', 3);
define('REJECTED', 4);
define('SOLD', 5);


define('NOT_DECIDED', 0);
define('ACCEPTED', 1);
define('DENIED', 2);

$json = file_get_contents('php://input');
$params = json_decode($json,true);
$params["token"];
$params["product_id"];
$params["latitude"];//optional
$params["longitude"];//optional





$buyers=array('tran_id' => 222,
	'name' => "scsc",
	'price' => 50.5,
	'timestamp' => time in miliseconds,
	'pincode' =>343434
	'status' => constants,
);
$res = array(
	'image_url'=>'onion.png',
	'name' => "Wheat",
	'quantity' => 25.5,
	'price' => 50.5,
	'description' => "Tasty Tasty Wheat",
	'distance' => 32,//optional
	'pincode' => 343343
	'status' => constant,
	'farmer_mobile' => 222222222,//if status is ACCEPTED
	'buyers' => array(buyer1,buyer2,buyer3)//if status is OWNED
);
$result = array('success' => $res);
echo json_encode($result);

function error($error_text){
	$result=array("error"=>$error_text);
	echo json_encode($result);
}
?>