<?php
include '../db.php';
include '../utils.php';
//DONE
define('OWNED', 0);
define('AVAILABLE', 1);
define('PENDING', 2);
define('ACCEPTED', 3);
define('REJECTED', 4);
define('SOLD', 5);
define('DELETED', 6);

define('ACTIVE', 1);
define('INACTIVE',0);

define('WRONG_PARAMETERS',0);
define('NOT_AUTHORISED', 1);
define('PRODUCT_NOT_FOUND', 2);


$json = file_get_contents('php://input');
$params = json_decode($json,true);
if (!(isset($params["token"])&&$params["product_id"]))
	error(WRONG_PARAMETERS);
$token=$params["token"];
$product_id=$params["product_id"];
if ($token===""||!is_numeric($product_id))
	error();
if (isset($params["latitude"])&&$params["longitude"]){
	$latitude=$params["latitude"];
	$longitude=$params["longitude"];
}
else{
	$latitude="NULL";
	$longitude="NULL";
}
$query="SELECT * FROM advertisements ad INNER JOIN authorisation au ON ad.mobile=au.mobile WHERE ad_id=".$product_id." AND token='".$token."';";
$result=mysqli_query($conn,$query);
if ($row=mysqli_fetch_assoc($result)) {
	if ($row["status"]==INACTIVE) {
		$status=DELETED;
	}
	else $status=OWNED;
	$mobile=$row["mobile"];
	$sellerPincode=$row["pincode"];
	$queryTran="SELECT transaction_id,name,clock,status,t.pincode,proposed_rate FROM transactions t INNER JOIN users u ON u.mobile=t.mobile WHERE ad_id=$product_id ORDER BY t.clock ASC";
	$resultTran=mysqli_query($conn,$queryTran);
	$buyers=array();
	while($rowNew=mysqli_fetch_assoc($resultTran)){
		$tmp=array('tran_id' => (int)$rowNew["transaction_id"],
			'name' => $rowNew["name"],
			'timestamp' => getTimeInMillis($rowNew["clock"]),
			'status' => (int)$rowNew["status"],
			'distance' => getDistanceByPincode($sellerPincode,$rowNew["pincode"])
		);

		if (!is_null($rowNew["proposed_rate"]))
			$tmp["price"]=(float)$rowNew["proposed_rate"];
		array_push($buyers,$tmp);
	}
	//TODO add image_url
	$latlng=getCoordinates($row["pincode"]);
	$res = array(
		'image_url'=>'onion.png',
		'name' => $row["name"],
		'quantity' => (float)$row["quantity"],
		'price' => (float)$row["rate"],
		'description' => $row["description"],
		'pincode' => (int)$row["pincode"],
		'latitude'=>$latlng["latitude"],
		'longitude'=>$latlng["longitude"],
		'status' => $status,
		'buyers' => $buyers
	);
	if (!is_null($row["category"]))
		$res["category"]=$row["category"];
	if (!is_null($row["sub_category"]))
		$res["sub"]=$row["sub_category"];
	success($res);
}
else{
	$query="SELECT ad.name,ad.quantity,ad.rate,ad.description,ad.pincode AS apincode,t.pincode AS tpincode, ad.mobile,t.status,ad.status AS astatus FROM transactions t INNER JOIN authorisation au ON au.mobile=t.mobile INNER JOIN advertisements ad ON ad.ad_id=t.ad_id WHERE ad.ad_id=".$product_id." AND au.token='".$token."';";
	$result=mysqli_query($conn,$query);
	if ($row=mysqli_fetch_assoc($result)) {
		if ($row["astatus"]==ACTIVE)
			$status=$row["status"];
		else $status=SOLD;
		$latlng=getCoordinates($row["apincode"]);
		$res = array(
			'image_url'=>'onion.png',
			'name' => $row["name"],
			'quantity' => (float)$row["quantity"],
			'price' => (float)$row["rate"],
			'description' => $row["description"],
			'latitude'=>$latlng["latitude"],
			'longitude'=>$latlng["longitude"],
			'distance' => getDistanceByPincode($row["apincode"],$latlng["latitude"],$latlng["longitude"]),//optional
			'pincode' => (int)$row["apincode"],
			'status' => (int)$status
		);
		if (!is_null($row["category"]))
			$res["category"]=$row["category"];
		if (!is_null($row["sub_category"]))
			$res["sub"]=$row["sub_category"];
		if ($status==ACCEPTED) {
			$res["farmer_mobile"]=$row["mobile"];
		}
		success($res);
	}
	else{
		$query="SELECT * FROM advertisements WHERE ad_id=".$product_id.";";
		$result=mysqli_query($query);
		if ($row=mysqli_fetch_assoc($result)) {
			if ($row["status"]==ACTIVE)
				$status=AVAILABLE;
			else error(NOT_AUTHORISED);
			$latlng=getCoordinates($row["apincode"]);
			$res = array(
				'image_url'=>'onion.png',
				'name' => $row["name"],
				'quantity' => (float)$row["quantity"],
				'price' => (float)$row["rate"],
				'description' => $row["description"],
				'pincode' => (int)$row["pincode"],
				'latitude'=>$latlng["latitude"],
				'longitude'=>$latlng["longitude"],
				'status' => (int)$status
			);
			if (!is_null($row["category"]))
				$res["category"]=$row["category"];
			if (!is_null($row["sub_category"]))
				$res["sub"]=$row["sub_category"];
			if ($latitude!=="NULL") 
				$res['distance'] = getDistanceByCoordinates($row["pincode"],$latitude,$longitude);
			success($res);
		}
		else error(PRODUCT_NOT_FOUND);	
	}
}

function success($res){
	$result=array("success"=>$res);
	echo json_encode($result);
	die();
}

function error($error_text){
	$result=array("error"=>$error_text);
	echo json_encode($result);
	die();
}
?>