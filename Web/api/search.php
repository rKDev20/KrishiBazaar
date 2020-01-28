<?php
$json = file_get_contents('php://input');
$params = json_decode($json,true);
//echo $json;
sleep(1);
$maxsize=13;
if(isset($params["search"])&&isset($params["size"])&&isset($params["pageOffset"])){
	if($params["size"]*($params["pageOffset"]+1)>$maxsize){
		echo json_encode(array('success'=>array()));		
	}
	else{
		$res1 = array('product_id' => 2432, 'name' => "Wheat",'quantity' => 25.5,'price' => 50.5,'description' => "Tasty Tasty Wheat",'distance' => $params["pageOffset"]);
		$res2 = array('product_id' => 2433, 'name' => "Rice",'quantity' => 10,'price' => 30,'description' => "Rice Rice",'distance' => $params["pageOffset"]);
		$res3 = array('product_id' => 2434, 'name' => "fruit",'quantity' => 20,'price' => 40,'description' => "FRUIT tasty tasty",'distance' => $params["pageOffset"]);
		$result = array('success' => array($res1,$res2,$res3));
		echo json_encode($result);
	}
}
else 
	error("fuck off bitch");
function error($error_text){
	$result=array("error"=>$error_text);
	echo json_encode($result);
}
?>