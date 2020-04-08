<?php
include_once '../util/db.php';
include_once '../util/utils.php';
define('ACTIVE', 1);
define('INACTIVE', 0);

define('WRONG_PARAMETERS', 0);

$json = file_get_contents('php://input');
$params = json_decode($json, true);
if (!(isset($params["token"]))) {
    error(WRONG_PARAMETERS);
}
if (!(isset($params["pageOffset"]) && isset($params["size"]))) {
    error(0);
}
$pageOffset = $params["pageOffset"];
$size = $params["size"];
$token = $params["token"];
if (!is_integer($pageOffset) || !is_integer($size)) {
    error(1);
}
$start = $size * ($pageOffset - 1);
$end = $start + $size;
if (isset($params["search"])) {
    $search = $params["search"];
    if (isset($params["latitude"]) && isset($params["longitude"]) && is_numeric($params["latitude"]) && is_numeric($params["longitude"])) {
        $query = getSearchByLocation($token, $search, $start, $end, $params["latitude"], $params[
            "longitude"]);
    } else {
        $query = getSearch($token, $search, $start, $end);
    }
} else {
    if (isset($params["latitude"]) && isset($params["longitude"]) && is_numeric($params["latitude"]) && is_numeric($params["longitude"])) {
        $query = getTrendingByLocation($token, $start, $end, $params["latitude"], $params[
            "longitude"]);
    } else {
        $query = getTrending($token, $start, $end);
    }
}

$result = mysqli_query($conn, $query);
$resultArray = array();
while ($row = mysqli_fetch_assoc($result)) {
    $res = array(
        'product_id' => $row["ad_id"],
        'name' => $row["name"],
        'quantity' => $row["quantity"],
        'price' => $row["rate"],
        'description' => $row["description"]);
    if (isset($row["distance"])) {
        $res['distance'] = $row["distance"];
    }
    if (isset($row["image"]) && !is_null($row["image"])) {
        $res['image_url'] = 'icons/' . $row["image"] . ".png";
    }
    array_push($resultArray, $res);
}
success($resultArray);

function getSearchByLocation($token, $search, $start, $end, $latitude, $longitude) {
    $searchTerms = explode(' ', $search);
    $searchTermBits = array();
    foreach ($searchTerms as $term) {
        $term = trim($term);
        if (!empty($term)) {
            $searchTermBits[] = "CONCAT(ad.name,' ',IFNULL(cat.name,''),' ',IFNULL(sub.name,'')) LIKE '%$term%'";
        }
    }
    $query = "SELECT ad.* , LAT_LONG_DISTANCE(pin.latitude,pin.longitude,$latitude,$longitude) AS distance, cat.name AS image
    FROM advertisements ad
    INNER JOIN categories cat ON cat.category_id=ad.category
    INNER JOIN sub_categories sub ON sub.sub_id=ad.sub_category
    INNER JOIN pincode pin ON pin.pincode=ad.pincode
    WHERE ad.status= " . ACTIVE . " AND ad.mobile!=(SELECT mobile FROM authorisation WHERE token = '$token') AND ";
    $query = $query . implode(' AND ', $searchTermBits);
    $query = $query . " ORDER BY distance LIMIT $start,$end;";
    return $query;
}

function getSearch($token, $search, $start, $end) {
    $searchTerms = explode(' ', $search);
    $searchTermBits = array();
    foreach ($searchTerms as $term) {
        $term = trim($term);
        if (!empty($term)) {
            $searchTermBits[] = "CONCAT(ad.name,' ',IFNULL(cat.name,''),' ',IFNULL(sub.name,'')) LIKE '%$term%'";
        }
    }
    $query = "SELECT ad.*, cat.name AS image
    FROM advertisements ad
    INNER JOIN categories cat ON cat.category_id=ad.category
    INNER JOIN sub_categories sub ON sub.sub_id=ad.sub_category
    WHERE ad.status= " . ACTIVE . " AND ad.mobile!=(SELECT mobile FROM authorisation WHERE token = '$token') AND ";
    $query = $query . implode(' AND ', $searchTermBits);
    $query = $query . " ORDER BY ad.clock LIMIT $start,$end";
    return $query;
}

function getTrending($token, $start, $end) {
    $query = "SELECT ad.*, cat.name AS image
    FROM advertisements ad
    INNER JOIN categories cat ON cat.category_id=ad.category
    WHERE ad.status= " . ACTIVE . " AND ad.mobile!=(SELECT mobile FROM authorisation WHERE token = '$token')
    ORDER BY clock
    LIMIT $start,$end";
    return $query;
}
function getTrendingByLocation($token, $start, $end, $latitude, $longitude) {
    $query = "SELECT ad.* , LAT_LONG_DISTANCE(pin.latitude,pin.longitude,$latitude,$longitude) AS distance, cat.name AS image
    FROM advertisements ad
    INNER JOIN categories cat ON cat.category_id=ad.category
    INNER JOIN pincode pin ON pin.pincode=ad.pincode
    WHERE ad.status= " . ACTIVE . " AND ad.mobile!=(SELECT mobile FROM authorisation WHERE token = '$token')
    ORDER BY distance
    LIMIT $start,$end";
    return $query;
}

function success($array) {
    echo json_encode(array('success' => $array));
    die();
}
function error($error_text) {
    echo json_encode(array("error" => $error_text));
    die();
}
?>