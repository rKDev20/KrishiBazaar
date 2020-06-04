<!DOCTYPE html>
<?php
include('../../connection.php');
session_start();
$id = $_GET['id'];
$qry = "SELECT * FROM advertisements WHERE ad_id = $id";
$q = mysqli_query($conn,$qry);
$product  = mysqli_fetch_assoc($q);

$qry2 = "SELECT * FROM transactions WHERE ad_id = $id AND mobile = ".$_SESSION['mobile'];
$q2 = mysqli_query($conn,$qry2);
?>
<html>
    <head>
        <title>Product Description</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet"
        href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
        <link rel="stylesheet" href="style.css" type="text/css">
        <link rel="stylesheet" href="../style.css">
        <!-- tags from index.php -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
        <link href="https://fonts.googleapis.com/css?family=Nunito&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="../../style.css">
        <link href="https://fonts.googleapis.com/css?family=Poppins&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Raleway&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Uncial+Antiqua&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="../../myjs.js?version=2"></script>

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
      <a class="navbar-brand font-weight-bold text-white" href="../../index.php">KRISHI BAZAR</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav ml-auto text-uppercase">

            <li class="nav-item active">
            <a class="nav-link" href="../../index.php">Home<span class="sr-only">(current)</span></a>
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
            <a class="dropdown-item" href="../../profile/profile.php">PROFILE</a>
            <a class="dropdown-item" href="../../logout.php">LOGOUT</a>
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

      <!-- Product Description -->
  <div class="container" style="height:600px;">
   <div class="wrapper">
    <div class="right">
        <div class="info">
            <h3>Information</h3>
            <h4 class="text-center"><?php echo $product['name']?></h4>
            <hr>
            <div class="info_data">
                 <div class="data">
                   <h4>Quantity</h4>
                    <p><?php echo $product['quantity']?></p>
              </div>
                <div class="data">
                   <h4>Price</h4>
                    <p><?php echo $product['rate']?>/Kg</p>
                </div>
                <div class="data">
                   <h4>Pincode</h4>
                    <p><?php echo $product['pincode']?></p>
                </div>
        </div>

      <div class="projects">
            <h3>Description</h3>
          <p><?php echo $product['description']?></p>
        </div>

        <?php
          //chech if user is loged in or not, if log in, give option to buy by giving a price. else ask to login.
          //$_SESSION['mobile'] = 1234;
          //echo $qry2;
          //echo mysqli_num_rows($q2);
          if (isset($_SESSION['mobile'])) {
            if(mysqli_num_rows($q2)==0)
            {
         ?>
           <form class="form-inline" method="post" action="bid.php">
           <div class="form-group mb-2">
             <label for="staticEmail2" class="sr-only">Proposed Rate</label>
             <input type="text" readonly class="form-control-plaintext" id="staticEmail2" value="Your Offer Price">
           </div>
           <div class="form-group mx-sm-3 mb-2">
             <label for="inputPassword2" class="sr-only">Price</label>
             <input type="number" class="form-control" id="price" placeholder="Price" name="price">
             <input type="number" name="ad_id" value="<?php echo $product['ad_id']?>" hidden>
             <input type="hidden" class="redirect" name = "redirect" value="productDescription.php?id=<?php echo $id;?>">
           </div>
           <button type="submit" class="btn btn-primary mb-2">Confirm!</button>
          </form>
          <?php
        }
        else {
          ?>
          <p>-Request already made, check profile section for order status.-</p>
          <?php
        }
          }
          else
          {
           ?>
           <p>-Please log in to make a bid.-</p>
           <?php
         }
          ?>
        </div>
      </div>
    </div>
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
            <form id="form" action="../../login.php" method="post">
              <div class="form-group">
                <label > Enter Mobile Number</label>
                <input type="tel" class="form-control" id="mobile" name="mobile" aria-describedby="emailHelp" placeholder="Enter Number">
                <div id="otpForm" hidden>
                <label >Enter OTP</label>
                <input  type="number" class="form-control" id="otp" name="otp" id="exampleInputPassword1" placeholder="OTP">
                <br>
                <input type="hidden" class="redirect" name = "redirect" value="productdescription/productDescription.php?id=<?php echo $id;?>">
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
                        <img src="../../google-play-badge.png" height="90" width="195">
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
