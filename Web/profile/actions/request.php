<?php echo "this is request.php. Add id =".$_POST['ad_id']."Action code =".$_POST['action']."transaction_id = ".$_POST['tid']."\r\n";
include('../../connection.php');
session_start();
if(!isset($_SESSION['mobile']))
  header('location:../../index.php');
if($_POST['action'] == 1) {
  // accept the request. ie. set transaction status to 3 AND Disable the add. set add status to 0
  // accept the request
  $qry = "UPDATE transactions SET status = 3 WHERE ad_id = ".$_POST['ad_id']." AND transaction_id = ".$_POST['tid'];
  $q = mysqli_query($conn,$qry);
  echo $qry;
  //disable the add.
  $qry = "UPDATE advertisements SET status = 1 WHERE ad_id = ".$_POST['ad_id']." AND mobile =".$_SESSION['mobile'];
  $q = mysqli_query($conn,$qry);
  echo $qry;
}
elseif ($_POST['action'] == 0) {
  // reject the request. ie. set transaction status to 4
  $qry = "UPDATE transactions SET status = 4 WHERE ad_id = ".$_POST['ad_id']." AND transaction_id = ".$_POST['tid'];
  $q = mysqli_query($conn,$qry);
  echo $qry;
}
header('location:../profile.php');
?>
