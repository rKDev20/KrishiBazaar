<?php
$json = file_get_contents('php://input');
$params = json_decode($json,true);
if(isset($params["latitude"])&&isset($params["longitude"])){
	query("http://dev.virtualearth.net/REST/v1/Locations/".trim($params['latitude']).",".trim($params['longitude'])."?maxResults=3&key=AuZ2njjdMZzeH1irPijjn6-Ex2GXdzc4R1ND61mj98qaQLeUfURW-jtVXnSg1Zgl");
}
elseif (isset($params["search"])) {
	query("http://dev.virtualearth.net/REST/v1/Locations/".trim($params['search'])."?maxResults=3&key=AuZ2njjdMZzeH1irPijjn6-Ex2GXdzc4R1ND61mj98qaQLeUfURW-jtVXnSg1Zgl");
}
else{
	error("Wrong parameters");
}
function query($url){
	try{
		$ch = curl_init();   
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); 
		curl_setopt($ch, CURLOPT_URL, $url); 
		$result = json_decode(curl_exec($ch),true);
		if (isset($result["resourceSets"][0]["resources"][0]["name"])&&
			isset($result["resourceSets"][0]["resources"][0]["point"]["coordinates"][0])&& 
			isset($result["resourceSets"][0]["resources"][0]["point"]["coordinates"][1])){
				$resultJSON = array("address"=>$result["resourceSets"][0]["resources"][0]["name"],
					"latitude"=>$result["resourceSets"][0]["resources"][0]["point"]["coordinates"][0],
					"longitude"=>$result["resourceSets"][0]["resources"][0]["point"]["coordinates"][1]);
			$finalResult = array("success"=>$resultJSON);	
			echo json_encode($finalResult);
		}else throw new Exception("Error Processing Request", 1);
	}
	catch(Exception $e){
		echo error("No result found");
	}
}
function error($error_text){
	$result=array("error"=>$error_text);
	echo json_encode($result);
}
?>