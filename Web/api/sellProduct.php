<?php
//DONE
include_once '../util/db.php';
include_once '../util/utils.php';
include_once '../sms/strings.php';
include_once '../util/notify.php';

define('WRONG_PARAMETER', -1);
define('WRONG_PINCODE', -2);
$json = file_get_contents('php://input');
$params = json_decode($json, true);
if (!(isset($params["token"]) && isset($params["category"]) && isset($params["sub"]) && isset($params["name"]) && isset($params["quantity"]) && isset($params["price"]) && isset($params["description"]) && isset($params["pincode"]))) {
    error(WRONG_PARAMETER);
}

$token = $params["token"]; //not null string
$category = $params["category"]; //not null int
$subCategory = $params["sub"]; //not null int
$name = $params["name"]; //not null string
$quantity = $params["quantity"]; //not null number
$price = $params["price"]; //not null number
$description = $params["description"]; //not null string
$pincode = $params["pincode"]; //not null pincode

if ($token === "" || !is_numeric($category) || !is_numeric($subCategory) || $name === "" || !is_numeric($quantity) || !is_numeric($price) || $description === "") {
    error(WRONG_PARAMETER);
}
if (!checkPincode($conn, $pincode)) {
    error(WRONG_PINCODE);
}
if ($subCategory == -1) {
    $subCategory = "NULL";
}

$query = "INSERT INTO advertisements(mobile, category,sub_category, name, quantity, rate, description, pincode) SELECT mobile, " . $category . ", " . $subCategory . ", '" . $name . "', " . $quantity . ", " . $price . ", '" . $description . "', " . $pincode . " FROM authorisation WHERE token='" . $token . "';";
mysqli_query($conn, $query);
if (mysqli_error($conn)) {
    error();
}

if (mysqli_affected_rows($conn) == 1) {
    success($conn, mysqli_insert_id($conn));
} else {
    error();
}

function success($conn, $product_id) {
    sendNewProductMessage($conn, $product_id);
    echo json_encode(array('status' => $product_id));
}
function error($err_code) {
    echo json_encode(array("status" => $err_code));
    die();
}
?>