<!DOCTYPE html>
<?php
include('../connection.php');
session_start();
if(!isset($_SESSION['mobile']))
  header('location:../index.php');
$mobile = $_SESSION['mobile'];
$qry = "SELECT * FROM users WHERE mobile = $mobile";
$row = mysqli_query($conn,$qry);
$user = mysqli_fetch_assoc($row);
$request = "SELECT ad.ad_id, ad.mobile AS user_m, ad.name AS name,
        ad.pincode AS userpin, ad.status AS adstatus,
        t.transaction_id AS tid, t.proposed_rate AS prate,
        t.mobile AS buyer_m, t.pincode AS buyerpin, t.status AS tstatus
        FROM transactions t
        JOIN advertisements ad
        ON t.ad_id = ad.ad_id
        WHERE ad.mobile = $mobile AND ad.status = 1 AND t.status = 2";
$qrequest = mysqli_query($conn,$request);
$requestHistory = "SELECT ad.ad_id, ad.mobile AS user_m, ad.name AS name,
        ad.pincode AS userpin, ad.status AS adstatus,
        t.transaction_id AS tid, t.proposed_rate AS prate,
        t.mobile AS buyer_m, t.pincode AS buyerpin, t.status AS tstatus
        FROM transactions t
        JOIN advertisements ad
        ON t.ad_id = ad.ad_id
        WHERE ad.mobile = $mobile AND (t.status = 3 OR t.status = 4)";
$qrequestHistory = mysqli_query($conn,$requestHistory);
$intrest = "SELECT ad.ad_id, ad.mobile AS user_m, ad.name AS name,
            ad.pincode AS userpin, ad.status AS adstatus,
            t.transaction_id AS tid, t.proposed_rate AS prate,
            t.mobile AS buyer_m, t.pincode AS buyerpin, t.status AS tstatus
            FROM transactions t
            JOIN advertisements ad
            ON t.ad_id = ad.ad_id
            WHERE t.mobile = $mobile";
$qintrest = mysqli_query($conn,$intrest);
$product = "SELECT * FROM advertisements WHERE mobile = $mobile";
$qproduct = mysqli_query($conn,$product);
?>
<html>
  <head>
    <title>Krishi Bazar</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
    <link href="https://fonts.googleapis.com/css?family=Nunito&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="mycss.css">
    <link rel="stylesheet" href="../style.css">
    <link href="https://fonts.googleapis.com/css?family=Poppins&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Raleway&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Uncial+Antiqua&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="../myjs.js?version=2"></script>
    <style>
        table
        {
          font-family: arial, sans-serif;
          border-collapse: collapse;
          width: 100%;
          margin-bottom: 15px;
        }

        td, th
        {
          text-align: left;
          padding: 8px;
        }

        tr:nth-child(even)
        {
          background-color: #dddddd;
        }
        .tableinfo
        {
          background : #fff;
          margin-bottom: 15px;

        }
        caption
        {
          caption-side: top;
          align : top;
          display: table-caption;
          text-align: center;
        }
    </style>
  </head>
  <body>
    <!-- nav bar -->
    <div class="header" id="topheader">
    <nav class="navbar navbar-expand-lg bg-dark text-white fixed-top">
    <div class="container text-uppercase p-2">
    <a class="navbar-brand font-weight-bold text-white" href="#">KRISHI BAZAR</a>
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
          <a class="dropdown-item" href="../productview/productview.php">See All Products</a>
          <a class="dropdown-item" href="../logout.php">LOGOUT</a>
        </div>
      </li>
        <?php
          }
        ?>

      </ul>
    </div>
        </div>
    </nav>
        <!-- nav bar over -->

        <!-- profile display -->
        <div class="wrapper">
            <div class="left">
                <img src=2.jpg
                alt="user" width="100">
                <h4><?php echo $user['name']; ?></h4>
            </div>
            <div class="right">
                <div class="info">
                    <h3>Information</h3>
                    <div class="info_data">
                         <div class="data">
                           <h4>Phone</h4>
                            <p><?php echo $user['mobile']; ?></p>
                      </div>
                        <div class="data">
                           <h4>Pincode</h4>
                            <p><?php echo $user['pincode']; ?></p>
                      </div>
                    </div>
                </div>
              <div class="projects">
                    <h3>ADDRESS</h3>
                  <p><?php echo $user['address']; ?></p>
                </div>
            </div>
        </div>
        <!-- profile display over -->
        <!-- display info of transactions -->
        <div class="container-fluid" style="margin-top:600px">

          <!-- Requests -->
          <div class="row">
            <div class="col-md-1"></div>
            <div class="col-md-10 tableinfo">
              <table>
                <caption>Requests</caption>
                <?php
                  if(mysqli_num_rows($qrequest) != 0)
                  {
                ?>
                <tr>
                  <th>Product Name</th>
                  <th>Product ID</th>
                  <th>Offer Price</th>
                  <th></th>
                  <th></th>
                </tr>
                <?php
                    while($q = mysqli_fetch_assoc($qrequest))
                    {
                      if($q['tstatus'] == 2 && $q['adstatus'] == 1)
                      {
                ?>
                <tr>
                  <form action="actions/request.php" method="post">
                  <td><?php echo $q['name']; ?></td>
                  <td><?php echo $q['ad_id']; ?></td>
                  <input type = "hidden" name = "ad_id" value="<?php echo $q['ad_id']; ?>">
                  <input type = "hidden" name = "tid" value="<?php echo $q['tid']; ?>">
                  <td><?php echo $q['prate']; ?></td>
                  <td><button type="submit" class="btn btn-success" name="action" value="1">Accept</button></td>
                  <td><button type="submit" class="btn btn-danger" name="action" value="0">Reject</button></td>
                  </form>
                </tr>
                <?php
                    }
                  }
                }
                else {
                ?>
                <td style="text-align:center">--NO REQUESTS--</td>
                <?php
                    }
                ?>
              </table>
            </div>
            <div class="col-md-1"></div>
          </div>


          <!-- Request history -->
          <div class="row">
            <div class="col-md-1"></div>
            <div class="col-md-10 tableinfo">
              <table>
                <caption>Request History</caption>
                <?php
                  if(mysqli_num_rows($qrequestHistory) != 0)
                  {
                ?>
                <tr>
                  <th>Product Name</th>
                  <th>Product ID</th>
                  <th>Offer Price</th>
                  <th>Status</th>
                </tr>
                <?php
                    while ($q = mysqli_fetch_assoc($qrequestHistory)) {
                ?>
                <tr>
                  <td><?php echo $q['name']; ?></td>
                  <td><?php echo $q['ad_id']; ?></td>
                  <td><?php echo $q['prate']; ?></td>
                  <td>
                    <?php
                      if($q['tstatus'] == 3)
                        {echo "ACCEPTED";}
                      elseif ($q['tstatus'] == 4) {
                        echo "REJECTED";
                      }
                    ?>
                  </td>
                </tr>
              <?php
                  }
                  }
                  else {
              ?>

                <td style="text-align:center">--NO REQUESTS HISTORY--</td>
                <?php
              } ?>
              </table>
            </div>
            <div class="col-md-1"></div>
          </div>


          <!-- My Intrests -->
          <div class="row">
            <div class="col-md-1"></div>
            <div class="col-md-10 tableinfo">
              <table>
                <caption>My Intrest</caption>
                <?php
                  if(mysqli_num_rows($qintrest) != 0)
                  {
                ?>
                <tr>
                  <th>Product Name</th>
                  <th>Product ID</th>
                  <th>Offered Price</th>
                  <th>Status</th>
                </tr>
                <?php
                    while ($q = mysqli_fetch_assoc($qintrest)) {
                ?>
                <tr>
                  <td><?php echo $q['name']; ?></td>
                  <td><?php echo $q['ad_id']; ?></td>
                  <td><?php echo $q['prate']; ?></td>
                  <td>
                    <?php
                      if ($q['tstatus'] == 2) {
                        echo "PENDING";
                      }
                      elseif ($q['tstatus'] == 4) {
                        echo "REJECTED";
                      }
                      elseif ($q['tstatus'] == 3) {
                        echo "ACCEPTED, Contact at :".$q['user_m'];
                      }
                    ?>
                  </td>
                </tr>
                <?php
                    }
                  }
                  else {
                ?>
                <td style="text-align:center">--NO INTREST--</td>
                <?php
                }
                ?>
              </table>
            </div>
            <div class="col-md-1"></div>
          </div>



        <!-- My Products -->
        <div class="row">
          <div class="col-md-1"></div>
          <div class="col-md-10 tableinfo">
            <table>
              <caption>My Product</caption>
              <?php
                if(mysqli_num_rows($qproduct) != 0)
                {
              ?>
              <tr>
                <th>Product Name</th>
                <th>Product ID</th>
                <th>Status</th>
                <th></th>
              </tr>
              <?php
                while($q = mysqli_fetch_assoc($qproduct))
                {
              ?>
              <tr>
                <form action="actions/productStatus.php" method="post">
                <td><?php echo $q['name']; ?></td>
                <td><?php echo $q['ad_id']; ?></td>
                <input type = "hidden" name = "ad_id" value="<?php echo $q['ad_id']; ?>">
                <?php if($q['status'] == 1){ ?>
                <td>ACTIVE</td>
                <td><button type="submit" name = "action" value = 0 class="btn btn-secondary">Disable</button></td>
                <?php
                  }
                  else {
                ?>
                <td>INACTIVE</td>
                <td><button type="submit" name = "action" value = 1 class="btn btn-success">Enable</button></td>
              <?php } ?>
                </form>
              </tr>
              <?php
                }
              ?>
              <?php
              }
              else {
              ?>
              <td style="text-align:center">--NO PRODUCTS--</td>
              <?php
                  }
              ?>
            </table>
          </div>
          <div class="col-md-1"></div>
        </div>
        </div>
        <br>
        <br>
        <br>
        <!-- footer -->

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
