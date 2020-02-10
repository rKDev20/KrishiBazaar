<?php
//DONE
include '../db.php';
define('PENDING', 2);
define('ACCEPTED', 3);
define('REJECTED', 4);

$json = file_get_contents('php://input');
$params = json_decode($json,true);
if(!(isset($params["token"])&&isset($params["tran_id"])&&isset($params["status"])))
	error();
$token=$params["token"];
$tran_id=$params["tran_id"];
$status=$params["status"];
if ($token===""||!is_numeric($tran_id)||!($status==ACCEPTED||$status==REJECTED))
	error();
$query="UPDATE transactions t INNER JOIN advertisements ad ON t.ad_id = ad.ad_id INNER JOIN authorisation a ON a.mobile=ad.mobile SET t.status=".$status." WHERE a.token='".$token."' AND transaction_id = ".$tran_id." AND t.status=".PENDING.";";
mysqli_query($conn,$query);
if (mysqli_error($conn))
	error();
if (mysqli_affected_rows($conn)==1)
	success();
else error();

function success(){
	echo json_encode(array("status"=>1));
	die();
}
function error(){
	echo json_encode(array("status"=>0));
	die();
}
?>

SELECT * FROM transactions t INNER JOIN advertisements ad ON t.ad_id = ad.ad_id INNER JOIN authorisation a ON a.mobile=ad.mobile WHERE a.token='6f7b2f9e255c6cfc865d974d447b9bec' AND transaction_id = 1;