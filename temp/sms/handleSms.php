<?php
include '../db.php';
include '../constants.php';
include '/strings.php';

$json = file_get_contents('php://input');
$params = json_decode($json,true);
$mobile = $params["mobile"];
$text = trim($params["text"]);

if (!$conn) {
	die(mysqli_connect_error())	;
}

$instance = generateSmsInstance($conn,$mobile);//id,status

switch ($instance['status']) {
	case STATE_NEW : sendNewMessage($conn,$instance['id']);
	break;
	case STATE_EXPECT_ACTION : processAction($conn,$instance['id'],$text);
	break;
	case STATE_EXPECT_NAME : processName($conn,$instance['id'],$text);
	break;
	case STATE_EXPECT_DESCRIPTION : processDescription($conn,$instance['id'],$text);
	break;
	case STATE_EXPECT_LOCATION : processLocation($conn,$instance['id'],$text);
	break;
	case STATE_EXPECT_QUANTITY : processQuantity($conn,$instance['id'],$text);
	break;
	case STATE_EXPECT_PRICE : processPrice($conn,$instance['id'],$text);
	break;
	case STATE_EXPECT_ITEM_ID : processDeleteRequest($conn,$instance['id'],$text);
	break;
}

function sendNewMessage($conn,$id){
	$time=getCurrentTimestamp();
	$query="UPDATE sms_instance SET status=".STATE_EXPECT_ACTION.", clock='$time' WHERE sms_id=$id;";
	mysqli_query($conn,$query);
	sendSms(WELCOME_MSG);
}

function processAction($conn,$id,$text){
	switch ($text) {
		case ACTION_UPLOAD:
		$time=getCurrentTimestamp();
		$query="UPDATE sms_instance SET status=".STATE_EXPECT_NAME.", clock='$time' WHERE sms_id=$id;";
		mysqli_query($conn,$query);
		sendSms(ENTER_NAME);
		break;
		case ACTION_REMOVE:
		$time=getCurrentTimestamp();
		$query="UPDATE sms_instance SET status=".STATE_EXPECT_ITEM_ID.", clock='$time' WHERE sms_id=$id;";
		mysqli_query($conn,$query);
		sendSms(ENTER_PRODUCT_ID);
		break;
		default:
		sendSms(WRONG_INPUT);
		break;
	}
}

function processName($conn,$id,$text){
	$time=getCurrentTimestamp();
	$text=trim($text);
	$query=mysqli_prepare($conn,"UPDATE sms_instance SET status=".STATE_EXPECT_DESCRIPTION.", clock='$time', name=? WHERE sms_id=$id;");
	mysqli_stmt_bind_param($query,'s',$text);
	mysqli_stmt_execute($query);
	sendSms (sprintf(ENTER_DESCRIPTION,$text));
}

function processDescription($conn,$id,$text){
	$time=getCurrentTimestamp();
	$query=mysqli_prepare($conn,"UPDATE sms_instance SET status=".STATE_EXPECT_LOCATION.", clock='$time', description=? WHERE sms_id=$id;");
	mysqli_stmt_bind_param($query,'s',$text);
	mysqli_stmt_execute($query);
	sendSms(ENTER_LOCATION);
}

function processLocation($conn,$id,$text){
	$time=getCurrentTimestamp();
	if(isValidPincode($text)){
		$query=mysqli_prepare($conn,"UPDATE sms_instance SET status=".STATE_EXPECT_QUANTITY.", clock='$time', pincode=? WHERE sms_id=$id;");
		mysqli_stmt_bind_param($query,'i',$text);
		mysqli_stmt_execute($query);
		sendSms (ENTER_QUANTITY);
	}
	else
		sendSms (WRONG_LOCATION);
}

function isValidPincode($pincode){
	if (is_numeric($pincode) && strlen($pincode)==6 && substr($pincode, 0,1)!=0 )
		return true;
	else
		return false;
}

function processQuantity($conn,$id,$text){
	$time=getCurrentTimestamp();
	if(isValidNumber($text)){
		$query=mysqli_prepare($conn,"UPDATE sms_instance SET status=".STATE_EXPECT_PRICE.", clock='$time', quantity=? WHERE sms_id=$id;");
		mysqli_stmt_bind_param($query,'i',$text);
		mysqli_stmt_execute($query);
		sendSms(ENTER_PRICE);
	}
	else
		sendSms(WRONG_INPUT);
}

function processPrice($conn,$id,$text){
	$time=getCurrentTimestamp();
	if(isValidNumber($text)){
		$query=mysqli_prepare($conn,"UPDATE sms_instance SET status=".STATE_EXPECT_PRICE.", clock='$time', rate=? WHERE sms_id=$id;");
		mysqli_stmt_bind_param($query,'d',$text);
		mysqli_stmt_execute($query);
		addProduct($conn,$id);
	}
	else
		sendSms(WRONG_INPUT);
}

function addProduct($conn,$id){
	$query="SELECT * FROM sms_instance WHERE sms_id=$id;";
	$result=mysqli_query($conn,$query);
	echo mysqli_error($conn);
	$row=mysqli_fetch_assoc($result);
	$mobile=$row['mobile'];
	$name=$row['name'];
	$description=$row['description'];
	$pincode=$row['pincode'];
	$quantity=$row['quantity'];
	$rate=$row['rate'];
	$query="INSERT INTO advertisements (mobile,name,quantity,rate,description,pincode) VALUES ($mobile,'$name',$quantity,$rate,'$description',$pincode)";
	mysqli_query($conn,$query);
	$ad_id=mysqli_insert_id($conn);
	sendSms(sprintf(SUCCESS,$ad_id));
	$query="DELETE FROM sms_instance WHERE sms_id = $id";
	mysqli_query($conn,$query);
}

function isValidNumber($number){
	if (is_numeric($number))
		return true;
	else return false;
}

function generateSmsInstance($conn,$mobile){
	//CHECK IF A SMS INSTANCE EXISTS
	$currentTime= getCurrentTimestamp();
	$query="SELECT sms_id,clock,status FROM sms_instance WHERE mobile = $mobile AND TIMESTAMPDIFF(MINUTE,clock,'$currentTime') < ".TIMEOUT.";";
	$result=mysqli_query($conn,$query);
	
	//IF SMS INSTANCE DOESNT EXISTS
	if (mysqli_num_rows($result) > 0) {
		$row = mysqli_fetch_assoc($result);
		return array('id' => $row["sms_id"],'status'=> $row["status"]);
	}
	else{
		//CHECK IF MOBILE IS REGSITERED. ADD IF NOT.
		$query="SELECT mobile FROM users WHERE mobile = $mobile;";
		$result=mysqli_query($conn,$query);
		if (mysqli_num_rows($result) == 0) {
			$query="INSERT into users(mobile) VALUES($mobile);";
			mysqli_query($conn,$query);
		}
		//CREATE A NEW SMS INSTANCE
		$query="INSERT into sms_instance(mobile) VALUES($mobile);";
		$result=mysqli_query($conn,$query);
		return array('id' => mysqli_insert_id($conn),'status'=>STATE_NEW);
	}	
}

function sendSms($text){
	echo json_encode(array('text' => $text));
}
function getCurrentTimestamp(){
	return (new DateTime())->format('Y-m-d H:i:s');
}
?>