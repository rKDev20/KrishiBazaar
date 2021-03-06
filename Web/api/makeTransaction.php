<?php
//DONE
include_once '../util/db.php';
include_once '../util/utils.php';
include_once '../util/notify.php';
include_once '../sms/strings.php';

define('PENDING', 2);

define('WRONG_PARAMETER', -1);
define('WRONG_PINCODE', -2);

$json = file_get_contents('php://input');
$params = json_decode($json, true);
if (!(isset($params["token"]) && isset($params["product_id"]) && isset($params["pincode"]))) {
    error(WRONG_PARAMETER);
}

if (isset($params["price"]) && is_numeric($params["price"])) {
    $price = $params["price"];
} else {
    $price = "NULL";
}

$token = $params["token"];
$product_id = $params["product_id"];
$pincode = $params["pincode"];
if ($token === "" || !is_numeric($product_id)) {
    error(WRONG_PARAMETER);
}
if (!checkPincode($conn, $pincode)) {
    error(WRONG_PINCODE);
}
$query = "SELECT ad.mobile FROM advertisements ad INNER JOIN authorisation au ON ad.mobile=au.mobile WHERE au.token='" . $token . "' AND ad_id=" . $product_id . ";";
$result = mysqli_query($conn, $query);

if (mysqli_error($conn) || mysqli_affected_rows($conn) == 1) {
    error(WRONG_PARAMETER);
}

$query = "INSERT INTO transactions(ad_id, proposed_rate, mobile, pincode, status) SELECT " . $product_id . ", " . $price . ", mobile, " . $pincode . ", " . PENDING . " FROM authorisation WHERE token='" . $token . "';";
mysqli_query($conn, $query);
if (mysqli_error($conn)) {
    error(WRONG_PARAMETER);
}

if (mysqli_affected_rows($conn) == 1) {
    $tran_id = mysqli_insert_id($conn);
    success($conn, $tran_id);
} else {
    error(WRONG_PARAMETER);
}

function success($conn, $tran_id) {
    sendNewRequestMessage($conn, $tran_id);
    echo json_encode(array("status" => 1));
    die();
}

function error($err_code) {
    echo json_encode(array("status" => $err_code));
    die();
}
?>