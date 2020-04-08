<?php

include_once '../util/db.php';
include_once '../sms/constants.php';
include_once '../sms/strings.php';

define('PENDING', 2);
define('ACCEPTED', 3);
define('REJECTED', 4);

$json = file_get_contents('php://input');
$params = json_decode($json, true);
$mobile = $params["mobile"];
$text = trim($params["text"]);
$auth=$params["auth"];

if($auth!="KKejNeOGdQR46bm6RNgl3344E29ICQjZ"){
    die("Unauthorised access");
}

if (!$conn) {
    die(mysqli_connect_error());
}
if (preg_match('/^[0-9]+/', $text, $match)) {
    $tran_id = $match[0];
    if (preg_match('/^[0-9]+\s+ACCEPT$/', $text)) {
        $newStatus = ACCEPTED;
    } elseif (preg_match('/^[0-9]+\s+REJECT$/', $text)) {
        $newStatus = REJECTED;
    } else {
		sendSms(WRONG_INPUT_TRANSACTIONS);
    }
} else {
    sendSms(WRONG_INPUT_TRANSACTIONS);
}
$query="SELECT t.status 
FROM transactions t
INNER JOIN advertisements ad ON ad.ad_id=t.ad_id 
WHERE ad.mobile=$mobile AND t.transaction_id=$tran_id";
$result=mysqli_query($conn,$query);
if(mysqli_num_rows($result)==0)
	sendSms(NO_TRANSACTION_FOUND);
else {
	$row=mysqli_fetch_assoc($result);
	if ($row["status"]==PENDING) {
		$query="UPDATE transactions SET status=$newStatus WHERE transaction_id=$tran_id";
		mysqli_query($conn,$query);
		if ($newStatus==ACCEPTED) {
			sendSms(ACCEPTED_SUCCESS);
		}
		else sendSms(REJECTED_SUCCESS);
	}
	else if ($row["status"]==REJECTED) {
		sendSms(TRANSACTION_ALREADY_REJECTED);
	}
	else sendSms(TRANSACTION_ALREADY_ACCEPTED);
}

function sendSms($text) {
	echo json_encode(array('text' => $text));
	die();
}
?>