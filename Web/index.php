<!DOCTYPE html>
<?php
  session_start();
  include("connection.php");
  unset($_SESSION['query']);
  $qry = "SELECT * FROM categories";
  $row = mysqli_query($conn,$qry);
?>
<html>
    <head>
        <title>Krishi Bazar</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
        <link href="https://fonts.googleapis.com/css?family=Nunito&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="style.css">
        <link href="https://fonts.googleapis.com/css?family=Poppins&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Raleway&display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Uncial+Antiqua&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="myjs.js?version=2"></script>
    </head>

    <body>

    <header>
   <!-------------------------------------- nav bar ------------------------------->
    <div class="header" id="topheader">
    <nav class="navbar navbar-expand-lg bg-dark text-white fixed-top">
    <div class="container text-uppercase p-2">
    <a class="navbar-brand font-weight-bold text-white" href="#">KRISHI BAZAR</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav ml-auto text-uppercase">
        <!-- User name display if user is loged in -->
        <?php
          if(isset($_SESSION['id']))
          {
        ?>
        <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          <?php echo "Welcome , ".$_SESSION['user_name']; ?>
        </a>
        <div class="dropdown-menu bg-dark" aria-labelledby="navbarDropdown">
          <a class="dropdown-item" href="profile/profile.php">PROFILE</a>
          <a class="dropdown-item" href="logout.php">LOGOUT</a>
        </div>
      </li>
        <?php
          }
          if(!isset($_SESSION['id']))
          {
        ?>
        <li class="nav-item active">
        <a class="nav-link" href="#">Home<span class="sr-only">(current)</span></a>
        </li>
        <li class="nav-item">
        <a class="nav-link" href="#"  data-target="#mymodel" data-toggle="modal">LOGIN</a>
        </li>
        <li class="nav-item">
        <a class="nav-link" href="signup/signup.php">SIGNUP</a>
        </li>
        <?php
          }
        ?>
        <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          CATEGORIES
        </a>
        <div class="dropdown-menu bg-dark" aria-labelledby="navbarDropdown" style="overflow-y:scroll;">
          <?php
            while($category = mysqli_fetch_assoc($row))
            {
          ?>
          <a class="dropdown-item" href="productview/productview.php?category=<?php echo $category['category_id'];?>">
          <?php echo $category['name'];?></a>
          <?php
            }
          ?>
        </div>
      </li>
      </ul>
    </div>
        </div>
    </nav>
        <!-------------------nav bar-------------------------------------->
        <!-------------------banner--------------------------------------->
        <section id="banner">
            <div class="container">
                <div class="row">
                <div class="col">
                    <p class="promo-title">WELCOME TO KRISHI BAZAR</p>
                    <p class="promo-title1">"trusted mediator between farmers & consumers"</p>
                    </div>
                </div>
            </div>
        <!--------------------------------search bar----------------------->
              <div class="search text-center">
            <form action="searchWeb.php" method="post" col-md-4>
                <h3>Find everything near you</h3>
                    <div class="form-box">
                        <input type="number" class="search-feild location" placeholder="Location" name="pincode">
                        <input type="text" class="search-feild business" placeholder="What do you want" name="q">
                        <button class="btn btn-primary" type="submit">Search</button>
                    </div>
            </form>
        </div>
            <!-------------end of search bar----------------------------------->
        <section id="services">
            <div class="container text-center mt-5">
                <h1 class="title" text-white>FEATURES</h1>
                <div class="row">
                    <div class="col-md-6 services" >
                        <p>NO INTERNET NO PROBLEM</p>
                        <p>CONNECTING FARMERS AROUND INDIA</p>
                        <p>FREE SERVICES</p>
                    </div>
                    <div class="col-md-6 services" >
                        <p>FREE ADVERTISEMENT</p>
                        <p>INCREASE PROFITS</p>
                        <p>TRUSTED MEDIATOR</p>
                    </div>
                </div>
            </div>
        </section>
            <img src="img1/wave.png" class="button-img img2">
        </section>
        <!-------------------------------->
        <section id="services">
            <div class="container text-center mt-5">
                <h1 class="title">WHAT DO WE OFFER</h1>
                <hr>
                <div class="row">
                    <div class="col-md-4 services" >
                        <h4>BUY PRODUCTS</h4>
                        <hr>
                        <p>buy products online</p>
                        <a href="productview/productview.php">
                        <button type="button" class="btn btn-primary" onclick=>SEE ALL</button>
                        </a>
                    </div>
                    <div class="col-md-4 services" >
                        <h4>POST ADS</h4>
                        <hr>
                        <p>post ads to sell your products</p>
                        <a href="sellitem/sell.php">
                        <button type="button" class="btn btn-primary">POST ADS</button>
                        </a>
                    </div>
                     <div class="col-md-4 services" >
                        <h4>SMS ADS</h4>
                         <hr>
                        <p>post ads via sms to sell your products</p>
                    </div>
                </div>
            </div>
        </section>
        </div>
        </header>
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
						<form id="form" action="login.php" method="post">
						  <div class="form-group">
						    <label > Enter Mobile Number</label>
						    <input type="tel" class="form-control" id="mobile" name="mobile" aria-describedby="emailHelp" placeholder="Enter Number">
                <div id="otpForm" hidden>
						    <label >Enter OTP</label>
						    <input  type="number" class="form-control" id="otp" name="otp" id="exampleInputPassword1" placeholder="OTP">
                <br>
                <input type="hidden" class="redirect" name="redirect" value="index.php">
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
                                <img src="google-play-badge.png" height="90" width="195">
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
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
    </body>

</html>
<?php
if(isset($_SESSION['message']))
{
  $message = $_SESSION['message'];
  echo "<script type='text/javascript'>
  $(document).ready(function(){
    alert('$message');
  });
  </script>";
  unset($_SESSION['message']);
}
?>
