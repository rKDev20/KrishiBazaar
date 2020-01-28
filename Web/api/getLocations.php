<?php
	$json = file_get_contents('php://input');
	$params = json_decode($json,true);
	$url="http://dev.virtualearth.net/REST/v1/Locations/query=".urlencode($params["search"])."?maxResults=10&key=AuZ2njjdMZzeH1irPijjn6-Ex2GXdzc4R1ND61mj98qaQLeUfURW-jtVXnSg1Zgl";
	$ch = curl_init();   
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); 
	curl_setopt($ch, CURLOPT_URL, $url); 
	$result = json_decode(curl_exec($ch),true);
	$array=$result["resourceSets"][0]["resources"];
	$resultLen=count($result["resourceSets"][0]["resources"]);
	$resultJSON[]=array();
	for($i=0;$i<$resultLen;$i++){
		if($array[$i]["address"]["countryRegion"]=="India")
			array_push($resultJSON,array("name"=>$array[$i]["name"],"latitude"=>$array[$i]["point"]["coordinates"][0],"longitude"=>$array[$i]["point"]["coordinates"][1]));
	}
	if(count($resultJSON)==0)
		echo json_encode(array("error"=>"Nothing found"));
	else
		echo json_encode(array("success"=>$resultJSON));
?>