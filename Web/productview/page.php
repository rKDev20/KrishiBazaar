<?php
    session_start();
    $pageno = $_POST['pageno'];
    $no_of_records_per_page = 6;
    $offset = ($pageno-1) * $no_of_records_per_page;
    include("../connection.php");
    // if(isset($_POST['category']))
    // {
    //   $sql = "SELECT * FROM advertisements WHERE category = ".$_POST['category']." LIMIT $offset, $no_of_records_per_page";
    // }
    // else {
    //   $sql = "SELECT * FROM advertisements LIMIT $offset, $no_of_records_per_page";
    // }
    $sql = $_SESSION['query']." LIMIT $offset, $no_of_records_per_page";
    //echo $sql;
    $res_data = mysqli_query($conn,$sql);
    while($products = mysqli_fetch_array($res_data)){
        //echo '<div>Demo'.$row["id"].'</div>';
        echo '<div class="col-md-4 product-grid">
  				<div class="image">
  					<a href="#">
  						<img src="images/ab.jpg" class="w-100">
  					</a>
  				    </div>
        				<h5 class="text-center">'.$products['name'].'</h5>
                        <p class="text-left">'.$products['rate'].'</p>
                        <p class="text-left">Distance</p>
                        <p class="text-left">'.$products['quantity'].'</p>
        				<a href="productdescription/productDescription.php?id='.$products['ad_id'].'" class="btn buy">View Details</a>
  			</div>';
    }
?>
