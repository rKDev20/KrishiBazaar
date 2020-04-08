<?php
include_once '../util/sendSms.php';
include_once '../sms/strings.php';
include_once '../sms/constants.php';

function sendNewRequestMessage($conn, $tran_id) {
    $query = "SELECT ad.mobile AS seller,ad.name AS product,buyer.name as buyer , LAT_LONG_DISTANCE(pin1.latitude, pin1.longitude, pin2.latitude, pin2.longitude) AS distance,t.proposed_rate AS rate
	FROM transactions t
	INNER JOIN pincode pin1 ON t.pincode=pin1.pincode
	INNER JOIN advertisements ad ON ad.ad_id = t.ad_id
	INNER JOIN pincode pin2 ON pin2.pincode=ad.pincode
	INNER JOIN users buyer ON buyer.mobile = t.mobile
	WHERE transaction_id = " . $tran_id . ";";
    $result = mysqli_query($conn, $query);
    if (mysqli_num_rows($result) == 0) {
        return;
    }
    $row = mysqli_fetch_assoc($result);
    $seller = $row["seller"];
    $product = $row["product"];
    $buyer = $row["buyer"];
    $distance = $row["distance"];
    $rate = round($row["rate"], 2);
    if (is_null($rate)) {
        $sms = sprintf(NEW_TRANSACTION, $product, $buyer, $distance, $tran_id, $tran_id, $tran_id);
    } else {
        $sms = sprintf(NEW_TRANSACTION_PROPOSED_RATE, $product, $buyer, $distance, $rate, $tran_id, $tran_id, $tran_id);
    }
    sendSms($seller, $sms);
}
function sendNewProductMessage($conn, $product_id) {
    $query = "SELECT mobile FROM advertisements WHERE ad_id=$product_id";
    $result = mysqli_query($conn, $query);
    if ($row = mysqli_fetch_assoc($result)) {
        sendSms($row["mobile"], sprintf(SUCCESS_AD_POSTED, $product_id));
    }
}
function sendProductStatusChangedMessage($conn, $tran_id) {
    $query = "SELECT ad.name,ad.mobile AS seller,t.mobile AS buyer,t.status
    FROM transactions t
    INNER JOIN advertisements ad ON ad.ad_id=t.ad_id
    WHERE transaction_id=$tran_id;";
    $result = mysqli_query($conn, $query);
    if ($row = mysqli_fetch_assoc($result)) {
        if ($row['status'] == ACCEPTED) {
            sendSms($row["buyer"], sprintf(TRANSACTION_ACCEPTED, $row["name"], $row["seller"]));
        } elseif ($row['status'] == REJECTED) {
            sendSms($row["buyer"], sprintf(TRANSACTION_REJECTED), $row["name"]);
        }
    }
}
?>