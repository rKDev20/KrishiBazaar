<?php
$json = file_get_contents('php://input');
$params = json_decode($json,true);
$params["search"];
$params["size"];
$params["pageOffset"];
$params["latitude"];//optional
$params["longitude"];//optional
//if no result
echo json_encode(array('success'=>array()));		
//if res
$res = array(
	'image_url'=>'onion.png',
	'product_id' => 2432,
	'name' => "Wheat",
	'quantity' => 25.5,
	'price' => 50.5,
	'description' => "Tasty Tasty Wheat",
	'distance' => 32);//optional
$result = array('success' => array($res1,$res2,$res3));
echo json_encode($result);

function error($error_text){
	$result=array("error"=>$error_text);
	echo json_encode($result);
}
?>