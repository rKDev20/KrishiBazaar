<?php
session_start();
include("../connection.php");
if(isset($_GET['category']))
{
  $catID = $_GET['category'];
  $qry = "SELECT * FROM advertisements WHERE category = ".$catID." AND status = 1 LIMIT 6";
  $_SESSION['query'] = "SELECT * FROM advertisements WHERE category = ".$_GET['category']." AND status =1";
}
else if (isset($_SESSION['searchQuery'])) {
  $qry = $_SESSION['searchQuery']." LIMIT 6";
  $_SESSION['query'] = $_SESSION['searchQuery'];
  unset($_SESSION['searchQuery']);
}
else {
  $qry = "SELECT * FROM advertisements WHERE status = 1 LIMIT 6";
  $_SESSION['query'] = "SELECT * FROM advertisements WHERE status = 1 ";
}
//echo $qry;
$row = mysqli_query($conn,$qry);
?>
<!DOCTYPE html>
<html>
<head>
        <title>Product view</title>
        <!-- tags from index.php -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
        <link href="https://fonts.googleapis.com/css?family=Nunito&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="../style.css">
        <link href="https://fonts.googleapis.com/css?family=Poppins&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Raleway&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Uncial+Antiqua&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="../myjs.js"></script>

          <!-- default tags of productview.php -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="style.css" type="text/css">
        <!-- JQuery CDN -->
        <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>

        <!-- Inview Js (jquery.inview.js) -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.inview/1.0.0/jquery.inview.js"></script>
</head>
<body>

  <!-- Nav bar -->
  <div class="container">
  <nav class="navbar navbar-expand-lg bg-dark text-white fixed-top">
  <div class="container text-uppercase p-2">
  <a class="navbar-brand font-weight-bold text-white" href="../index.php">KRISHI BAZAR</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
  <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav ml-auto text-uppercase">

        <li class="nav-item active">
        <a class="nav-link" href="../index.php">Home<span class="sr-only">(current)</span></a>
        </li>
      <!-- User name display if user is loged in -->
      <?php
        if(isset($_SESSION['id']))
        {
      ?>
      <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
        <?php echo $_SESSION['user_name']; ?>
      </a>
      <div class="dropdown-menu bg-dark" aria-labelledby="navbarDropdown">
        <a class="dropdown-item" href="../profile/profile.php">PROFILE</a>
        <a class="dropdown-item" href="../logout.php">LOGOUT</a>
      </div>
    </li>
      <?php
        }
        if(!isset($_SESSION['id']))
        {
      ?>

      <li class="nav-item">
      <a class="nav-link" href="#"  data-target="#mymodel" data-toggle="modal">LOGIN</a>
      </li>
      <li class="nav-item">
      <a class="nav-link" href="../signup/signup.php">SIGNUP</a>
      </li>
      <?php
        }
      ?>
    </ul>
  </div>
      </div>
  </nav>
</div>
  <div class="container">
	<h1 class="text-center">PRODUCT VIEW</h1>
		<div class="row" id = "response">
			<!-- Product  -->

        <?php
          while($products = mysqli_fetch_assoc($row))
          {
            if(!(isset($_SESSION['mobile'])) || $products['mobile'] != $_SESSION['mobile'])
            {
        ?>
  			<div class="col-md-4 product-grid">
  				<div class="image">
  					<a href="#">
  						<img src="images/ab.jpg" class="w-100">
  					</a>
  				    </div>
        				<h5 class="text-center"><?php echo $products['name']; ?></h5>
                        <p class="text-left">Rate&nbsp;&emsp;&emsp;:  <?php echo $products['rate']; ?></p>
                        <p class="text-left">PinCode&ensp;: <?php echo $products['pincode']; ?></p>
                        <p class="text-left">Quantity&nbsp;: <?php echo $products['quantity']; ?></p>
        				<a href="productdescription/productDescription.php?id=<?php echo $products['ad_id'];?>" class="btn buy">View Details</a>
  			</div>
        <?php
            }
          }
        ?>
			</div>
      <?php
        if(mysqli_num_rows($row) == 6)
        {
      ?>
      <input type="hidden" id="pageno" value="1">
      <center><img id="loader" class="loader" src="491.gif"></center>
      <?php
        }
      ?>
    </div>


    <!--Login Modal -->
    <div class="modal" tabindex="-1" role="dialog" id="mymodel">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Login</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
        <form id="form" action="../login.php" method="post">
          <div class="form-group">
            <label > Enter Mobile Number</label>
            <input type="tel" class="form-control" id="mobile" name="mobile" aria-describedby="emailHelp" placeholder="Enter Number">
            <div id="otpForm" hidden>
            <label >Enter OTP</label>
            <input  type="number" class="form-control" id="otp" name="otp" id="exampleInputPassword1" placeholder="OTP">
            <br>
            <input type="hidden" class="redirect" name = "redirect" value="productview/productview.php">
            <center><input type="button" onclick="loginButton()" id="login" class="btn btn-primary" data-dismiss="modal" value="Login"/></center>
            </div>
            <br>
            <div id = "sendotp">
              <center><button type="button" id = "btnid" onclick="buttonclick()" class="btn btn-primary">Send OTP</button></center>
            </div>
        </div>
        </form>
        </div>
      </div>
    </div>
  </div>


    <script>
    var isLoading = 0;
     $(document).ready(function(){
       $(window).scroll(function() {
         if(!$("#loader").is(":hidden")){
              var top_of_element = $("#loader").offset().top;
              var bottom_of_element = $("#loader").offset().top + $("#loader").outerHeight();
              var bottom_of_screen = $(window).scrollTop() + $(window).innerHeight();
              var top_of_screen = $(window).scrollTop();
              if ((bottom_of_screen > top_of_element) && (top_of_screen < bottom_of_element)){
                  load();
              }
        }
      });
    });
    function load(){
      if (isLoading==0) {
            console.log("loading");
            isLoading=1;
            var nextPage = parseInt($('#pageno').val())+1;
            $.ajax({
                type: 'POST',
                url: 'page.php',
                data: { pageno: nextPage, },
                success: function(data){
                    isLoading=0;
                    if(data != ''){
                      console.log("loading success");
                        $('#response').append(data);
                        $('#pageno').val(nextPage);
                    } else {
                      console.log("loading null");
                        $("#loader").hide();
                    }
                }
            }).fail( function(xhr, textStatus, errorThrown) {
                console.log("failed to load");
                isLoading=0;
            });
        }
      }
         //     $('img').on('inview', function(event, isInView) {
         //         console.log("here");
         //         if (isInView) {
         //           //alert ("inview");
         //
         //         }
         //     });
         // });
     </script>

  <!-- FOOTER -->
  <footer>
      <div class="footer-top">
          <div class="container">
              <div class="row">
                  <div class="col-md-3 col-sm-6 col-xs-12 segment-one">
                      <h3>KRISHI BAZAR</h3>
                      <p class="av" text-white>"Adding Green to your Life"</p>
                  </div>
                  <div class="col-md-3 col-sm-6 col-xs-12 segment-two">
                    <h2>Get on GooglePlay</h2>
                    <img src="../google-play-badge.png" height="90" width="195">
                  </div>
                  <div class="col-md-3 col-sm-6 col-xs-12 segment-three">
                      <h2> Follow us</h2>
                      <p class="av"> Please follow us on our social media profile inorder to keep updated.</p>
                      <a href="#"><i class="fa fa-facebook"></i></a>
                      <a href="#"><i class="fa fa-twitter"></i></a>
                      <a href="#"><i class="fa fa-linkedin"></i></a>
                      <a href="#"><i class="fa fa-pinterest"></i></a>
                  </div>
                  <div class="col-md-3 col-sm-6 col-xs-12 segment-four">
                      <h2>Our Newsletter</h2>
                      <p class="av">Subscribe to our newsletter to get latest updates and offers</p>
                      <form action="">
                          <input type="email">
                          <input type="submit" value="Subscribe">
                      </form>
                  </div>
                  </div>
            </div>
      </div>
      <p class="footer-bottom-text">All Right reserved by &copy;Chandra-2020</p>
  </footer>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
</body>
</html>
