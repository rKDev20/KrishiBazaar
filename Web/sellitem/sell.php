<?php
session_start();
if(!isset($_SESSION['mobile']))
{
		$_SESSION['message'] = "Please LogIn/SignUp to post ad.";
	  header('location:../index.php');
}
include_once '../util/db.php';
$qryCat = "SELECT * FROM categories";
$qrySubCat = "SELECT * FROM sub_categories";
$cat = mysqli_query($conn,$qryCat);
$subCat = mysqli_query($conn,$qrySubCat);
?>
<html>
	<head>
		<title></title>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="global1.css">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
		<link href="https://fonts.googleapis.com/css?family=Nunito&display=swap" rel="stylesheet">
		<link rel="stylesheet" href="../style.css">
		<link href="https://fonts.googleapis.com/css?family=Poppins&display=swap" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Raleway&display=swap" rel="stylesheet">
		<link href="https://fonts.googleapis.com/css?family=Uncial+Antiqua&display=swap" rel="stylesheet">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
		<script src="../myjs.js"></script>
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
					<?php echo "Welcome , ".$_SESSION['user_name']; ?>
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

		<section class="container-fluid bg">
			<section class=" row justify-content-center"  >
				<section class="col-12 col-sm-6 cold-md-3">
					<form class="form-container" method="post" action="postAdd.php">
						  <div class="form-group">
								<label >Name</label>
							    <input type="text" class="form-control" name="pname">
							  </div>
								<div>
						    <label>Category</label>
								<div>
								<select class="btn btn-secondary dropdown-toggle" name="category">
									<option value="">Select Category</option>
									<?php
									while ($category = mysqli_fetch_assoc($cat)) {
										echo "<option value = ".$category['category_id'].">".$category['name']."</option>";
									}
									?>
								</select>
								</div>
							</div>
							<div>
							<label>Sub Category</label>
							<div>
							<select class="btn btn-secondary dropdown-toggle" name="SubCategory">
								<option value="">Select Sub Category</option>
								<?php
								while ($Subcategory = mysqli_fetch_assoc($subCat)) {
									echo "<option value = ".$Subcategory['sub_id'].">".$Subcategory['name']."</option>";
								}
								?>
							</select>
							</div>
						</div>
							<label >Quantity</label>
						    <input type="number" class="form-control" name="quantity">
						  </div>
						  </div>
							<label >Price</label>
						    <input type="number" class="form-control" name="price">
						  </div>
						  </div>
						  </div>
							<label >Pin Code</label>
						    <input type="number" class="form-control" name="pin" >
						  </div>
						   </div>
							<label >Description</label>
						    <input type="text" class="form-control" name="description">
						  </div>
						  </div>
						  <br>
						  <button type="submit" class="btn btn-primary btn-block">Post ad</button>
					</form>
				</section>
			</section>
		</section>
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
												<h2>USEFUL LINKS</h2>
												<ul>
														<li><a href="#">Event</a></li>
														<li><a href="#">Support</a></li>
														<li><a href="#">Hosting</a></li>
														<li><a href="#">Carerr</a></li>
												</ul>
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
