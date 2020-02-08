<?php
//DONE
include '../db.php';
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
$query="UPDATE transactions t INNER JOIN authorisation a ON t.mobile = a.mobile SET t.status=".$status." WHERE a.token='".$token."' AND transaction_id = ".$tran_id.";";
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