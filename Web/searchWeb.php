<?php
session_start();
include_once 'util/db.php';
include_once 'util/utils.php';

if(isset($_POST['pincode']))
  $pincode = $_POST['pincode'];
else
  $pincode = 0;
if(empty($_POST['q']))
{
  $_SESSION['message'] = "Search value missing.";
  header('location:index.php');
  exit();
}
$search = $_POST['q'];
if($pincode != 0)
{
  $coordinate=getCoordinates($pincode);
  if ($coordinate) {
    $_SESSION['searchQuery'] = getSearchByLocation($search,$coordinate["latitude"],$coordinate["longitude"]);
    echo $_SESSION['searchQuery'];
    header('location:productview/productview.php');
    exit();
  }
  else {
    $_SESSION['message'] = "Invalid Pincode";
    header('location:index.php');
    exit();
  }
}
else {
  $_SESSION['searchQuery'] = getSearch($search);
  echo $_SESSION['searchQuery'];
  header('location:productview/productview.php');
  exit();
}
function getSearchByLocation($search, $latitude, $longitude) {
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
      WHERE ad.status= 1 AND ";
      $query = $query . implode(' AND ', $searchTermBits);
    return $query;
}
function getSearch($search) {
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
      WHERE ad.status= 1 AND ";
      $query = $query . implode(' AND ', $searchTermBits);
      return $query;

}
?>
