<?php
define('OWNED', 0);
define('AVAILABLE', 1);
define('PENDING', 2);
define('ACCEPTED', 3);
define('REJECTED', 4);
define('SOLD', 5);
define('DELETED', 6);

$json = file_get_contents('php://input');
$params = json_decode($json,true);
$params["token"]);
$params["pageOffset"]);
$params["size"]);

$res = array(
	'image_url'=>'onion.png',
	'product_id' => 2432,
	'name' => "Wheat",
	'quantity' => 25.5,
	'price' => 50.5,
	'description' => "Tasty Tasty Wheat",
	'distance' => 32,
	'status'=>constant);

$result = array('success' => array($res1,$res2,$res3));
echo json_encode($result);

function error($error_text){
	$result=array("error"=>$error_text);
	echo json_encode($result);
}
?>