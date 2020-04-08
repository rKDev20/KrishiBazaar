<?php

function getCurrentTimestamp() {
    return (new DateTime())->format('Y-m-d H:i:s');
}
function checkMobile($mobile) {
    return preg_match('/^\d{10}$/', $mobile);
}
function checkOtp($otp) {
    return preg_match('/^\d{6}$/', $otp);
}
function checkPincode($conn, $pincode) {
    if (preg_match('/^\d{6}$/', $pincode)) {
        $query = "SELECT latitude,longitude FROM pincode where pincode =" . $pincode . ";";
        $result = mysqli_query($conn, $query);
        if (mysqli_num_rows($result) == 1) {
            $res=mysqli_fetch_assoc($result);
            $res["pincode"] = $pincode;
            return $res; 
        } else {
            if ($result = getCoordinates($pincode)) {
                $query = "INSERT INTO pincode(pincode,latitude,longitude) VALUES (" . $pincode . "," . $result["latitude"] . "," . $result["longitude"] . ");";
                mysqli_query($conn, $query);
                $result["pincode"] = $pincode;
                return $result;
            } else {
                return false;
            }
        }
    } else {
        return false;
    }

}
function getTimeInMillis($time) {
    $givenTime = new DateTime($time);
    return ($givenTime->getTimestamp());
}
function getDistanceByPincode($pincode1, $pincode2) {
    $coordinates2 = getCoordinates($pincode2);
    return round(getDistanceByCoordinates($pincode1, $coordinates2["latitude"], $coordinates2["longitude"]),2);
}
function getDistanceByCoordinates($pincode1, $latitude, $longitude) {
    $coordinates1 = getCoordinates($pincode1);
    $long1 = deg2rad($coordinates1["longitude"]);
    $long2 = deg2rad($longitude);
    $lat1 = deg2rad($coordinates1["latitude"]);
    $lat2 = deg2rad($latitude);
    $dlong = $long2 - $long1;
    $dlati = $lat2 - $lat1;
    $val = pow(sin($dlati / 2), 2) + cos($lat1) * cos($lat2) * pow(sin($dlong / 2), 2);
    $res = 2 * asin(sqrt($val));
    $radius = 6371;
    return round(($res * $radius),2);
}
function getDistance($latitude1, $longitude1, $latitude2, $longitude2) {
    $long1 = deg2rad($longitude1);
    $long2 = deg2rad($longitude2);
    $lat1 = deg2rad($latitude1);
    $lat2 = deg2rad($latitude2);
    $dlong = $long2 - $long1;
    $dlati = $lat2 - $lat1;
    $val = pow(sin($dlati / 2), 2) + cos($lat1) * cos($lat2) * pow(sin($dlong / 2), 2);
    $res = 2 * asin(sqrt($val));
    $radius = 6371;
    return round(($res * $radius),2);
}
function getCoordinates($pincode) {
    $url = "http://dev.virtualearth.net/REST/v1/Locations/" . $pincode . "?maxResults=3&key=AuZ2njjdMZzeH1irPijjn6-Ex2GXdzc4R1ND61mj98qaQLeUfURW-jtVXnSg1Zgl";
    try {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($ch, CURLOPT_URL, $url);
        $result = json_decode(curl_exec($ch), true);
        if (isset($result["resourceSets"][0]["resources"][0]["name"]) &&
            isset($result["resourceSets"][0]["resources"][0]["point"]["coordinates"][0]) &&
            isset($result["resourceSets"][0]["resources"][0]["point"]["coordinates"][1])) {
            $resultJSON = array(
                "latitude" => $result["resourceSets"][0]["resources"][0]["point"]["coordinates"][0],
                "longitude" => $result["resourceSets"][0]["resources"][0]["point"]["coordinates"][1]);
            return $resultJSON;
        } else {
            return false;
        }

    } catch (Exception $e) {
        return false;
    }
}
?>