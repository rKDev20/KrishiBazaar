<?php
include_once '../util/db.php';
include_once '../util/utils.php';
//DONE
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
define('NOT_AUTHORISED', 1);
define('PRODUCT_NOT_FOUND', 2);

$json = file_get_contents('php://input');
$params = json_decode($json, true);
if (!(isset($params["token"]) && $params["product_id"])) {
    error(WRONG_PARAMETERS);
}

$token = $params["token"];
$product_id = $params["product_id"];
if ($token === "" || !is_numeric($product_id)) {
    error();
}

if (isset($params["latitude"]) && $params["longitude"]) {
    $latitude = $params["latitude"];
    $longitude = $params["longitude"];
} else {
    $latitude = "NULL";
    $longitude = "NULL";
}
$query = "SELECT ad.* ,latitude,longitude,cat.name AS cat,sub.name AS sub
FROM advertisements ad
LEFT JOIN categories cat ON cat.category_id=ad.category
LEFT JOIN sub_categories sub ON sub.sub_id = ad.sub_category
INNER JOIN authorisation au ON ad.mobile=au.mobile
INNER JOIN pincode pin ON ad.pincode=pin.pincode
WHERE ad_id=" . $product_id . " AND token='" . $token . "';";
// echo $query;
$result = mysqli_query($conn, $query);
//SELF UPLOADED PRODUCT
if ($row = mysqli_fetch_assoc($result)) {
    if ($row["status"] == INACTIVE) {
        $status = DELETED;
    } else {
        $status = OWNED;
    }

    $mobile = $row["mobile"];
    $sellerLatitude = $row["latitude"];
    $sellerLongitude = $row["longitude"];
    $queryTran =
        "SELECT transaction_id,name,clock,status,t.pincode,proposed_rate,latitude,longitude
	FROM transactions t
	INNER JOIN users u ON u.mobile=t.mobile
	INNER JOIN pincode pin ON t.pincode=pin.pincode
	WHERE ad_id=$product_id
	ORDER BY t.clock ASC";
    $resultTran = mysqli_query($conn, $queryTran);
    $buyers = array();
    while ($rowNew = mysqli_fetch_assoc($resultTran)) {
        $tmp = array('tran_id' => (int)$rowNew["transaction_id"],
            'name' => $rowNew["name"],
            'timestamp' => getTimeInMillis($rowNew["clock"]),
            'status' => (int)$rowNew["status"],
            'distance' => getDistance($sellerLatitude, $sellerLongitude, $rowNew["latitude"], $rowNew["longitude"]),
        );
        if (!is_null($rowNew["proposed_rate"])) {
            $tmp["price"] = (float)$rowNew["proposed_rate"];
        }

        array_push($buyers, $tmp);
    }
    //TODO add image_url
    $res = array(
        'name' => $row["name"],
        'quantity' => (float)$row["quantity"],
        'price' => (float)$row["rate"],
        'description' => $row["description"],
        'pincode' => (int)$row["pincode"],
        'latitude' => $row["latitude"],
        'longitude' => $row["longitude"],
        'status' => $status,
        'buyers' => $buyers,
    );
    if (!is_null($row["cat"])) {
        $res['image_url'] = 'product_images/' . $row["cat"] . ".png";
        $res["category"] = $row["cat"];
    }

    if (!is_null($row["sub"])) {
        $res["sub"] = $row["sub"];
    }

    success($res);
}
//OTHERS PRODUCT
else {
    //CHECK IF I HAVE MADE TRANSACTION TO THIS PRODUCT
    $query =
        "SELECT ad.name,ad.quantity,ad.rate,ad.description,ad.pincode AS apincode,pin1.latitude AS alatitude,pin1.longitude AS alongitude,pin2.latitude AS tlatitude,pin2.longitude AS tlongitude, ad.mobile,t.status,ad.status AS astatus ,cat.name AS cat,sub.name AS sub
		FROM transactions t
		INNER JOIN authorisation au ON au.mobile=t.mobile
		INNER JOIN advertisements ad ON ad.ad_id=t.ad_id
        LEFT JOIN categories cat ON cat.category_id = ad.category
        LEFT JOIN sub_categories sub ON sub.sub_id = ad.sub_category
		INNER JOIN pincode pin1 ON ad.pincode= pin1.pincode
		INNER JOIN pincode pin2 ON ad.pincode= pin2.pincode
        WHERE ad.ad_id=" . $product_id . " AND au.token='" . $token . "';";
    $result = mysqli_query($conn, $query);
    if ($row = mysqli_fetch_assoc($result)) {
        //YES I HAVE MADE TRANSACTION
        if ($row["astatus"] == ACTIVE) {
            $status = $row["status"];
        } else {
            $status = SOLD;
        }

        $latlng = getCoordinates($row["apincode"]);
        $res = array(
            'name' => $row["name"],
            'quantity' => (float)$row["quantity"],
            'price' => (float)$row["rate"],
            'description' => $row["description"],
            'latitude' => $row["alatitude"],
            'longitude' => $row["alongitude"],
            'distance' => getDistance($row["alatitude"], $row["alongitude"], $row["tlatitude"], $row["tlongitude"]),
            'pincode' => (int)$row["apincode"],
            'status' => (int)$status,
        );
        if (!is_null($row["cat"])) {
            $res['image_url'] = 'product_images/' . $row["cat"] . ".png";
            $res["category"] = $row["cat"];
        }

        if (!is_null($row["sub"])) {
            $res["sub"] = $row["sub"];
        }

        if ($status == ACCEPTED) {
            $res["farmer_mobile"] = $row["mobile"];
        }
        success($res);
    } else {
        //NO THIS PRODUCT IS FREE TO MAKE TRANSACTION
        $query =
            "SELECT * ,cat.name AS cat,sub.name AS sub
		FROM advertisements ad
        LEFT JOIN categories cat ON cat.category_id=ad.category
        LEFT JOIN sub_categories sub ON sub.sub_id = ad.sub_category
		INNER JOIN pincode pin ON pin.pincode=ad.pincode
		WHERE ad_id=" . $product_id . ";";
        $result = mysqli_query($conn, $query);
        if ($row = mysqli_fetch_assoc($result)) {
            if ($row["status"] == ACTIVE) {
                $status = AVAILABLE;
            } else {
                error(NOT_AUTHORISED);
            }

            $res = array(
                'name' => $row["name"],
                'quantity' => (float)$row["quantity"],
                'price' => (float)$row["rate"],
                'description' => $row["description"],
                'pincode' => (int)$row["pincode"],
                'latitude' => $row["latitude"],
                'longitude' => $row["longitude"],
                'status' => (int)$status,
            );
            if (!is_null($row["cat"])) {
                $res["category"] = $row["cat"];
                $res['image_url'] = 'product_images/' . $row["cat"] . '.png';
            }

            if (!is_null($row["sub"])) {
                $res["sub"] = $row["sub"];
            }

            if ($latitude !== "NULL") {
                $res['distance'] = getDistance($row["latitude"], $row["longitude"], $latitude, $longitude);
            }

            success($res);
        } else {
            error(PRODUCT_NOT_FOUND);
        }
    }
}

function success($res) {
    $result = array("success" => $res);
    echo json_encode($result);
    die();
}

function error($error_text) {
    $result = array("error" => $error_text);
    echo json_encode($result);
    die();
}
?>