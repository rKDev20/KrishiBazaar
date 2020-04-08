<?php
//DONE
include_once '../util/db.php';
include_once '../util/utils.php';

define('OWNED', 0);
define('AVAILABLE', 1);
define('PENDING', 2);
define('ACCEPTED', 3);
define('REJECTED', 4);
define('SOLD', 5);
define('DELETED', 6);

define('ACTIVE', 1);
define('INACTIVE', 0);

define('WRONG_PARAMETERS', 0);

$json = file_get_contents('php://input');
$params = json_decode($json, true);

if (!(isset($params["token"]) && isset($params["pageOffset"]) && isset($params["size"]))) {
    error(WRONG_PARAMETERS);
}

$token = $params["token"];
$pageOffset = $params["pageOffset"];
$size = $params["size"];
if ($token === "" || !is_numeric($pageOffset) || !is_numeric($size)) {
    error(WRONG_PARAMETERS);
}

$start = $size * ($pageOffset - 1);
$end = $start + $size;
$query =
    "SELECT
NULL AS transaction_id , NULL AS tad_id, NULL as proposed_rate , NULL AS tmobile , ad.clock AS tclock, NULL AS tpincode, NULL AS tstatus, NULL AS tlatitude, NULL AS tlongitude, ad . *, pin.latitude, pin.longitude
FROM advertisements ad
INNER JOIN pincode pin ON pin.pincode=ad.pincode
INNER JOIN authorisation au ON ad.mobile = au.mobile
WHERE token = '" . $token . "'
UNION
SELECT t.* , pin1.latitude AS tlatitude, pin1.longitude AS tlongitude , ad.* , pin2.latitude, pin2.longitude
FROM transactions t
INNER JOIN pincode pin1 ON t.pincode=pin1.pincode
INNER JOIN advertisements ad ON ad.ad_id = t.ad_id
INNER JOIN pincode pin2 ON pin2.pincode=ad.pincode
INNER JOIN authorisation au ON t.mobile = au.mobile
WHERE token = '" . $token . "'
ORDER BY tclock DESC
LIMIT " . $start . "," . $end . ";";
$result = mysqli_query($conn, $query);
$data = array();
echo mysqli_error($conn);
while ($row = mysqli_fetch_assoc($result)) {
    $res = array(
        'product_id' => (int)$row["ad_id"],
        'name' => $row["name"],
        'quantity' => (float)$row["quantity"],
        'price' => (float)$row["rate"],
        'timestamp' => getTimeInMillis($row["tclock"]));
    if (is_null($row["transaction_id"])) {
        if ($row["status"] == ACTIVE) {
            $res["status"] = OWNED;
        } else {
            $res["status"] = DELETED;
        }

    } else {
        $res['distance'] = getDistance($row["latitude"], $row["longitude"], $row["tlatitude"], $row["tlongitude"]);
        if ($row["status"] == INACTIVE) {
            $res["status"] = SOLD;
        } else {
            $res["status"] = $row["tstatus"];
        }
    }
    array_push($data, $res);
}
success($data);

function success($data) {
    $result = array('success' => $data);
    echo json_encode($result);
    die();
}
function error($error_text) {
    $result = array("error" => $error_text);
    echo json_encode($result);
    die();
}
?>