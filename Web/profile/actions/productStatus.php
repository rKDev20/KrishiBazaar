<?php echo "this is productStatus.php. Add id =".$_POST['ad_id']."Action code =".$_POST['action']."\r\n";
include('../../connection.php');
session_start();
if(!isset($_SESSION['mobile']))
  header('location:../../index.php');
if ($_POST['action'] == 1) {
  // Enable the add. set add status to 1
  $qry = "UPDATE advertisements SET status = 1 WHERE ad_id = ".$_POST['ad_id']." AND mobile =".$_SESSION['mobile'];
  echo $qry;
  $q = mysqli_query($conn,$qry);
}
elseif ($_POST['action'] == 0) {
  // Disable the add. set add status to 0
  $qry = "UPDATE advertisements SET status = 0 WHERE ad_id = ".$_POST['ad_id']." AND mobile =".$_SESSION['mobile'];
  echo $qry;
  $q = mysqli_query($conn,$qry);
}
header('location:../profile.php');
?>
